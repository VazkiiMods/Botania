/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.item.IHourglassTrigger;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileAnimatedTorch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockAnimatedTorch extends BlockModWaterloggable implements EntityBlock, IWandable, IManaTrigger, IHourglassTrigger, IWandHUD {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 4, 16);

	public BlockAnimatedTorch(Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
		if (hand == InteractionHand.MAIN_HAND && playerIn.isShiftKeyDown() && playerIn.getItemInHand(hand).isEmpty()) {
			((TileAnimatedTorch) worldIn.getBlockEntity(pos)).handRotate();
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileAnimatedTorch) world.getBlockEntity(pos)).onPlace(entity);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, Level world, BlockPos pos) {
		if (!burst.isFake()) {
			((TileAnimatedTorch) world.getBlockEntity(pos)).toggle();
		}
	}

	@Override
	public void onTriggeredByHourglass(Level world, BlockPos pos, BlockEntity hourglass) {
		((TileAnimatedTorch) world.getBlockEntity(pos)).toggle();
	}

	@Override
	public boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos, Direction side) {
		((TileAnimatedTorch) world.getBlockEntity(pos)).onWanded();
		return true;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void renderHUD(PoseStack ms, Minecraft mc, Level world, BlockPos pos) {
		((TileAnimatedTorch) world.getBlockEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return getSignal(blockState, blockAccess, pos, side);
	}

	@Override
	public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
		TileAnimatedTorch tile = (TileAnimatedTorch) blockAccess.getBlockEntity(pos);

		if (tile.rotating) {
			return 0;
		}

		if (TileAnimatedTorch.SIDES[tile.side] == side) {
			return 15;
		}

		return 0;
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileAnimatedTorch(pos, state);
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTiles.ANIMATED_TORCH, TileAnimatedTorch::commonTick);
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
		// Block entity is already gone so best we can do is just notify everyone
		world.blockUpdated(pos, this);
		super.destroy(world, pos, state);
	}

}
