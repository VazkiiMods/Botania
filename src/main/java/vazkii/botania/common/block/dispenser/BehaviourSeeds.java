/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [21/09/2016, 21:08:33 (GMT)]
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BehaviourSeeds extends BehaviorDefaultDispenseItem {
	private Block block;

	public BehaviourSeeds(Block block) {
		this.block = block;
	}

	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		EnumFacing facing = source.getBlockState().get(BlockDispenser.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);
		World world = source.getWorld();

		if(world.isAirBlock(pos) && block.getDefaultState().isValidPosition(world, pos)) {
			world.setBlockState(pos, block.getDefaultState());
			stack.shrink(1);
			return stack;
		}

		return super.dispenseStack(source, stack);
	}

}