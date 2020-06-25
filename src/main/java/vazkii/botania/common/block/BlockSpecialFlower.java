/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BlockSpecialFlower extends FlowerBlock implements ITileEntityProvider, IWandable, IWandHUD {
	private static final VoxelShape SHAPE = makeCuboidShape(4.8, 0, 4.8, 12.8, 16, 12.8);
	private final Supplier<? extends TileEntitySpecialFlower> teProvider;

	protected BlockSpecialFlower(Properties props, Supplier<? extends TileEntitySpecialFlower> teProvider) {
		super(Effects.SPEED, 4, props);
		this.teProvider = teProvider;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, ISelectionContext ctx) {
		Vec3d shift = state.getOffset(world, pos);
		return SHAPE.withOffset(shift.x, shift.y, shift.z);
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).getComparatorInputOverride();
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).getPowerLevel(side);
	}

	@Override
	public int getStrongPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return getWeakPower(state, world, pos, side);
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.getBlock() == ModBlocks.redStringRelay
				|| state.getBlock() == Blocks.MYCELIUM
				|| super.isValidGround(state, worldIn, pos);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBlockHarvested(world, pos, state, player);
		((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockHarvested(world, pos, state, player);
	}

	@Nonnull
	@Override
	public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootContext.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);
		TileEntity te = builder.get(LootParameters.BLOCK_ENTITY);
		if (te instanceof TileEntitySpecialFlower) {
			return ((TileEntitySpecialFlower) te).getDrops(drops, builder);
		} else {
			return drops;
		}
	}

	@Override
	public boolean eventReceived(BlockState state, World world, BlockPos pos, int event, int param) {
		super.eventReceived(state, world, pos, event, param);
		TileEntity tileentity = world.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(event, param);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return teProvider.get();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).onWanded(player, stack);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockAdded(world, pos, state);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockActivated(world, pos, state, player, hand, hit);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).renderHUD(mc);
	}
}
