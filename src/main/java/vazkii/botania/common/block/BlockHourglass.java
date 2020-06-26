/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

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

public class BlockHourglass extends BlockModWaterloggable implements IManaTrigger, ITileEntityProvider, IWandable, IWandHUD {

	private static final VoxelShape SHAPE = makeCuboidShape(4, 0, 4, 12, 18.4, 12);

	protected BlockHourglass(Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileHourglass hourglass = (TileHourglass) world.getTileEntity(pos);
		ItemStack hgStack = hourglass.getItemHandler().getStackInSlot(0);
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.isEmpty() && stack.getItem() == ModItems.twigWand) {
			return ActionResultType.PASS;
		}

		if (hourglass.lock) {
			if (!player.world.isRemote) {
				player.sendMessage(new TranslationTextComponent("botaniamisc.hourglassLock"), Util.field_240973_b_);
			}
			return ActionResultType.FAIL;
		}

		if (hgStack.isEmpty() && TileHourglass.getStackItemTime(stack) > 0) {
			hourglass.getItemHandler().setStackInSlot(0, stack.copy());
			stack.setCount(0);
			return ActionResultType.SUCCESS;
		} else if (!hgStack.isEmpty()) {
			ItemHandlerHelper.giveItemToPlayer(player, hgStack);
			hourglass.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return state.get(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (state.get(BlockStateProperties.POWERED)) {
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false));
		}
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);
			InventoryHelper.dropInventory(inv, world, state, pos);
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileHourglass();
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if (!burst.isFake()) {
			TileHourglass tile = (TileHourglass) world.getTileEntity(pos);
			tile.onManaCollide();
		}
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		TileHourglass tile = (TileHourglass) world.getTileEntity(pos);
		tile.lock = !tile.lock;
		if (!world.isRemote) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		}
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		TileHourglass tile = (TileHourglass) world.getTileEntity(pos);
		tile.renderHUD();
	}

}
