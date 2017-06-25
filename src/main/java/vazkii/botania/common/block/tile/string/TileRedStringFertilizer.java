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

	public boolean canGrow(World world, boolean isClient) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();

		return block instanceof IGrowable ? ((IGrowable) block).canGrow(world, binding, world.getBlockState(binding), isClient) : false;
	}

	public boolean canUseBonemeal(World world, Random rand) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		return block instanceof IGrowable ? ((IGrowable) block).canUseBonemeal(world, rand, binding, world.getBlockState(binding)) : false;
	}

	public void grow(World world, Random rand) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		if(block instanceof IGrowable)
			((IGrowable) block).grow(world, rand, binding, world.getBlockState(binding));
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getBlockState(pos).getBlock() instanceof IGrowable;
	}

}
