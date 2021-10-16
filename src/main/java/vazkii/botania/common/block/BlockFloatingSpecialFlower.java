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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.block.decor.BlockFloatingFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;
import java.util.function.Supplier;

public class BlockFloatingSpecialFlower extends BlockFloatingFlower implements IWandable, IWandHUD {
	private final Supplier<BlockEntityType<? extends TileEntitySpecialFlower>> blockEntityType;

	public BlockFloatingSpecialFlower(Properties props, Supplier<BlockEntityType<? extends TileEntitySpecialFlower>> blockEntityType) {
		super(DyeColor.WHITE, props);
		this.blockEntityType = blockEntityType;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		BlockSpecialFlower.redstoneParticlesIfPowered(state, world, pos, rand);
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

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		TileEntitySpecialFlower te = blockEntityType.get().create(pos, state);
		te.setFloating(true);
		return te;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
		return createTickerHelper(type, blockEntityType.get(), TileEntitySpecialFlower::commonTick);
	}
}
