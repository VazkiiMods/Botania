/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;
import java.util.function.Supplier;

public class BlockSpecialFlower extends FlowerBlock implements BlockEntityProvider, IWandable, IWandHUD {
	private static final VoxelShape SHAPE = createCuboidShape(4.8, 0, 4.8, 12.8, 16, 12.8);
	private final Supplier<? extends TileEntitySpecialFlower> teProvider;

	protected BlockSpecialFlower(StatusEffect stewEffect, int stewDuration, Settings props, Supplier<? extends TileEntitySpecialFlower> teProvider) {
		super(stewEffect, stewDuration, props);
		this.teProvider = teProvider;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, @Nonnull BlockView world, @Nonnull BlockPos pos, ShapeContext ctx) {
		Vec3d shift = state.getModelOffset(world, pos);
		return SHAPE.offset(shift.x, shift.y, shift.z);
	}

	@Override
	protected boolean canPlantOnTop(BlockState state, BlockView worldIn, BlockPos pos) {
		return state.getBlock() == ModBlocks.redStringRelay
				|| state.getBlock() == Blocks.MYCELIUM
				|| super.canPlantOnTop(state, worldIn, pos);
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int event, int param) {
		super.onSyncedBlockEvent(state, world, pos, event, param);
		BlockEntity tileentity = world.getBlockEntity(pos);
		return tileentity != null && tileentity.onSyncedBlockEvent(event, param);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return teProvider.get();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		return ((TileEntitySpecialFlower) world.getBlockEntity(pos)).onWanded(player, stack);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileEntitySpecialFlower) world.getBlockEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		((TileEntitySpecialFlower) world.getBlockEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		redstoneParticlesIfPowered(state, world, pos, rand);
	}

	public static void redstoneParticlesIfPowered(BlockState state, World world, BlockPos pos, Random rand) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TileEntityFunctionalFlower && rand.nextBoolean()) {
			TileEntityFunctionalFlower flower = (TileEntityFunctionalFlower) te;
			if (flower.acceptsRedstone() && flower.redstoneSignal > 0) {
				VoxelShape shape = state.getOutlineShape(world, pos);
				if (!shape.isEmpty()) {
					Box localBox = shape.getBoundingBox();
					BlockPos effPos = flower.getEffectivePos();
					double x = effPos.getX() + localBox.minX + rand.nextDouble() * (localBox.maxX - localBox.minX);
					double y = effPos.getY() + localBox.minY + rand.nextDouble() * (localBox.maxY - localBox.minY);
					double z = effPos.getZ() + localBox.minZ + rand.nextDouble() * (localBox.maxZ - localBox.minZ);
					world.addParticle(DustParticleEffect.RED, x, y, z, 0, 0, 0);
				}
			}
		}
	}
}
