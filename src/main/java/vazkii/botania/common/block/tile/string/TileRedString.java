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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.block.ITileBound;
import vazkii.botania.common.block.tile.TileMod;

import javax.annotation.Nullable;

public abstract class TileRedString extends TileMod implements ITileBound {

	private BlockPos binding;

	protected TileRedString(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static void commonTick(Level level, BlockPos pos_, BlockState state, TileRedString self) {
		Direction dir = self.getOrientation();
		int range = self.getRange();
		BlockPos currBinding = self.getBinding();
		self.setBinding(null);

		for (int i = 0; i < range; i++) {
			pos_ = pos_.relative(dir);
			if (level.isEmptyBlock(pos_)) {
				continue;
			}

			BlockEntity tile = level.getBlockEntity(pos_);
			if (tile instanceof TileRedString) {
				continue;
			}

			if (self.acceptBlock(pos_)) {
				self.setBinding(pos_);
				if (currBinding == null || !currBinding.equals(pos_)) {
					self.onBound(pos_);
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
