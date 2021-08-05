/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ItemCloudPendant;

import java.util.function.Supplier;

public class PacketJump {
	public static void encode(PacketJump msg, PacketBuffer buf) {}

	public static PacketJump decode(PacketBuffer buf) {
		return new PacketJump();
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isServer()) {
			ctx.get().enqueueWork(() -> {
				ServerPlayerEntity player = ctx.get().getSender();

				ItemStack amuletStack = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemCloudPendant, player);
				if (!amuletStack.isEmpty()) {
					net.minecraftforge.common.ForgeHooks.onLivingJump(player);
					player.addExhaustion(0.3F);
					player.fallDistance = 0;
					ItemCloudPendant.setJumping(player);
				}
			});
		}
		ctx.get().setPacketHandled(true);
	}
}
