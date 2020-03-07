/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.play.server.SCollectItemPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import vazkii.botania.client.gui.bag.ContainerFlowerBag;
import vazkii.botania.common.block.BlockModFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFlowerBag extends Item {
	public ItemFlowerBag(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onPickupItem);
	}

	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
		return new InvProvider();
	}

	private static class InvProvider implements ICapabilitySerializable<INBT> {

		private final IItemHandler inv = new ItemStackHandler(16) {
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				Block blk = Block.getBlockFromItem(stack.getItem());
				return !stack.isEmpty()
						&& blk.getClass() == BlockModFlower.class
						&& slot == ((BlockModFlower) blk).color.getId();
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

	private void onPickupItem(EntityItemPickupEvent event) {
		ItemStack entityStack = event.getItem().getItem();
		if (Block.getBlockFromItem(entityStack.getItem()) instanceof BlockModFlower && entityStack.getCount() > 0) {
			int color = ((BlockModFlower) Block.getBlockFromItem(entityStack.getItem())).color.getId();

			for (int i = 0; i < event.getPlayer().inventory.getSizeInventory(); i++) {
				if (i == event.getPlayer().inventory.currentItem) {
					continue; // prevent item deletion
				}

				ItemStack bag = event.getPlayer().inventory.getStackInSlot(i);
				if (!bag.isEmpty() && bag.getItem() == this) {
					IItemHandler bagInv = bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);

					ItemStack result = bagInv.insertItem(color, entityStack, false);
					int numPickedUp = entityStack.getCount() - result.getCount();

					event.getItem().setItem(result);

					if (numPickedUp > 0) {
						event.setCanceled(true);
						if (!event.getItem().isSilent()) {
							event.getItem().world.playSound(null, event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getZ(),
									SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
									((event.getItem().world.rand.nextFloat() - event.getItem().world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
						}
						((ServerPlayerEntity) event.getPlayer()).connection.sendPacket(new SCollectItemPacket(event.getItem().getEntityId(), event.getPlayer().getEntityId(), numPickedUp));
						event.getPlayer().openContainer.detectAndSendChanges();

						return;
					}
				}
			}
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			INamedContainerProvider container = new SimpleNamedContainerProvider((w, p, pl) -> new ContainerFlowerBag(w, p, stack), stack.getDisplayName());
			NetworkHooks.openGui((ServerPlayerEntity) player, container, buf -> {
				buf.writeBoolean(hand == Hand.MAIN_HAND);
			});
		}
		return ActionResult.success(player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		Direction side = ctx.getFace();

		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (!world.isRemote) {
				IItemHandler tileInv;
				if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).isPresent()) {
					tileInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).orElseThrow(NullPointerException::new);
				} else if (tile instanceof IInventory) {
					tileInv = new InvWrapper((IInventory) tile);
				} else {
					return ActionResultType.FAIL;
				}

				ctx.getItem().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(bagInv -> {
					for (int i = 0; i < bagInv.getSlots(); i++) {
						ItemStack flower = bagInv.getStackInSlot(i);
						((IItemHandlerModifiable) bagInv).setStackInSlot(i, ItemHandlerHelper.insertItemStacked(tileInv, flower, false));
					}
				});

			}

			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
