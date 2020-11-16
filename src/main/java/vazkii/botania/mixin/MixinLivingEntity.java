package vazkii.botania.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.brew.potion.PotionSoulCross;
import vazkii.botania.common.components.EntityComponents;
import vazkii.botania.common.components.LooniumComponent;
import vazkii.botania.common.item.ItemCraftingHalo;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumAxe;
import vazkii.botania.common.item.rod.ItemGravityRod;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
	@Shadow
	public World world;

	@Shadow
	public abstract void dropStack(ItemStack stack);

	@Shadow public abstract ItemStack getStackInHand(Hand hand);

	@Shadow protected int playerHitTimer;

	@Inject(at = @At("HEAD"), cancellable = true, method = "dropLoot")
	private void dropLoonium(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		LooniumComponent comp = EntityComponents.LOONIUM_DROP.getNullable(this);
		if (comp != null && !comp.getDrop().isEmpty()) {
			dropStack(comp.getDrop());
			ci.cancel();
		}
	}

	@Inject(at = @At("RETURN"), method = "drop")
	private void dropEnd(DamageSource source, CallbackInfo ci) {
		ItemElementiumAxe.onEntityDrops(playerHitTimer, source, (LivingEntity) (Object) this);
	}

	/**
	 * Applies soul cross effect when being killed
	 */
	@Inject(at = @At("RETURN"), method = "onKilledBy")
	private void healKiller(@Nullable LivingEntity adversary, CallbackInfo ci) {
		if (!world.isClient && adversary != null) {
			PotionSoulCross.onEntityKill((LivingEntity) (Object) this, adversary);
		}

	}

	@Inject(at = @At("HEAD"), method = "swingHand(Lnet/minecraft/util/Hand;Z)V", cancellable = true)
	private void onSwing(Hand hand, boolean bl, CallbackInfo ci) {
		ItemStack stack = getStackInHand(hand);
		LivingEntity self = (LivingEntity) (Object) this;
		if (!world.isClient) {
			if (stack.getItem() instanceof ItemCraftingHalo && ItemCraftingHalo.onEntitySwing(stack, self)) {
				ci.cancel();
			} else if (stack.getItem() instanceof ItemGravityRod) {
				ItemGravityRod.onEntitySwing(self);
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "jump")
	private void onJump(CallbackInfo ci) {
		((ItemTravelBelt) ModItems.travelBelt).onPlayerJump((LivingEntity) (Object) this);
		((ItemTravelBelt) ModItems.superTravelBelt).onPlayerJump((LivingEntity) (Object) this);
		((ItemTravelBelt) ModItems.speedUpBelt).onPlayerJump((LivingEntity) (Object) this);
	}
}
