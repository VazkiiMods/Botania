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
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BehaviourCocoaBeans extends BehaviorDefaultDispenseItem {

	IBehaviorDispenseItem vanillaBehaviour;
	public BehaviourCocoaBeans(IBehaviorDispenseItem vanilla) {
		vanillaBehaviour = vanilla;
	}

	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
		if(par2ItemStack.getItemDamage() == EnumDyeColor.BROWN.getDyeDamage()) {
			Block block = Blocks.COCOA;
			EnumFacing facing = par1IBlockSource.getBlockState().getValue(BlockDispenser.FACING);
			BlockPos pos = par1IBlockSource.getBlockPos().offset(facing);
			World world = par1IBlockSource.getWorld();

			BlockPos logPos = pos.offset(facing);
			IBlockState logState = world.getBlockState(logPos);
			if(logState.getBlock() == Blocks.LOG && logState.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE && world.isAirBlock(pos) && block.canPlaceBlockAt(world, pos)) {
				world.setBlockState(pos, block.getDefaultState().withProperty(BlockHorizontal.FACING, facing));
				par2ItemStack.shrink(1);
				return par2ItemStack;
			}
		}

		return vanillaBehaviour.dispense(par1IBlockSource, par2ItemStack);
	}

}
