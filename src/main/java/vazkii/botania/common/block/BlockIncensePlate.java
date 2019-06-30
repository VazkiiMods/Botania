/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 15, 2015, 4:07:09 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.TileIncensePlate;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockIncensePlate extends BlockMod implements ILexiconable {

	private static final VoxelShape X_SHAPE = makeCuboidShape(6, 0, 2, 10, 1, 14);
	private static final VoxelShape Z_SHAPE = makeCuboidShape(2, 0, 6, 14, 1, 10);

	protected BlockIncensePlate(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.CARDINALS, Direction.SOUTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.CARDINALS);
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileIncensePlate plate = (TileIncensePlate) world.getTileEntity(pos);
		ItemStack plateStack = plate.getItemHandler().getStackInSlot(0);
		ItemStack stack = player.getHeldItem(hand);
		boolean did = false;

		if(world.isRemote)
			return true;

		if(plateStack.isEmpty() && plate.acceptsItem(stack)) {
			plate.getItemHandler().setStackInSlot(0, stack.copy());
			stack.shrink(1);
			did = true;
		} else if(!plateStack.isEmpty() && !plate.burning) {
			if(!stack.isEmpty() && stack.getItem() == Items.FLINT_AND_STEEL) {
				plate.ignite();
				stack.damageItem(1, player, e -> e.sendBreakAnimation(hand));
				did = true;
			} else {
				ItemHandlerHelper.giveItemToPlayer(player, plateStack);
				plate.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);

				did = true;
			}
		}

		if(did)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);

		return did;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(BotaniaStateProps.CARDINALS, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return ((TileIncensePlate) world.getTileEntity(pos)).comparatorOutput;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		if(state.get(BotaniaStateProps.CARDINALS).getAxis() == Direction.Axis.X) {
			return X_SHAPE;
		} else {
			return Z_SHAPE;
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileIncensePlate();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
		return LexiconData.incense;
	}

}
