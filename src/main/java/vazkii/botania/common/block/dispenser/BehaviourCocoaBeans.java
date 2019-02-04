/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [21/09/2016, 21:08:41 (GMT)]
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BehaviourCocoaBeans extends BehaviorDefaultDispenseItem {
	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		Block block = Blocks.COCOA;
		EnumFacing facing = source.getBlockState().get(BlockDispenser.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);
		World world = source.getWorld();

		IBlockState cocoa = block.getStateForPlacement(new BlockItemUseContext(world, null, ItemStack.EMPTY, pos, facing.getOpposite(), 0, 0, 0));
		if(cocoa != null && world.isAirBlock(pos)) {
			world.setBlockState(pos, cocoa);
			stack.shrink(1);
			return stack;
		}

		return stack;
	}

}
