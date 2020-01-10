/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 24, 2015, 3:15:11 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockAvatar extends BlockMod {

	private static final VoxelShape X_AABB = makeCuboidShape(5, 0, 3.5, 11, 17, 12.5);
	private static final VoxelShape Z_AABB = makeCuboidShape(3.5, 0, 5, 12.5, 17, 11);

	protected BlockAvatar(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.CARDINALS, Direction.NORTH));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		if(state.get(BotaniaStateProps.CARDINALS).getAxis() == Direction.Axis.X)
			return X_AABB;
		else return Z_AABB;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.CARDINALS);
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileAvatar avatar = (TileAvatar) world.getTileEntity(pos);
		ItemStack stackOnAvatar = avatar.getItemHandler().getStackInSlot(0);
		ItemStack stackOnPlayer = player.getHeldItem(hand);
		if(!stackOnAvatar.isEmpty()) {
			avatar.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
			ItemHandlerHelper.giveItemToPlayer(player, stackOnAvatar);
			return ActionResultType.SUCCESS;
		} else if(!stackOnPlayer.isEmpty() && stackOnPlayer.getItem() instanceof IAvatarWieldable) {
			avatar.getItemHandler().setStackInSlot(0, stackOnPlayer.split(1));
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newstate, boolean isMoving) {
		if(state.getBlock() != newstate.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);
			InventoryHelper.dropInventory(inv, world, state, pos);
			super.onReplaced(state, world, pos, newstate, isMoving);
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(BotaniaStateProps.CARDINALS, context.getPlacementHorizontalFacing().getOpposite());
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
		return new TileAvatar();
	}

	@Nonnull
	@Override
	public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
		return state.with(BotaniaStateProps.CARDINALS, mirror.mirror(state.get(BotaniaStateProps.CARDINALS)));
	}

	@Nonnull
	@Override
	public BlockState rotate(@Nonnull BlockState state, Rotation rot) {
		return state.with(BotaniaStateProps.CARDINALS, rot.rotate(state.get(BotaniaStateProps.CARDINALS)));
	}
}
