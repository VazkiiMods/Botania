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
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.Nonnull;

public class BehaviourSeeds extends OptionalDispenseItemBehavior {
	private Block block;

	public BehaviourSeeds(Block block) {
		this.block = block;
	}

	@Nonnull
	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
		BlockPos pos = source.getPos().relative(facing);
		Level world = source.getLevel();

		setSuccess(false);
		if (world.isEmptyBlock(pos) && block.defaultBlockState().canSurvive(world, pos)) {
			world.setBlockAndUpdate(pos, block.defaultBlockState());
			stack.shrink(1);
			setSuccess(true);
		}

		return stack;
	}

}
