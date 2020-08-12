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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.client.gui.bag.ContainerFlowerBag;
import vazkii.botania.common.block.BlockModFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFlowerBag extends Item {
	public static final int SIZE = 16;

	public ItemFlowerBag(Settings props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onPickupItem);
	}

	public static boolean isValid(int slot, ItemStack stack) {
		Block blk = Block.getBlockFromItem(stack.getItem());
		return !stack.isEmpty()
				&& blk.getClass() == BlockModFlower.class
				&& slot == ((BlockModFlower) blk).color.getId();
	}

	public static SimpleInventory getInventory(ItemStack stack) {
		return new ItemBackedInventory(stack, SIZE) {
			@Override
			public boolean isValid(int slot, @Nonnull ItemStack stack) {
				return isValid(slot, stack);
			}
		};
	}

	private void onPickupItem(EntityItemPickupEvent event) {
		ItemStack entityStack = event.getItem().getStack();
		if (Block.getBlockFromItem(entityStack.getItem()) instanceof BlockModFlower && entityStack.getCount() > 0) {
			int color = ((BlockModFlower) Block.getBlockFromItem(entityStack.getItem())).color.getId();

			for (int i = 0; i < event.getPlayer().inventory.size(); i++) {
				if (i == event.getPlayer().inventory.selectedSlot) {
					continue; // prevent item deletion
				}

				ItemStack bag = event.getPlayer().inventory.getStack(i);
				if (!bag.isEmpty() && bag.getItem() == this) {
					SimpleInventory bagInv = getInventory(bag);
					ItemStack existing = bagInv.getStack(color);
					int newCount = Math.min(existing.getCount() + entityStack.getCount(),
							Math.min(existing.getMaxCount(), bagInv.getMaxCountPerStack()));
					int numPickedUp = newCount - existing.getCount();

					if (numPickedUp > 0) {
						if (existing.isEmpty()) {
							bagInv.setStack(color, entityStack.split(numPickedUp));
						} else {
							existing.increment(numPickedUp);
							entityStack.decrement(numPickedUp);
						}
						event.getItem().setStack(entityStack);
						bagInv.markDirty();

						event.setCanceled(true);
						if (!event.getItem().isSilent()) {
							event.getItem().world.playSound(null, event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getZ(),
									SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
									((event.getItem().world.random.nextFloat() - event.getItem().world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
						}
						((ServerPlayerEntity) event.getPlayer()).networkHandler.sendPacket(new ItemPickupAnimationS2CPacket(event.getItem().getEntityId(), event.getPlayer().getEntityId(), numPickedUp));
						event.getPlayer().currentScreenHandler.sendContentUpdates();

						return;
					}
				}
			}
		}
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		if (!world.isClient) {
			ItemStack stack = player.getStackInHand(hand);
			NamedScreenHandlerFactory container = new SimpleNamedScreenHandlerFactory((w, p, pl) -> new ContainerFlowerBag(w, p, stack), stack.getName());
			NetworkHooks.openGui((ServerPlayerEntity) player, container, buf -> {
				buf.writeBoolean(hand == Hand.MAIN_HAND);
			});
		}
		return TypedActionResult.success(player.getStackInHand(hand));
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		Direction side = ctx.getSide();

		BlockEntity tile = world.getBlockEntity(pos);
		if (tile != null) {
			if (!world.isClient) {
				IItemHandler tileInv;
				if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).isPresent()) {
					tileInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).orElseThrow(NullPointerException::new);
				} else if (tile instanceof Inventory) {
					tileInv = new InvWrapper((Inventory) tile);
				} else {
					return ActionResult.FAIL;
				}

				Inventory bagInv = getInventory(ctx.getStack());
				for (int i = 0; i < bagInv.size(); i++) {
					ItemStack flower = bagInv.getStack(i);
					ItemStack rem = ItemHandlerHelper.insertItemStacked(tileInv, flower, false);
					bagInv.setStack(i, rem);
				}

			}

			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}
}
