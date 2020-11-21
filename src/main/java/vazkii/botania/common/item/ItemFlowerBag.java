/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
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

	public static boolean onPickupItem(ItemEntity entity, PlayerEntity player) {
		ItemStack entityStack = entity.getStack();
		if (Block.getBlockFromItem(entityStack.getItem()) instanceof BlockModFlower && entityStack.getCount() > 0) {
			int color = ((BlockModFlower) Block.getBlockFromItem(entityStack.getItem())).color.getId();

			for (int i = 0; i < player.inventory.size(); i++) {
				if (i == player.inventory.selectedSlot) {
					continue; // prevent item deletion
				}

				ItemStack bag = player.inventory.getStack(i);
				if (!bag.isEmpty() && bag.getItem() == ModItems.flowerBag) {
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
						entity.setStack(entityStack);
						bagInv.markDirty();

						if (!entity.isSilent()) {
							entity.world.playSound(null, player.getX(), player.getY(), player.getZ(),
									SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
									((entity.world.random.nextFloat() - entity.world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
						}
						((ServerPlayerEntity) player).networkHandler.sendPacket(new ItemPickupAnimationS2CPacket(entity.getEntityId(), player.getEntityId(), numPickedUp));
						player.currentScreenHandler.sendContentUpdates();

						return true;
					}
				}
			}
		}
		return false;
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		if (!world.isClient) {
			ItemStack stack = player.getStackInHand(hand);
			NamedScreenHandlerFactory container = new ExtendedScreenHandlerFactory() {
				@Override
				public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
					buf.writeBoolean(hand == Hand.MAIN_HAND);
				}

				@Override
				public Text getDisplayName() {
					return stack.getName();
				}

				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
					return new ContainerFlowerBag(syncId, inv, stack);
				}
			};
			player.openHandledScreen(container);
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
				Inventory tileInv;
				if (tile instanceof Inventory) {
					tileInv = (Inventory) tile;
				} else {
					return ActionResult.FAIL;
				}

				Inventory bagInv = getInventory(ctx.getStack());
				for (int i = 0; i < bagInv.size(); i++) {
					ItemStack flower = bagInv.getStack(i);
					ItemStack rem = HopperBlockEntity.transfer(bagInv, tileInv, flower, side);
					bagInv.setStack(i, rem);
				}

			}

			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}
}
