/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ItemCloudPendant;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;

import java.util.function.Supplier;

public class PacketJump {
	public static void encode(PacketJump msg, PacketByteBuf buf) {}

	public static PacketJump decode(PacketByteBuf buf) {
		return new PacketJump();
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isServer()) {
			ctx.get().enqueueWork(() -> {
				ServerPlayerEntity player = ctx.get().getSender();

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
		ctx.get().setPacketHandled(true);
	}
}
