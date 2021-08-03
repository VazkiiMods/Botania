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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.common.block.tile.TileTeruTeruBozu;

import javax.annotation.Nonnull;

public class BlockTeruTeruBozu extends BlockModWaterloggable implements EntityBlock {

	private static final VoxelShape SHAPE = box(4, 0.16, 4, 12, 15.84, 12);

	public BlockTeruTeruBozu(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity e) {
		if (!world.isClientSide && e instanceof ItemEntity) {
			ItemEntity item = (ItemEntity) e;
			ItemStack stack = item.getItem();
			if (isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world)) {
				stack.shrink(1);
			}
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty() && (isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world))) {
			if (!player.abilities.instabuild) {
				stack.shrink(1);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	private boolean isSunflower(ItemStack stack) {
		return stack.getItem() == Blocks.SUNFLOWER.asItem();
	}

	private boolean isBlueOrchid(ItemStack stack) {
		return stack.getItem() == Blocks.BLUE_ORCHID.asItem();
	}

	private boolean removeRain(Level world) {
		if (world.isRaining()) {
			world.getLevelData().setRaining(false);
			TileTeruTeruBozu.resetRainTime(world);
			return true;
		}
		return false;
	}

	private boolean startRain(Level world) {
		if (!world.isRaining()) {
			if (world.random.nextInt(10) == 0) {
				world.getLevelData().setRaining(true);
				TileTeruTeruBozu.resetRainTime(world);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return world.isRaining() ? 15 : 0;
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
		return new TileTeruTeruBozu();
	}

}
