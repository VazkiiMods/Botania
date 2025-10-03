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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

import java.util.function.Supplier;

public class FloatingSpecialFlowerBlock extends FloatingFlowerBaseBlock {
	private final Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType;
	private final boolean hasComparatorOutput;

	public FloatingSpecialFlowerBlock(Properties props,
			Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType) {
		this(props, blockEntityType, false);
	}

	public FloatingSpecialFlowerBlock(Properties props,
			Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType,
			boolean hasComparatorOutput) {
		super(props);
		this.blockEntityType = blockEntityType;
		this.hasComparatorOutput = hasComparatorOutput;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if (world.getBlockEntity(pos) instanceof SpecialFlowerBlockEntity flower) {
			flower.setPlacedBy(world, pos, state, entity, stack);
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (hasComparatorOutput && !newState.hasAnalogOutputSignal()) {
			level.updateNeighbourForOutputSignal(pos, newState.getBlock());
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		SpecialFlowerBlockEntity te = blockEntityType.get().create(pos, state);
		if (te == null) {
			throw new IllegalStateException("Not a special flower block entity");
		}
		te.setFloating(true);
		return te;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
		return createTickerHelper(type, blockEntityType.get(), SpecialFlowerBlockEntity::commonTick);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState bs) {
		return hasComparatorOutput;
	}

	@Override
	public int getAnalogOutputSignal(BlockState bs, Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof SpecialFlowerBlockEntity flower ? flower.getComparatorSignal() : 0;
	}
}
