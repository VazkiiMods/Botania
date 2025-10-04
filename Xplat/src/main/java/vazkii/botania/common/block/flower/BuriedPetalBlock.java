/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.internal.Colored;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.item.material.MysticalPetalItem;

import java.util.function.Function;

public class BuriedPetalBlock extends BushBlock implements TallFlowerGrower, Colored {
	public static final MapCodec<BuriedPetalBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			StringRepresentable.fromEnum(DyeColor::values).fieldOf("color").forGetter(BuriedPetalBlock::getColor),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("tallFLower").forGetter(BuriedPetalBlock::getTallFlower),
			propertiesCodec()
	).apply(instance, (dyeColor, block, props) -> new BuriedPetalBlock(dyeColor, b -> block, props)));

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 1.6, 16);

	public final DyeColor color;
	private final Function<TallFlowerGrower, @Nullable Block> tallFlowerFunction;

	@Override
	protected MapCodec<BuriedPetalBlock> codec() {
		return CODEC;
	}

	public BuriedPetalBlock(DyeColor color, Function<TallFlowerGrower, @Nullable Block> tallFlowerFunction, Properties builder) {
		super(builder);
		this.color = color;
		this.tallFlowerFunction = tallFlowerFunction;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		int color = MysticalPetalItem.getPetalLikeColor(this.color);
		float r = FastColor.ARGB32.red(color) / 255f;
		float g = FastColor.ARGB32.green(color) / 255f;
		float b = FastColor.ARGB32.blue(color) / 255f;

		SparkleParticleData data = SparkleParticleData.noClip(rand.nextFloat(), r, g, b, 5);
		world.addParticle(data, pos.getX() + 0.25 + rand.nextFloat() * 0.5, pos.getY() + 0.1 + rand.nextFloat() * 0.1, pos.getZ() + 0.25 + rand.nextFloat() * 0.5, 0, 0, 0);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public DyeColor getColor() {
		return color;
	}

	@Override
	public @Nullable Block getTallFlower() {
		return tallFlowerFunction.apply(this);
	}
}
