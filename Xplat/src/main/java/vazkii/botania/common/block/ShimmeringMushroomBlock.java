/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.internal.Colored;
import vazkii.botania.api.recipe.CustomApothecaryColor;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.item.material.MysticalPetalItem;
import vazkii.botania.xplat.BotaniaConfig;

public class ShimmeringMushroomBlock extends MushroomBlock implements CustomApothecaryColor, Colored {

	private static final VoxelShape SHAPE = box(4.8, 0, 4.8, 12.8, 16, 12.8);
	public final DyeColor color;

	public ShimmeringMushroomBlock(DyeColor color, Properties properties) {
		super(TreeFeatures.HUGE_BROWN_MUSHROOM /* Doesn't matter, we override the grow method */, properties);
		this.color = color;
	}

	@Override
	public DyeColor getColor() {
		return color;
	}

	@Override
	public boolean growMushroom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	// [VanillaCopy] super, without light level requirement
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState blockstate = level.getBlockState(blockpos);
		if (!blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
			return this.mayPlaceOn(blockstate, level, blockpos);
		} else {
			return true;
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextDouble() >= BotaniaConfig.client().flowerParticleFrequency() * 0.25F) {
			return;
		}
		int color = MysticalPetalItem.getPetalLikeColor(this.color);
		float r = FastColor.ARGB32.red(color) / 255f;
		float g = FastColor.ARGB32.red(color) / 255f;
		float b = FastColor.ARGB32.red(color) / 255f;

		SparkleParticleData data = SparkleParticleData.sparkle(random.nextFloat(), r, g, b, 5);
		level.addParticle(data,
				pos.getX() + 0.3 + random.nextFloat() * 0.5,
				pos.getY() + 0.5 + random.nextFloat() * 0.5,
				pos.getZ() + 0.3 + random.nextFloat() * 0.5,
				0, 0, 0);
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return MysticalPetalItem.getPetalLikeColor(color);
	}
}
