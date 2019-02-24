/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 16, 2015, 6:43:33 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFlowerBag extends ItemMod {
	public ItemFlowerBag(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onPickupItem);
	}

	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
		return new InvProvider();
	}

	private static class InvProvider implements ICapabilitySerializable<INBTBase> {

		private final IItemHandler inv = new ItemStackHandler(16) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack toInsert, boolean simulate) {
				Block blk = Block.getBlockFromItem(toInsert.getItem());
				if(!toInsert.isEmpty()
						&& blk instanceof BlockModFlower
						&& slot == ((BlockModFlower) blk).color.getId())
					return super.insertItem(slot, toInsert, simulate);
				else return toInsert;
			}
		};
		private final LazyOptional<IItemHandler> opt = LazyOptional.of(() -> inv);

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, opt);
		}

		@Override
		public INBTBase serializeNBT() {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null);
		}

		@Override
		public void deserializeNBT(INBTBase nbt) {
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt);
		}
	}

	private void onPickupItem(EntityItemPickupEvent event) {
		ItemStack entityStack = event.getItem().getItem();
		if(Block.getBlockFromItem(entityStack.getItem()) instanceof BlockModFlower && entityStack.getCount() > 0) {
			int color = ((BlockModFlower) Block.getBlockFromItem(entityStack.getItem())).color.getId();

			for(int i = 0; i < event.getEntityPlayer().inventory.getSizeInventory(); i++) {
				if(i == event.getEntityPlayer().inventory.currentItem)
					continue; // prevent item deletion

				ItemStack bag = event.getEntityPlayer().inventory.getStackInSlot(i);
				if(!bag.isEmpty() && bag.getItem() == this) {
					IItemHandler bagInv = bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);

					ItemStack result = bagInv.insertItem(color, entityStack, false);
					int numPickedUp = entityStack.getCount() - result.getCount();

					event.getItem().setItem(result);

					if(numPickedUp > 0) {
						event.setCanceled(true);
						if (!event.getItem().isSilent()) {
							event.getItem().world.playSound(null, event.getEntityPlayer().posX, event.getEntityPlayer().posY, event.getEntityPlayer().posZ,
									SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
									((event.getItem().world.rand.nextFloat() - event.getItem().world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
						}
						((EntityPlayerMP) event.getEntityPlayer()).connection.sendPacket(new SPacketCollectItem(event.getItem().getEntityId(), event.getEntityPlayer().getEntityId(), numPickedUp));
						event.getEntityPlayer().openContainer.detectAndSendChanges();

						return;
					}
				}
			}
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.openGui(Botania.instance, LibGuiIDs.FLOWER_BAG, world, hand == EnumHand.OFF_HAND ? 1 : 0, 0, 0);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		EnumFacing side = ctx.getFace();

		TileEntity tile = world.getTileEntity(pos);
		if(tile != null) {
			if(!world.isRemote) {
				IItemHandler tileInv;
				if(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).isPresent())
					tileInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).orElseThrow(NullPointerException::new);
				else if(tile instanceof IInventory)
					tileInv = new InvWrapper((IInventory) tile);
				else return EnumActionResult.FAIL;

				ctx.getItem().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(bagInv -> {
					for(int i = 0; i < bagInv.getSlots(); i++) {
						ItemStack flower = bagInv.getStackInSlot(i);
						((IItemHandlerModifiable) bagInv).setStackInSlot(i, ItemHandlerHelper.insertItemStacked(tileInv, flower, false));
					}
				});

			}

			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

}
