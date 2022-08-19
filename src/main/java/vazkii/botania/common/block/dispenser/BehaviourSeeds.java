/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 16, 2014, 10:36:05 PM (GMT)]
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviourSeeds extends BehaviorDefaultDispenseItem {

	Block block;

	public BehaviourSeeds(Block block) {
		this.block = block;
	}

	@Override
	public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
		EnumFacing facing = BlockDispenser.func_149937_b(par1IBlockSource.getBlockMetadata());
		int x = par1IBlockSource.getXInt() + facing.getFrontOffsetX();
		int y = par1IBlockSource.getYInt() + facing.getFrontOffsetY();
		int z = par1IBlockSource.getZInt() + facing.getFrontOffsetZ();
		World world = par1IBlockSource.getWorld();

		if(world.getBlock(x, y, z).isAir(world, x, y, z) && block.canBlockStay(world, x, y, z)) {
			world.setBlock(x, y, z, block);
			par2ItemStack.stackSize--;
			return par2ItemStack;
		}

		return super.dispenseStack(par1IBlockSource, par2ItemStack);
	}

}
