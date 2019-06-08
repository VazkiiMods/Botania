/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [22/10/2016, 18:18:19 (GMT)]
 */
package vazkii.botania.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.botania.common.Botania;
import vazkii.botania.common.integration.curios.CurioIntegration;
import vazkii.botania.common.item.equipment.bauble.ItemCloudPendant;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;

import java.util.function.Supplier;

public class PacketJump {
	public static void encode(PacketJump msg, PacketBuffer buf) {}

	public static PacketJump decode(PacketBuffer buf) {
		return new PacketJump();
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();

			if(Botania.curiosLoaded) {
				ItemStack amuletStack = CurioIntegration.findOrEmpty(s -> s.getItem() instanceof ItemCloudPendant, player);
				if(!amuletStack.isEmpty()) {
					player.addExhaustion(0.3F);
					player.fallDistance = 0;

					ItemStack belt = CurioIntegration.findOrEmpty(s -> s.getItem() instanceof ItemTravelBelt, player);
					if(!belt.isEmpty())
						player.fallDistance = -((ItemTravelBelt) belt.getItem()).fallBuffer * ((ItemCloudPendant) amuletStack.getItem()).getMaxAllowedJumps();
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
