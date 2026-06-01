/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

import vazkii.botania.common.block.PetalApothecaryBlock;

import java.util.function.Predicate;

@Mixin(PointedDripstoneBlock.class)
public class PointedDripstoneBlockMixin {
	@ModifyVariable(
		method = "findFillableCauldronBelowStalactiteTip", ordinal = 0, at = @At(value = "LOAD", ordinal = 0),
		slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/core/Direction;getAxisDirection()Lnet/minecraft/core/Direction$AxisDirection;"))
	)
	private static Predicate<BlockState> isCauldronOrApothecary(Predicate<BlockState> cauldronPredicate, @Local(argsOnly = true) Fluid fluid) {
		return cauldronPredicate.or(blockState -> blockState.getBlock() instanceof PetalApothecaryBlock apothecary
				&& apothecary.canReceiveStalactiteDrip(blockState, fluid));
	}
}
