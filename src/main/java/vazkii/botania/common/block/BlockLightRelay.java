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
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockLightRelay extends BlockModWaterloggable implements EntityBlock, IWandable {

	private static final VoxelShape SHAPE = box(5, 5, 5, 11, 11, 11);
	public final LuminizerVariant variant;

	protected BlockLightRelay(LuminizerVariant variant, Properties builder) {
		super(builder);
		this.variant = variant;
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!player.getItemInHand(hand).is(Items.ENDER_PEARL)) {
			BlockEntity te = world.getBlockEntity(pos);
			if (te instanceof TileLightRelay) {
				((TileLightRelay) te).mountEntity(player);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isClientSide && variant == LuminizerVariant.TOGGLE) {
			if (state.getValue(BlockStateProperties.POWERED) && !worldIn.hasNeighborSignal(pos)) {
				worldIn.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, false));
			} else if (!state.getValue(BlockStateProperties.POWERED) && worldIn.hasNeighborSignal(pos)) {
				worldIn.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, true));
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return variant == LuminizerVariant.DETECTOR;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction s) {
		return variant == LuminizerVariant.DETECTOR
				&& state.getValue(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileLightRelay(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTiles.LIGHT_RELAY, level.isClientSide ? TileLightRelay::clientTick : TileLightRelay::serverTick);
	}

	@Override
	public boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos, Direction side) {
		return false;
	}

}
