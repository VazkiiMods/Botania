/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileTeruTeruBozu;

import javax.annotation.Nonnull;

public class BlockTeruTeruBozu extends BlockModWaterloggable {

	private static final VoxelShape SHAPE = makeCuboidShape(4, 0.16, 4, 12, 15.84, 12);

	public BlockTeruTeruBozu(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if (!world.isRemote && e instanceof ItemEntity) {
			ItemEntity item = (ItemEntity) e;
			ItemStack stack = item.getItem();
			if (isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world)) {
				stack.shrink(1);
			}
		}
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.isEmpty() && (isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world))) {
			if (!player.abilities.isCreativeMode) {
				stack.shrink(1);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	private boolean isSunflower(ItemStack stack) {
		return stack.getItem() == Blocks.SUNFLOWER.asItem();
	}

	private boolean isBlueOrchid(ItemStack stack) {
		return stack.getItem() == Blocks.BLUE_ORCHID.asItem();
	}

	private boolean removeRain(World world) {
		if (world.isRaining()) {
			world.getWorldInfo().setRaining(false);
			TileTeruTeruBozu.resetRainTime(world);
			return true;
		}
		return false;
	}

	private boolean startRain(World world) {
		if (!world.isRaining()) {
			if (world.rand.nextInt(10) == 0) {
				world.getWorldInfo().setRaining(true);
				TileTeruTeruBozu.resetRainTime(world);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return world.isRaining() ? 15 : 0;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileTeruTeruBozu();
	}

}
