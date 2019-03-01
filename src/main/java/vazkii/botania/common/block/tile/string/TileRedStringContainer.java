/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 14, 2014, 5:26:39 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;
import net.minecraftforge.items.wrapper.EmptyHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class TileRedStringContainer extends TileRedString {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.RED_STRING_CONTAINER)
	public static TileEntityType<TileRedStringContainer> TYPE;
	private static final LazyOptional<IItemHandler> EMPTY = LazyOptional.of(EmptyHandler::new);

	public TileRedStringContainer() {
		this(TYPE);
	}

	public TileRedStringContainer(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return tile != null
				&& Arrays.stream(EnumFacing.values())
				.anyMatch(e -> tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, e).isPresent());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		// todo 1.13 need to invalidate when binding breaks
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(getTileAtBinding() != null) {
				return getTileAtBinding().getCapability(cap, side);
			}
			return EMPTY.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		TileEntity tile = getTileAtBinding();
		if(tile != null)
			tile.markDirty();
	}

}
