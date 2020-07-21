/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.ModTiles;

import java.util.Random;

public class TileRedStringFertilizer extends TileRedString {
	public TileRedStringFertilizer() {
		super(ModTiles.RED_STRING_FERTILIZER);
	}

	public boolean canGrow(BlockView world, boolean isClient) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();

		return block instanceof Fertilizable && ((Fertilizable) block).isFertilizable(world, binding, world.getBlockState(binding), isClient);
	}

	public boolean canUseBonemeal(World world, Random rand) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		return block instanceof Fertilizable && ((Fertilizable) block).canGrow(world, rand, binding, world.getBlockState(binding));
	}

	public void grow(ServerWorld world, Random rand) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		if (block instanceof Fertilizable) {
			((Fertilizable) block).grow(world, rand, binding, world.getBlockState(binding));
		}
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getBlockState(pos).getBlock() instanceof Fertilizable;
	}

}
