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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.Random;

public class MysticalFlowerFeature extends Feature<MysticalFlowerConfig> {
	public MysticalFlowerFeature() {
		super(MysticalFlowerConfig.CODEC);
	}

	@Override
	public boolean place(@Nonnull FeaturePlaceContext<MysticalFlowerConfig> ctx) {
		WorldGenLevel level = ctx.level();
		Random rand = ctx.random();
		MysticalFlowerConfig config = ctx.config();
		BlockPos pos = ctx.origin();
		boolean any = false;
		int dist = Math.min(8, Math.max(1, config.getPatchRadius()));
		for (int i = 0; i < config.getPatchCount(); i++) {
			if (rand.nextInt(config.getPatchChance()) == 0) {
				int x = pos.getX() + rand.nextInt(16);
				int z = pos.getZ() + rand.nextInt(16);
				int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

				DyeColor color = DyeColor.byId(rand.nextInt(16));
				BlockState flower = ModBlocks.getFlower(color).defaultBlockState();

				for (int j = 0; j < config.getPatchDensity() * config.getPatchChance(); j++) {
					int x1 = x + rand.nextInt(dist * 2) - dist;
					int y1 = y + rand.nextInt(4) - rand.nextInt(4);
					int z1 = z + rand.nextInt(dist * 2) - dist;
					BlockPos pos2 = new BlockPos(x1, y1, z1);
					if (level.isEmptyBlock(pos2) && (!level.dimensionType().hasCeiling() || y1 < 127) && flower.canSurvive(level, pos2)) {
						level.setBlock(pos2, flower, 2);
						any = true;
						if (rand.nextDouble() < config.getTallChance()
								&& ((BlockModFlower) flower.getBlock()).isValidBonemealTarget(level, pos2, level.getBlockState(pos2), false)) {
							Block block = ModBlocks.getDoubleFlower(color);
							if (block instanceof DoublePlantBlock) {
								DoublePlantBlock.placeAt(level, block.defaultBlockState(), pos2, 3);
							}
						}
					}
				}
			}
		}

		return any;
	}
}
