/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileHourglass;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockHourglass extends BlockModWaterloggable implements IManaTrigger, BlockEntityProvider, IWandable, IWandHUD {

	private static final VoxelShape SHAPE = createCuboidShape(4, 0, 4, 12, 18.4, 12);

	protected BlockHourglass(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		TileHourglass hourglass = (TileHourglass) world.getBlockEntity(pos);
		ItemStack hgStack = hourglass.getItemHandler().getStack(0);
		ItemStack stack = player.getStackInHand(hand);
		if (!stack.isEmpty() && stack.getItem() == ModItems.twigWand) {
			return ActionResult.PASS;
		}

		if (hourglass.lock) {
			if (!player.world.isClient) {
				player.sendSystemMessage(new TranslatableText("botaniamisc.hourglassLock"), Util.NIL_UUID);
			}
			return ActionResult.FAIL;
		}

		if (hgStack.isEmpty() && TileHourglass.getStackItemTime(stack) > 0) {
			hourglass.getItemHandler().setStack(0, stack.copy());
			stack.setCount(0);
			return ActionResult.SUCCESS;
		} else if (!hgStack.isEmpty()) {
			player.inventory.offerOrDrop(player.world, hgStack);
			hourglass.getItemHandler().setStack(0, ItemStack.EMPTY);
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction side) {
		return state.get(Properties.POWERED) ? 15 : 0;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (state.get(Properties.POWERED)) {
			world.setBlockState(pos, state.with(Properties.POWERED, false));
		}
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getBlockEntity(pos);
			InventoryHelper.dropInventory(inv, world, state, pos);
			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileHourglass();
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if (!burst.isFake()) {
			TileHourglass tile = (TileHourglass) world.getBlockEntity(pos);
			tile.onManaCollide();
		}
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		TileHourglass tile = (TileHourglass) world.getBlockEntity(pos);
		tile.lock = !tile.lock;
		if (!world.isClient) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		}
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		TileHourglass tile = (TileHourglass) world.getBlockEntity(pos);
		tile.renderHUD(ms);
	}

}
