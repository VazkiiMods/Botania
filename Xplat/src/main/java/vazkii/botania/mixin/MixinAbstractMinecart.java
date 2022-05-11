package vazkii.botania.mixin;

import net.minecraft.world.entity.vehicle.AbstractMinecart;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.BlockGhostRail;
import vazkii.botania.common.block.ModBlocks;

@Mixin(AbstractMinecart.class)
public class MixinAbstractMinecart {
	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(CallbackInfo ci) {
		AbstractMinecart self = (AbstractMinecart) (Object) this;
		((BlockGhostRail) ModBlocks.ghostRail).tickCart(self);
	}
}
