/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

import vazkii.botania.common.block.ModBlocks;

import java.util.Random;

public class MysticalMushroomFeature extends Feature<MysticalMushroomConfig> {
	public MysticalMushroomFeature() {
		super(MysticalMushroomConfig.CODEC);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, MysticalMushroomConfig config) {
		boolean any = false;
		for (int i = 0; i < config.getMushroomPatchSize(); i++) {
			int x = pos.getX() + rand.nextInt(16) + 8;
			int z = pos.getZ() + rand.nextInt(16) + 8;
			int y = rand.nextInt(26) + 4;
			BlockPos pos3 = new BlockPos(x, y, z);
			DyeColor color = DyeColor.byId(rand.nextInt(16));
			BlockState mushroom = ModBlocks.getMushroom(color).defaultBlockState();
			if (world.isEmptyBlock(pos3) && mushroom.canSurvive(world, pos3)) {
				world.setBlock(pos3, mushroom, 2);
				any = true;
			}
		}
		return any;
	}
}
