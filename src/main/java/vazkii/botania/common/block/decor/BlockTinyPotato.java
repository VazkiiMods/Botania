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
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Particles;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockTinyPotato extends BlockMod implements ILexiconable {

	private static final VoxelShape SHAPE = makeCuboidShape(6, 0, 6, 10, 6, 10);

	public BlockTinyPotato(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState()
				.with(BotaniaStateProps.CARDINALS, EnumFacing.SOUTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(BotaniaStateProps.CARDINALS);
	}

	@Override
	public void onReplaced(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState newState, boolean isMoving) {
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing par6, float par7, float par8, float par9) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileTinyPotato) {
			((TileTinyPotato) tile).interact(player, hand, player.getHeldItem(hand), par6);
			AxisAlignedBB box = SHAPE.getBoundingBox();
			world.spawnParticle(Particles.HEART, pos.getX() + box.minX + Math.random() * (box.maxX - box.minX), pos.getY() + box.maxY, pos.getZ() + box.minZ + Math.random() * (box.maxZ - box.minZ), 0, 0 ,0);
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
		// todo 1.13 move to getstateforplacement
		world.setBlockState(pos, state.with(BotaniaStateProps.CARDINALS, par5EntityLiving.getHorizontalFacing().getOpposite()));
		if (par6ItemStack.hasDisplayName())
			((TileTinyPotato) world.getTileEntity(pos)).name = par6ItemStack.getDisplayName();
	}

	@Override
	public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest, IFluidState fluid) {
		if (willHarvest) {
			// Copy of super.removedByPlayer but don't remove block yet
			// This is so getDrops below will have a TE to work with
			onBlockHarvested(world, pos, state, player);
			return true;
		} else {
			return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		}
	}

	@Override
	public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		// Now delete the block and TE
		world.removeBlock(pos);
	}

	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> list, World world, BlockPos pos, int fortune) {
		TileEntity tile = world.getTileEntity(pos);

		if(tile != null) {
			ItemStack stack = new ItemStack(this);
			ITextComponent name = ((TileTinyPotato) tile).name;
			if(!name.getString().isEmpty())
				stack.setDisplayName(name);
			list.add(stack);
		}
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileTinyPotato();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.tinyPotato;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}
}
