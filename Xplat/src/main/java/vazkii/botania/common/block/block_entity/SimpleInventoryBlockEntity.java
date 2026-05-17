/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import com.google.common.base.Preconditions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public abstract class SimpleInventoryBlockEntity extends BlockEntity implements Clearable {

	private final boolean synchronize;
	private final SimpleContainer itemHandler = createItemHandler();

	protected SimpleInventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean synchronize) {
		super(type, pos, state);
		this.synchronize = synchronize;
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
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		NonNullList<ItemStack> tmp = NonNullList.withSize(inventorySize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, tmp, registries);
		copyToInv(tmp, itemHandler);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		ContainerHelper.saveAllItems(tag, copyFromInv(itemHandler), registries);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		if (synchronize) {
			ContainerHelper.saveAllItems(tag, copyFromInv(itemHandler), registries);
		}
		return tag;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return synchronize ? ClientboundBlockEntityDataPacket.create(this) : null;
	}

	// NB: Cannot be named the same as the corresponding method in vanilla's interface -- causes obf issues with MCP
	public final int inventorySize() {
		return getItemHandler().getContainerSize();
	}

	protected abstract SimpleContainer createItemHandler();

	@Override
	public void clearContent() {
		getItemHandler().clearContent();
	}

	public final Container getItemHandler() {
		return itemHandler;
	}

	public RecipeInput getRecipeInput() {
		return new RecipeInput() {
			@Override
			public ItemStack getItem(int index) {
				return itemHandler.getItem(index);
			}

			@Override
			public int size() {
				return itemHandler.getContainerSize();
			}
		};
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(itemHandler.getItems()));
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput componentInput) {
		componentInput.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(itemHandler.getItems());
	}
}
