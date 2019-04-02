/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 26, 2014, 12:22:58 AM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPool extends BlockMod implements IWandHUD, IWandable, ILexiconable {
	private static final VoxelShape SLAB = makeCuboidShape(0, 0, 0, 16, 8, 16);
	private static final VoxelShape CUTOUT = makeCuboidShape(1, 1, 1, 15, 8, 15);
	private static final VoxelShape REAL_SHAPE = VoxelShapes.combineAndSimplify(SLAB, CUTOUT, IBooleanFunction.ONLY_FIRST);

	public enum Variant {
		DEFAULT,
		CREATIVE,
		DILUTED,
		FABULOUS
	}

	public final Variant variant;

	public BlockPool(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return REAL_SHAPE;
	}

	// If harvesting, delay setting block to air so getDrops can read the TE
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, IFluidState fluid) {
		if (willHarvest)
			return true;
		return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
	}

	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TilePool && !((TilePool) te).fragile) {
			super.getDrops(state, drops, world, pos, fortune);
		}
	}

	// After getDrops reads the TE, then delete the block
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.removeBlock(pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TilePool();
	}

	@Override
	public void onEntityCollision(IBlockState state, World world, BlockPos pos, Entity entity) {
		if(entity instanceof EntityItem) {
			TilePool tile = (TilePool) world.getTileEntity(pos);
			if(tile.collideEntityItem((EntityItem) entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
		}
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		if (variant == Variant.FABULOUS)
			return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
		else return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TilePool pool = (TilePool) world.getTileEntity(pos);
		return TilePool.calculateComparatorLevel(pool.getCurrentMana(), pool.manaCap);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TilePool) world.getTileEntity(pos)).renderHUD(mc);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		((TilePool) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return variant == Variant.FABULOUS ? LexiconData.rainbowRod : LexiconData.pool;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
}
