/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 16, 2014, 11:11:31 PM (GMT)]
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
import vazkii.botania.api.wand.IWandable;

public class BehaviourWand extends BehaviorDefaultDispenseItem {

	@Override
	protected ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
		EnumFacing facing = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());
		BlockPos pos = par1IBlockSource.getBlockPos().offset(facing);
		World world = par1IBlockSource.getWorld();
		Block block = world.getBlockState(pos).getBlock();
		if(block instanceof IWandable) {
			((IWandable) block).onUsedByWand(null, par2ItemStack, world, pos, facing.getOpposite());
			return par2ItemStack;
		}

		return super.dispenseStack(par1IBlockSource, par2ItemStack);
	}

}
