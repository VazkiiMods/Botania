package vazkii.botania.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.xplat.XplatAbstractions;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@Shadow
	private int age;

	@Inject(
		method = "tick", at = @At(
			value = "FIELD", opcode = 0xB5 /* PUTFIELD */, shift = At.Shift.AFTER,
			target = "Lnet/minecraft/world/entity/item/ItemEntity;age:I"
		)
	)
	private void disableDespawn(CallbackInfo ci) {
		if (age < 5000 || age > 5100) {
			// Allow items close to despawn (like fakes spawned by /give) to despawn normally.
			// Leave wiggle room for mods that might give special appearance for items close to despawn (like 1.12 Quark)
			return;
		}
		Item item = ((ItemEntity) (Object) this).getItem().getItem();
		if (BotaniaItems.isNoDespawn(item)) {
			age = 0;
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		XplatAbstractions.INSTANCE.itemFlagsComponent(self).tick();
	}
}
