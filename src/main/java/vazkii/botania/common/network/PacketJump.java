/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ItemCloudPendant;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketJump {
	public static final Identifier ID = prefix("jmp");

	public static void send() {
		ClientPlayNetworking.send(ID, PacketHandler.EMPTY_BUF);
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		server.execute(() -> {
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
