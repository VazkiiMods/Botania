/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;
import java.util.function.Supplier;

public class BlockSpecialFlower extends FlowerBlock implements EntityBlock, IWandable, IWandHUD {
	private static final VoxelShape SHAPE = box(4.8, 0, 4.8, 12.8, 16, 12.8);
	private final Supplier<BlockEntityType<? extends TileEntitySpecialFlower>> blockEntityType;

	protected BlockSpecialFlower(MobEffect stewEffect, int stewDuration, Properties props, Supplier<BlockEntityType<? extends TileEntitySpecialFlower>> blockEntityType) {
		super(stewEffect, stewDuration, props);
		this.blockEntityType = blockEntityType;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, CollisionContext ctx) {
		Vec3 shift = state.getOffset(world, pos);
		return SHAPE.move(shift.x, shift.y, shift.z);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return state.getBlock() == ModBlocks.redStringRelay
				|| state.getBlock() == Blocks.MYCELIUM
				|| super.mayPlaceOn(state, worldIn, pos);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int event, int param) {
		super.triggerEvent(state, world, pos, event, param);
		BlockEntity tileentity = world.getBlockEntity(pos);
		return tileentity != null && tileentity.triggerEvent(event, param);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return blockEntityType.get().create(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return BlockMod.createTickerHelper(type, blockEntityType.get(), TileEntitySpecialFlower::commonTick);
	}

	@Override
	public boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos, Direction side) {
		return ((TileEntitySpecialFlower) world.getBlockEntity(pos)).onWanded(player, stack);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileEntitySpecialFlower) world.getBlockEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc, Level world, BlockPos pos) {
		((TileEntitySpecialFlower) world.getBlockEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		redstoneParticlesIfPowered(state, world, pos, rand);
	}

	public static void redstoneParticlesIfPowered(BlockState state, Level world, BlockPos pos, Random rand) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TileEntityFunctionalFlower && rand.nextBoolean()) {
			TileEntityFunctionalFlower flower = (TileEntityFunctionalFlower) te;
			if (flower.acceptsRedstone() && flower.redstoneSignal > 0) {
				VoxelShape shape = state.getShape(world, pos);
				if (!shape.isEmpty()) {
					AABB localBox = shape.bounds();
					BlockPos effPos = flower.getEffectivePos();
					double x = effPos.getX() + localBox.minX + rand.nextDouble() * (localBox.maxX - localBox.minX);
					double y = effPos.getY() + localBox.minY + rand.nextDouble() * (localBox.maxY - localBox.minY);
					double z = effPos.getZ() + localBox.minZ + rand.nextDouble() * (localBox.maxZ - localBox.minZ);
					world.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0, 0, 0);
				}
			}
		}
	}
}
