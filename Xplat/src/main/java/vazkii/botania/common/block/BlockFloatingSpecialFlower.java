/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.block.decor.BlockFloatingFlower;

import java.util.function.Supplier;

public class BlockFloatingSpecialFlower extends BlockFloatingFlower {
	private final Supplier<BlockEntityType<? extends TileEntitySpecialFlower>> blockEntityType;

	public BlockFloatingSpecialFlower(Properties props, Supplier<BlockEntityType<? extends TileEntitySpecialFlower>> blockEntityType) {
		super(DyeColor.WHITE, props);
		this.blockEntityType = blockEntityType;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		BlockSpecialFlower.redstoneParticlesIfPowered(state, world, pos, rand);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileEntitySpecialFlower) world.getBlockEntity(pos)).setPlacedBy(world, pos, state, entity, stack);
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
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
