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
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileHourglass;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockHourglass extends BlockModWaterloggable implements IManaTrigger, EntityBlock, IWandable, IWandHUD {

	private static final VoxelShape SHAPE = box(4, 0, 4, 12, 18.4, 12);

	protected BlockHourglass(Properties builder) {
		super(builder);
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
		TileHourglass hourglass = (TileHourglass) world.getBlockEntity(pos);
		ItemStack hgStack = hourglass.getItemHandler().getItem(0);
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty() && stack.getItem() == ModItems.twigWand) {
			return InteractionResult.PASS;
		}

		if (hourglass.lock) {
			if (!player.level.isClientSide && hand == InteractionHand.OFF_HAND) {
				player.sendMessage(new TranslatableComponent("botaniamisc.hourglassLock"), Util.NIL_UUID);
			}
			return InteractionResult.FAIL;
		}

		if (hgStack.isEmpty() && TileHourglass.getStackItemTime(stack) > 0) {
			hourglass.getItemHandler().setItem(0, stack.copy());
			stack.setCount(0);
			return InteractionResult.SUCCESS;
		} else if (!hgStack.isEmpty()) {
			player.getInventory().placeItemBackInInventory(player.level, hgStack);
			hourglass.getItemHandler().setItem(0, ItemStack.EMPTY);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
		return state.getValue(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (state.getValue(BlockStateProperties.POWERED)) {
			world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, false));
		}
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				Containers.dropContents(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileHourglass(pos, state);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, Level world, BlockPos pos) {
		if (!burst.isFake()) {
			TileHourglass tile = (TileHourglass) world.getBlockEntity(pos);
			tile.onManaCollide();
		}
	}

	@Override
	public boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos, Direction side) {
		TileHourglass tile = (TileHourglass) world.getBlockEntity(pos);
		tile.lock = !tile.lock;
		if (!world.isClientSide) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		}
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc, Level world, BlockPos pos) {
		TileHourglass tile = (TileHourglass) world.getBlockEntity(pos);
		tile.renderHUD(ms);
	}

}
