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

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
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
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return REAL_SHAPE;
	}

	// If harvesting, delay setting block to air so getDrops can read the TE
	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {
		if (willHarvest)
			return true;
		return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
	}

	@Override
	public void getDrops(BlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TilePool && !((TilePool) te).fragile) {
			super.getDrops(state, drops, world, pos, fortune);
		}
	}

	// After getDrops reads the TE, then delete the block
	@Override
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.removeBlock(pos);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TilePool();
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if(entity instanceof ItemEntity) {
			TilePool tile = (TilePool) world.getTileEntity(pos);
			if(tile.collideEntityItem((ItemEntity) entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
		}
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		if (variant == Variant.FABULOUS)
			return BlockRenderType.ENTITYBLOCK_ANIMATED;
		else return BlockRenderType.MODEL;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		TilePool pool = (TilePool) world.getTileEntity(pos);
		return TilePool.calculateComparatorLevel(pool.getCurrentMana(), pool.manaCap);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TilePool) world.getTileEntity(pos)).renderHUD(mc);
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TilePool) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
		return variant == Variant.FABULOUS ? LexiconData.rainbowRod : LexiconData.pool;
	}
}
