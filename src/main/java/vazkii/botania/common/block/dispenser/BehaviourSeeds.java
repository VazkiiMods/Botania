/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BehaviourSeeds extends DefaultDispenseItemBehavior {
	private Block block;

	public BehaviourSeeds(Block block) {
		this.block = block;
	}

	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		Direction facing = source.getBlockState().get(DispenserBlock.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);
		World world = source.getWorld();

		if (world.isAirBlock(pos) && block.getDefaultState().isValidPosition(world, pos)) {
			world.setBlockState(pos, block.getDefaultState());
			stack.shrink(1);
			return stack;
		}

		return super.dispenseStack(source, stack);
	}

}
