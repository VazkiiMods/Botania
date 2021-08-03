/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;

import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

// Taken from vanilla pumpkin dispense behaviour
public class BehaviourFelPumpkin extends OptionalDispenseItemBehavior {
	@Nonnull
	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level world = source.getLevel();
		BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		Block blockcarvedpumpkin = ModBlocks.felPumpkin;
		this.setSuccess(false);
		if (world.isEmptyBlock(blockpos) && world.getBlockState(blockpos.below()).getBlock() == Blocks.IRON_BARS
				&& world.getBlockState(blockpos.below(2)).getBlock() == Blocks.IRON_BARS) // Botania - Check for iron bars
		{
			this.setSuccess(true);
			if (!world.isClientSide) {
				world.setBlock(blockpos, blockcarvedpumpkin.defaultBlockState(), 3);
			}

			stack.shrink(1);
		}

		return stack;
	}
}
