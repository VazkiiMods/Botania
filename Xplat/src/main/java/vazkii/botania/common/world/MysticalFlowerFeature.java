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
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlock;

/**
 * This Feature is essentially the same as vanilla's
 * {@link net.minecraft.world.level.levelgen.feature.SimpleBlockFeature},
 * but any of Botania's mystical flowers have a chance to be replaced with their tall variant.
 */
public class MysticalFlowerFeature extends Feature<MysticalFlowerConfig> {
	public MysticalFlowerFeature() {
		super(MysticalFlowerConfig.CODEC);
	}

	@Override
	public boolean place(@NotNull FeaturePlaceContext<MysticalFlowerConfig> ctx) {
		MysticalFlowerConfig config = ctx.config();
		WorldGenLevel level = ctx.level();
		BlockPos pos = ctx.origin();
		BlockState state = config.toPlace().getState(ctx.random(), pos);
		if (state.canSurvive(level, pos)) {
			if (state.getBlock().getClass() == BotaniaFlowerBlock.class
					&& ctx.random().nextFloat() < config.tallChance()) {
				if (!level.isEmptyBlock(pos.above())) {
					return false;
				}

				var color = ((BotaniaFlowerBlock) state.getBlock()).color;
				var doubleFlower = BotaniaBlocks.getDoubleFlower(color);
				DoublePlantBlock.placeAt(level, doubleFlower.defaultBlockState(), pos, Block.UPDATE_CLIENTS);
			} else {
				level.setBlock(pos, state, Block.UPDATE_CLIENTS);
			}

			return true;
		} else {
			return false;
		}
	}
}
