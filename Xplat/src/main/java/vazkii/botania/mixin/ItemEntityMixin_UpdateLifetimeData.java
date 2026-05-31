package vazkii.botania.mixin;

import net.minecraft.world.entity.item.ItemEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.internal_caps.ItemLifetime;

@Mixin(ItemEntity.class)
public class ItemEntityMixin_UpdateLifetimeData {
	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		ItemLifetime.increment(self);
	}
}
