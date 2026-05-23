package vazkii.botania.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.BotaniaItems;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin_NoDespawn extends Entity {
	// decently close to a seamless reset cycle, and rare enough to likely go by unnoticed:
	@Unique
	private static final int BOTANIA_RESET_UNLIMITED_LIFETIME_AFTER_TICKS = 16600;
	@Unique
	private static final short BOTANIA_UNLIMITED_LIFETIME = Short.MIN_VALUE;

	@Shadow
	private int age;

	@Unique
	private boolean botania_checkedAge;

	private ItemEntityMixin_NoDespawn(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract ItemStack getItem();

	/**
	 * As an item entity is spawned and its item is set, also set unlimited lifetime, if appropriate.
	 */
	@WrapOperation(
		method = "onSyncedDataUpdated",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;")
	)
	private ItemStack setNeverDespawnIfAppropriate(ItemEntity instance, Operation<ItemStack> original) {
		ItemStack item = original.call(instance);
		if (age == 0 && BotaniaItems.isNoDespawn(item.getItem())) {
			instance.setUnlimitedLifetime();
		}
		return item;
	}

	/**
	 * On the client an unlimited lifetime also causes the item to no longer bob and spin, so fake that by returning a
	 * value that actually increases every tick, but wrap it around eventually.
	 */
	@WrapMethod(method = "getAge")
	private int adjustClientAgeForRendering(Operation<Integer> original) {
		int originalAge = original.call();
		return originalAge == BOTANIA_UNLIMITED_LIFETIME && level().isClientSide()
				&& BotaniaItems.isNoDespawn(getItem().getItem())
						? originalAge + tickCount % BOTANIA_RESET_UNLIMITED_LIFETIME_AFTER_TICKS
						: originalAge;
	}

	/**
	 * For diagnostics, check if this item entity is for a no-despawn Botania item and report any incorrect age.
	 */
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
	private void checkStillWontDespawn(CallbackInfo ci) {
		if (!botania_checkedAge && age != BOTANIA_UNLIMITED_LIFETIME) {
			botania_checkedAge = true;
			Item item = getItem().getItem();
			if (age < 5000 && BotaniaItems.isNoDespawn(item)) {
				if (age == -6000) {
					// someone meant well
					BotaniaAPI.LOGGER.info(
							"ItemEntity for {} at {} was set to extended despawn time, reverting to no despawn. (item age was {})",
							item, position(), age);
				} else {
					BotaniaAPI.LOGGER.warn(
							"ItemEntity for {} at {} was set to allow despawning, reverting to no despawn. (item age was {})",
							item, position(), age);
				}
				age = BOTANIA_UNLIMITED_LIFETIME;
			}
		}
	}

	/**
	 * For diagnostics, report when a despawning item was a no-despawn Botania item.
	 */
	@Inject(
		method = "tick",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V"),
		slice = @Slice(from = @At(value = "CONSTANT", args = "intValue=6000"))
	)
	private void reportUnintendedDespawn(CallbackInfo ci) {
		ItemStack stack = getItem();
		Item item = stack.getItem();
		if (BotaniaItems.isNoDespawn(item)) {
			BotaniaAPI.LOGGER.warn("ItemEntity for {} has despawned at {}! Item stack data for reference: {}",
					item, position(), stack.save(level().registryAccess()));
		}
	}
}
