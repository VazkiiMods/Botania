/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.api.recipe.ICustomApothecaryColor;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockModMushroom extends MushroomBlock implements IHornHarvestable, ICustomApothecaryColor {

	private static final VoxelShape SHAPE = makeCuboidShape(4.8, 0, 4.8, 12.8, 16, 12.8);
	public final DyeColor color;

	public BlockModMushroom(DyeColor color, Properties builder) {
		super(builder);
		this.color = color;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	// [VanillaCopy] super, without light level requirement
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.down();
		BlockState iblockstate = worldIn.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		if (block != Blocks.MYCELIUM && block != Blocks.PODZOL) {
			return iblockstate.canSustainPlant(worldIn, blockpos, Direction.UP, this);
		} else {
			return true;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		int hex = color.getColorValue();
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		if (rand.nextDouble() < ConfigHandler.CLIENT.flowerParticleFrequency.get() * 0.25F) {
			SparkleParticleData data = SparkleParticleData.sparkle(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
			world.addParticle(data, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.5 + rand.nextFloat() * 0.5, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
		}
	}

	@Override
	public boolean canHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public boolean hasSpecialHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public void harvestByHorn(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {}

	@Override
	public int getParticleColor(ItemStack stack) {
		return color.getColorValue();
	}
}
