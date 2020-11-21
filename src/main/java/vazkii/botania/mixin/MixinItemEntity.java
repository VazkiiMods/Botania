package vazkii.botania.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.item.ItemFlowerBag;

@Mixin(ItemEntity.class)
public class MixinItemEntity {
	@Inject(at = @At("HEAD"), method = "onPlayerCollision", cancellable = true)
	private void onPickup(PlayerEntity player, CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (ItemFlowerBag.onPickupItem(self, player)) {
			ci.cancel();
		}
	}
}
