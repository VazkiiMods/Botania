/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import com.google.common.base.Preconditions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileSimpleInventory extends TileMod {

	private final SimpleContainer itemHandler = createItemHandler();

	protected TileSimpleInventory(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemHandler.addListener(i -> setChanged());
	}

	private static void copyToInv(NonNullList<ItemStack> src, Container dest) {
		Preconditions.checkArgument(src.size() == dest.getContainerSize());
		for (int i = 0; i < src.size(); i++) {
			dest.setItem(i, src.get(i));
		}
	}

	private static NonNullList<ItemStack> copyFromInv(Container inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ret.set(i, inv.getItem(i));
		}
		return ret;
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		NonNullList<ItemStack> tmp = NonNullList.withSize(inventorySize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, tmp);
		copyToInv(tmp, itemHandler);
	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		ContainerHelper.saveAllItems(tag, copyFromInv(itemHandler));
	}

	// NB: Cannot be named the same as the corresponding method in vanilla's interface -- causes obf issues with MCP
	public final int inventorySize() {
		return getItemHandler().getContainerSize();
	}

	protected abstract SimpleContainer createItemHandler();

	public final Container getItemHandler() {
		return itemHandler;
	}
}
