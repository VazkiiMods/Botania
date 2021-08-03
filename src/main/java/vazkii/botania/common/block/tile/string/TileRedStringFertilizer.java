/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;

import vazkii.botania.common.block.tile.ModTiles;

import java.util.Random;

public class TileRedStringFertilizer extends TileRedString {
	public TileRedStringFertilizer() {
		super(ModTiles.RED_STRING_FERTILIZER);
	}

	public boolean canGrow(BlockGetter world, boolean isClient) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();

		return block instanceof BonemealableBlock && ((BonemealableBlock) block).isValidBonemealTarget(world, binding, world.getBlockState(binding), isClient);
	}

	public boolean canUseBonemeal(Level world, Random rand) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		return block instanceof BonemealableBlock && ((BonemealableBlock) block).isBonemealSuccess(world, rand, binding, world.getBlockState(binding));
	}

	public void grow(ServerLevel world, Random rand) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		if (block instanceof BonemealableBlock) {
			((BonemealableBlock) block).performBonemeal(world, rand, binding, world.getBlockState(binding));
		}
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return level.getBlockState(pos).getBlock() instanceof BonemealableBlock;
	}

}
