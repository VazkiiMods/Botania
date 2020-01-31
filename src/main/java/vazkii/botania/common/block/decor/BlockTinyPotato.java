/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 18, 2014, 7:58:08 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.core.helper.InventoryHelper;

import javax.annotation.Nonnull;

public class BlockTinyPotato extends BlockMod {

	private static final VoxelShape SHAPE = makeCuboidShape(6, 0, 6, 10, 6, 10);

	public BlockTinyPotato(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState()
				.with(BotaniaStateProps.CARDINALS, Direction.SOUTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.CARDINALS);
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
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileTinyPotato) {
			((TileTinyPotato) tile).interact(player, hand, player.getHeldItem(hand), hit.getFace());
			if(!world.isRemote) {
				AxisAlignedBB box = SHAPE.getBoundingBox();
				((ServerWorld) world).spawnParticle(ParticleTypes.HEART, pos.getX() + box.minX + Math.random() * (box.maxX - box.minX), pos.getY() + box.maxY, pos.getZ() + box.minZ + Math.random() * (box.maxZ - box.minZ), 1, 0 ,0, 0, 0);
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return getDefaultState().with(BotaniaStateProps.CARDINALS, ctx.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if (stack.hasDisplayName())
			((TileTinyPotato) world.getTileEntity(pos)).name = stack.getDisplayName();
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
		return new TileTinyPotato();
	}
}
