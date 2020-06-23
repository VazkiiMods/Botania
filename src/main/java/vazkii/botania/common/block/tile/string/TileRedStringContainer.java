/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Arrays;

public class TileRedStringContainer extends TileRedString {
	private static final LazyOptional<IItemHandler> EMPTY = LazyOptional.of(EmptyHandler::new);
	@Nullable private LazyOptional<?> lastBoundInv = null;
	@Nullable private LazyOptional<?> proxiedInv = null;

	public TileRedStringContainer() {
		this(ModTiles.RED_STRING_CONTAINER);
	}

	public TileRedStringContainer(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return tile != null
				&& Arrays.stream(Direction.values())
						.anyMatch(e -> tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, e).isPresent());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (getTileAtBinding() != null) {
				LazyOptional<?> optional = getTileAtBinding().getCapability(cap, side);
				if (!optional.isPresent()) {
					invalidateLastCap();
					return EMPTY.cast();
				}
				if (lastBoundInv == optional) {
					return proxiedInv.cast();
				}
				if (proxiedInv != null) {
					proxiedInv.invalidate();
				}
				lastBoundInv = optional;
				proxiedInv = createProxyOptional(optional.cast());
				return proxiedInv.cast();

			} else {
				invalidateLastCap();
				return EMPTY.cast();
			}
		}
		return super.getCapability(cap, side);
	}

	private void invalidateLastCap() {
		if (proxiedInv != null) {
			proxiedInv.invalidate();
			proxiedInv = null;
		}
		lastBoundInv = null;
	}

	private LazyOptional<IItemHandler> createProxyOptional(LazyOptional<IItemHandler> original) {
		LazyOptional<IItemHandler> proxy = LazyOptional.of(() -> original.orElse(EmptyHandler.INSTANCE));
		original.addListener(orig -> proxy.invalidate());
		return proxy;
	}

	@Override
	public void remove() {
		super.remove();
		invalidateLastCap();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		TileEntity tile = getTileAtBinding();
		if (tile != null) {
			tile.markDirty();
		}
	}

}
