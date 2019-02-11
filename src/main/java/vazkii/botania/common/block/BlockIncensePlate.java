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
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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

	protected BlockIncensePlate(Builder builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.CARDINALS, EnumFacing.SOUTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(BotaniaStateProps.CARDINALS);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs) {
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
				stack.damageItem(1, player);
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
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(BotaniaStateProps.CARDINALS, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TileIncensePlate) world.getTileEntity(pos)).comparatorOutput;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		if(state.get(BotaniaStateProps.CARDINALS).getAxis() == EnumFacing.Axis.X) {
			return X_SHAPE;
		} else {
			return Z_SHAPE;
		}
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileIncensePlate();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.incense;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

}
