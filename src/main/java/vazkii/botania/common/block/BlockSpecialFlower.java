/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 22, 2014, 7:06:38 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.item.material.ItemDye;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class BlockSpecialFlower extends BlockFlower implements ISpecialFlower, IWandable, ILexiconable, IWandHUD {
	private static final VoxelShape SHAPE = makeCuboidShape(4.8, 0, 4.8, 12.8, 16, 12.8);
	private final Supplier<? extends TileEntitySpecialFlower> teProvider;

	protected BlockSpecialFlower(Properties props, Supplier<? extends TileEntitySpecialFlower> teProvider) {
		super(props);
		this.teProvider = teProvider;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
		Vec3d shift = state.getOffset(world, pos);
		return SHAPE.withOffset(shift.x, shift.y, shift.z);
	}

	@Override
	public int getLightValue(@Nonnull IBlockState state, IWorldReader world, @Nonnull BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != this)
			return world.getBlockState(pos).getLightValue(world, pos);

		return world.getTileEntity(pos) == null ? 0 : ((TileEntitySpecialFlower) world.getTileEntity(pos)).getLightValue();
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).getComparatorInputOverride();
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing side) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).getPowerLevel(side);
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing side) {
		return getWeakPower(state, world, pos, side);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	protected boolean isValidGround(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.getBlock() == ModBlocks.redStringRelay
				|| state.getBlock() == Blocks.MYCELIUM
				|| super.isValidGround(state, worldIn, pos);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		super.onBlockHarvested(world, pos, state, player);
		((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockHarvested(world, pos, state, player);
	}

	@Override
	public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest, IFluidState fluidState) {
		if (willHarvest) {
			// Copy of super.removedByPlayer but don't set to air yet
			// This is so getDrops below will have a TE to work with
			onBlockHarvested(world, pos, state, player);
			return true;
		} else {
			return super.removedByPlayer(state, world, pos, player, willHarvest, fluidState);
		}
	}

	@Override
	public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		// Now delete the block and TE
		world.removeBlock(pos);
	}

	@Override
	public void getDrops(@Nonnull IBlockState state, NonNullList<ItemStack> list, World world, BlockPos pos, int fortune) {
		super.getDrops(state, list, world, pos, fortune);
		TileEntity tile = world.getTileEntity(pos);

		if(tile instanceof TileEntitySpecialFlower) {
			((TileEntitySpecialFlower) tile).getDrops(list);
		}
	}

	@Override
	public boolean eventReceived(IBlockState state, World world, BlockPos pos, int par5, int par6) {
		super.eventReceived(state, world, pos, par5, par6);
		TileEntity tileentity = world.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(par5, par6);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return teProvider.get();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).getEntry();
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).onWanded(player, stack);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Override
	public void onBlockAdded(IBlockState state, World world, BlockPos pos, IBlockState oldState) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockAdded(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty() && stack.getItem() instanceof ItemDye) {
			EnumDyeColor newColor = ((ItemDye) stack.getItem()).color;
			EnumDyeColor oldColor = state.get(BotaniaStateProps.COLOR);
			if(newColor != oldColor)
				world.setBlockState(pos, state.with(BotaniaStateProps.COLOR, newColor), 1 | 2);
			return true;
		}

		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).renderHUD(mc);
	}
}
