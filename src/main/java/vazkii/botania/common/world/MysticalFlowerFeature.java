/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.Random;

public class MysticalFlowerFeature extends Feature<MysticalFlowerConfig> {
	public MysticalFlowerFeature() {
		super(MysticalFlowerConfig.CODEC);
	}

	@Override
	public boolean generate(@Nonnull StructureWorldAccess world, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull MysticalFlowerConfig config) {
		boolean any = false;
		int dist = Math.min(8, Math.max(1, config.getPatchRadius()));
		for (int i = 0; i < config.getPatchCount(); i++) {
			if (rand.nextInt(config.getPatchChance()) == 0) {
				int x = pos.getX() + rand.nextInt(16);
				int z = pos.getZ() + rand.nextInt(16);
				int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);

				DyeColor color = DyeColor.byId(rand.nextInt(16));
				BlockState flower = ModBlocks.getFlower(color).getDefaultState();

				for (int j = 0; j < config.getPatchDensity() * config.getPatchChance(); j++) {
					int x1 = x + rand.nextInt(dist * 2) - dist;
					int y1 = y + rand.nextInt(4) - rand.nextInt(4);
					int z1 = z + rand.nextInt(dist * 2) - dist;
					BlockPos pos2 = new BlockPos(x1, y1, z1);
					if (world.isAir(pos2) && (!world.getDimension().hasCeiling() || y1 < 127) && flower.canPlaceAt(world, pos2)) {
						world.setBlockState(pos2, flower, 2);
						any = true;
						if (rand.nextDouble() < config.getTallChance()
								&& ((BlockModFlower) flower.getBlock()).isFertilizable(world, pos2, world.getBlockState(pos2), false)) {
							Block block = ModBlocks.getDoubleFlower(color);
							if (block instanceof TallPlantBlock) {
								((TallPlantBlock) block).placeAt(world, pos2, 3);
							}
						}
					}
				}
			}
		}

		return any;
	}
}
