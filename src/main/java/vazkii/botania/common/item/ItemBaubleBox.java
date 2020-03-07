/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import vazkii.botania.client.gui.box.ContainerBaubleBox;
import vazkii.botania.common.core.handler.EquipmentHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBaubleBox extends Item {

	public ItemBaubleBox(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
		return new InvProvider();
	}

	private static class InvProvider implements ICapabilitySerializable<INBT> {

		private final IItemHandler inv = new ItemStackHandler(24) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack toInsert, boolean simulate) {
				if (!toInsert.isEmpty()) {
					if (EquipmentHandler.instance.isAccessory(toInsert)) {
						return super.insertItem(slot, toInsert, simulate);
					}
				}
				return toInsert;
			}
		};
		private final LazyOptional<IItemHandler> opt = LazyOptional.of(() -> inv);

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, opt);
		}

		@Override
		public INBT serializeNBT() {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null);
		}

		@Override
		public void deserializeNBT(INBT nbt) {
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt);
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			INamedContainerProvider container = new SimpleNamedContainerProvider((w, p, pl) -> new ContainerBaubleBox(w, p, stack), stack.getDisplayName());
			NetworkHooks.openGui((ServerPlayerEntity) player, container, b -> {
				b.writeBoolean(hand == Hand.MAIN_HAND);
			});
		}
		return ActionResult.success(player.getHeldItem(hand));
	}
}
