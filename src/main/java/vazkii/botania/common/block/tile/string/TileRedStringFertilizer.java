/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 16, 2014, 7:17:16 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class TileRedStringFertilizer extends TileRedString {

	public boolean canGrow(World p_149851_1_, boolean p_149851_5_) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();

		return block instanceof IGrowable ? ((IGrowable) block).canGrow(p_149851_1_, binding, p_149851_1_.getBlockState(binding), p_149851_5_) : false;
	}

	public boolean canUseBonemeal(World p_149852_1_, Random p_149852_2_) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		return block instanceof IGrowable ? ((IGrowable) block).canUseBonemeal(p_149852_1_, p_149852_2_, binding, p_149852_1_.getBlockState(binding)) : false;
	}

	public void grow(World p_149853_1_, Random p_149853_2_) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		if(block instanceof IGrowable)
			((IGrowable) block).grow(p_149853_1_, p_149853_2_, binding, p_149853_1_.getBlockState(binding));
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return worldObj.getBlockState(pos).getBlock() instanceof IGrowable;
	}

}
