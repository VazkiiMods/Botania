/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.common.block.tile.TileMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileRedString extends TileMod implements ITileBound, Tickable {

	private BlockPos binding;

	public TileRedString(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public void tick() {
		Direction dir = getOrientation();
		BlockPos pos_ = getPos();
		int range = getRange();
		BlockPos currBinding = getBinding();
		setBinding(null);

		for (int i = 0; i < range; i++) {
			pos_ = pos_.offset(dir);
			if (world.isAir(pos_)) {
				continue;
			}

			BlockEntity tile = world.getBlockEntity(pos_);
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

	/* todo 1.16-fabric
	@Nonnull
	@Override
	public Box getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
	*/

	@Nullable
	@Override
	public BlockPos getBinding() {
		return binding;
	}

	public void setBinding(BlockPos binding) {
		this.binding = binding;
	}

	public Direction getOrientation() {
		return getCachedState().get(Properties.FACING);
	}

	public BlockEntity getTileAtBinding() {
		BlockPos binding = getBinding();
		return binding == null || world == null ? null : world.getBlockEntity(binding);
	}

	public BlockState getStateAtBinding() {
		BlockPos binding = getBinding();
		return binding == null ? Blocks.AIR.getDefaultState() : world.getBlockState(binding);
	}

	public Block getBlockAtBinding() {
		return getStateAtBinding().getBlock();
	}
}
