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

import vazkii.botania.common.block.decor.FloatingFlowerBlock;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {
	@Inject(
		method = "canSurvive", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/LevelReader;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
		cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void floatingFlowerOverride(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir, BlockState stateAbove) {
		if (stateAbove.getBlock() instanceof FloatingFlowerBlock) {
			cir.setReturnValue(true);
		}
	}
}
