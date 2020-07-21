/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileTeruTeruBozu;

import javax.annotation.Nonnull;

public class BlockTeruTeruBozu extends BlockModWaterloggable implements BlockEntityProvider {

	private static final VoxelShape SHAPE = createCuboidShape(4, 0.16, 4, 12, 15.84, 12);

	public BlockTeruTeruBozu(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if (!world.isClient && e instanceof ItemEntity) {
			ItemEntity item = (ItemEntity) e;
			ItemStack stack = item.getStack();
			if (isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world)) {
				stack.decrement(1);
			}
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (!stack.isEmpty() && (isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world))) {
			if (!player.abilities.creativeMode) {
				stack.decrement(1);
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	private boolean isSunflower(ItemStack stack) {
		return stack.getItem() == Blocks.SUNFLOWER.asItem();
	}

	private boolean isBlueOrchid(ItemStack stack) {
		return stack.getItem() == Blocks.BLUE_ORCHID.asItem();
	}

	private boolean removeRain(World world) {
		if (world.isRaining()) {
			world.getLevelProperties().setRaining(false);
			TileTeruTeruBozu.resetRainTime(world);
			return true;
		}
		return false;
	}

	private boolean startRain(World world) {
		if (!world.isRaining()) {
			if (world.random.nextInt(10) == 0) {
				world.getLevelProperties().setRaining(true);
				TileTeruTeruBozu.resetRainTime(world);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return world.isRaining() ? 15 : 0;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileTeruTeruBozu();
	}

}
