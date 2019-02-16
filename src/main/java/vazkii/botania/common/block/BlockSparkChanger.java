/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 28, 2015, 10:01:01 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.TileSparkChanger;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ItemSparkUpgrade;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockSparkChanger extends BlockMod implements ILexiconable {

	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 3, 16);

	public BlockSparkChanger(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.POWERED, true));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(BotaniaStateProps.POWERED);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BotaniaStateProps.POWERED);

		if(power && !powered) {
			((TileSparkChanger) world.getTileEntity(pos)).doSwap();
			world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, true), 4);
		} else if(!power && powered)
			world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, false), 4);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs) {
		TileSparkChanger changer = (TileSparkChanger) world.getTileEntity(pos);
		ItemStack pstack = player.getHeldItem(hand);
		ItemStack cstack = changer.getItemHandler().getStackInSlot(0);
		if(!cstack.isEmpty()) {
			changer.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
			world.updateComparatorOutputLevel(pos, this);
			changer.markDirty();
			ItemHandlerHelper.giveItemToPlayer(player, cstack);
			return true;
		} else if(!pstack.isEmpty() && pstack.getItem() instanceof ItemSparkUpgrade) {
			changer.getItemHandler().setStackInSlot(0, pstack.split(1));
			world.updateComparatorOutputLevel(pos, this);
			changer.markDirty();

			return true;
		}

		return false;
	}

	@Override
	public void onReplaced(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState newState, boolean isMoving) {
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileSparkChanger changer = (TileSparkChanger) world.getTileEntity(pos);
		ItemStack stack = changer.getItemHandler().getStackInSlot(0);
		if(stack.isEmpty())
			return 0;
		return stack.getItemDamage() + 1;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileSparkChanger();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.sparkChanger;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

}
