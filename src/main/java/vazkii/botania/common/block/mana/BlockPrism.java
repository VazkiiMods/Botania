/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 17, 2015, 7:16:48 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockPrism extends BlockMod implements IManaTrigger, ILexiconable, IManaCollisionGhost {
	private static final VoxelShape SHAPE = makeCuboidShape(4, 0, 4, 12, 16, 12);

	public BlockPrism(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState()
				.with(BotaniaStateProps.POWERED, false)
				.with(BotaniaStateProps.HAS_LENS, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(BotaniaStateProps.POWERED, BotaniaStateProps.HAS_LENS);
	}

	@Nonnull
	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float par7, float par8, float par9) {
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TilePrism))
			return false;

		TilePrism prism = (TilePrism) tile;
		ItemStack lens = prism.getItemHandler().getStackInSlot(0);
		ItemStack heldItem = player.getHeldItem(hand);
		boolean isHeldItemLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;

		if(lens.isEmpty() && isHeldItemLens) {
			if(!player.abilities.isCreativeMode)
				player.setHeldItem(hand, ItemStack.EMPTY);

			prism.getItemHandler().setStackInSlot(0, heldItem.copy());
			prism.markDirty();
			world.setBlockState(pos, state.with(BotaniaStateProps.HAS_LENS, true), 1 | 2);
		} else if(!lens.isEmpty()) {
			ItemHandlerHelper.giveItemToPlayer(player, lens);
			prism.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
			prism.markDirty();
			world.setBlockState(pos, state.with(BotaniaStateProps.HAS_LENS, false), 1 | 2);
		}

		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BotaniaStateProps.POWERED);

		if(!world.isRemote) {
			if(power && !powered)
				world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, true), 1 | 2);
			else if(!power && powered)
				world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, false), 1 | 2);
		}
	}

	@Override
	public void onReplaced(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState newState, boolean isMoving) {
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TilePrism();
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TilePrism)
			((TilePrism) tile).onBurstCollision(burst);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.prism;
	}

	@Override
	public boolean isGhost(IBlockState state, World world, BlockPos pos) {
		return true;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return side.getAxis() == EnumFacing.Axis.Y ? BlockFaceShape.CENTER_BIG : BlockFaceShape.MIDDLE_POLE_THICK;
	}
}
