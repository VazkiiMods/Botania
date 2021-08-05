/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.common.block.tile.TileMod;

import javax.annotation.Nullable;

public abstract class TileRedString extends TileMod implements ITileBound, TickableBlockEntity {

	private BlockPos binding;

	public TileRedString(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		Direction dir = getOrientation();
		BlockPos pos_ = getBlockPos();
		int range = getRange();
		BlockPos currBinding = getBinding();
		setBinding(null);

		for (int i = 0; i < range; i++) {
			pos_ = pos_.relative(dir);
			if (level.isEmptyBlock(pos_)) {
				continue;
			}

			BlockEntity tile = level.getBlockEntity(pos_);
			if (tile instanceof TileRedString) {
				continue;
			}

			if (acceptBlock(pos_)) {
				setBinding(pos_);
				if (currBinding == null || !currBinding.equals(pos_)) {
					onBound(pos_);
				}
				break;
			}
		}
	}

	public int getRange() {
		return 8;
	}

	public abstract boolean acceptBlock(BlockPos pos);

	public void onBound(BlockPos pos) {}

	@Nullable
	@Override
	public BlockPos getBinding() {
		return binding;
	}

	public void setBinding(BlockPos binding) {
		this.binding = binding;
	}

	public Direction getOrientation() {
		return getBlockState().getValue(BlockStateProperties.FACING);
	}

	public BlockEntity getTileAtBinding() {
		BlockPos binding = getBinding();
		return binding == null || level == null ? null : level.getBlockEntity(binding);
	}

	public BlockState getStateAtBinding() {
		BlockPos binding = getBinding();
		return binding == null ? Blocks.AIR.defaultBlockState() : level.getBlockState(binding);
	}

	public Block getBlockAtBinding() {
		return getStateAtBinding().getBlock();
	}
}
