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
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.client.gui.box.ContainerBaubleBox;
import vazkii.botania.common.core.handler.EquipmentHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBaubleBox extends Item {
	public static final int SIZE = 24;

	public ItemBaubleBox(Settings props) {
		super(props);
	}

	public static SimpleInventory getInventory(ItemStack stack) {
		return new ItemBackedInventory(stack, SIZE) {
			@Override
			public boolean isValid(int index, @Nonnull ItemStack stack) {
				return EquipmentHandler.instance.isAccessory(stack);
			}
		};
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		if (!world.isClient) {
			ItemStack stack = player.getStackInHand(hand);
			NamedScreenHandlerFactory container = new SimpleNamedScreenHandlerFactory((w, p, pl) -> new ContainerBaubleBox(w, p, stack), stack.getName());
			NetworkHooks.openGui((ServerPlayerEntity) player, container, b -> {
				b.writeBoolean(hand == Hand.MAIN_HAND);
			});
		}
		return TypedActionResult.success(player.getStackInHand(hand));
	}
}
