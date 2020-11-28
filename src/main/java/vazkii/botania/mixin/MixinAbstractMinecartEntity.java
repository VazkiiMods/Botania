/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.BlockGhostRail;
import vazkii.botania.common.block.ModBlocks;

@Mixin(AbstractMinecartEntity.class)
public class MixinAbstractMinecartEntity {
	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(CallbackInfo ci) {
		AbstractMinecartEntity self = (AbstractMinecartEntity) (Object) this;
		((BlockGhostRail) ModBlocks.ghostRail).tickCart(self);

	}

	@Inject(at = @At("HEAD"), method = "moveOnRail")
	private void handleOnRail(BlockPos pos, BlockState state, CallbackInfo ci) {
		if (state.isOf(ModBlocks.ghostRail)) {
			AbstractMinecartEntity self = (AbstractMinecartEntity) (Object) this;
			((BlockGhostRail) ModBlocks.ghostRail).onMinecartPass(self.world, self);
		}

	}

}
