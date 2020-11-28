/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ItemCloudPendant;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketJump {
	public static final Identifier ID = prefix("jmp");

	public static void send() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, PacketHandler.EMPTY_BUF);
	}

	public static void handle(PacketContext ctx, PacketByteBuf buf) {
		ctx.getTaskQueue().execute(() -> {
			PlayerEntity player = ctx.getPlayer();

			ItemStack amuletStack = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemCloudPendant, player);
			if (!amuletStack.isEmpty()) {
				player.addExhaustion(0.3F);
				player.fallDistance = 0;

				ItemStack belt = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemTravelBelt, player);
				if (!belt.isEmpty()) {
					player.fallDistance = -((ItemTravelBelt) belt.getItem()).fallBuffer * ((ItemCloudPendant) amuletStack.getItem()).getMaxAllowedJumps();
				}
			}
		});
	}
}
