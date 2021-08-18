/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.common.block.decor.BlockFloatingFlower;

@Mixin(FarmBlock.class)
public class MixinFarmBlock {
	@Inject(
		method = "canSurvive", at = @At(value = "RETURN"),
		cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void floatingFlowerOverride(BlockState state, LevelReader worldIn, BlockPos pos,
			CallbackInfoReturnable<Boolean> cir, BlockState stateAbove) {
		if (stateAbove.getBlock() instanceof BlockFloatingFlower) {
			cir.setReturnValue(true);
		}
	}
}
