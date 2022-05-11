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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.block.BlockAltGrass;
import vazkii.botania.common.block.BlockEnchantedSoil;

@Mixin(BushBlock.class)
public class MixinBushBlock {
	@Inject(at = @At("HEAD"), method = "mayPlaceOn", cancellable = true)
	private void canPlant(BlockState floor, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (floor.getBlock() instanceof BlockEnchantedSoil || floor.getBlock() instanceof BlockAltGrass) {
			cir.setReturnValue(true);
		}
	}
}
