/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;

import java.util.function.Supplier;

public class PacketLeftClick {
	public static void encode(PacketLeftClick msg, PacketBuffer buf) {}

	public static PacketLeftClick decode(PacketBuffer buf) {
		return new PacketLeftClick();
	}

	public static void handle(PacketLeftClick msg, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isServer()) {
			ctx.get().enqueueWork(() -> ((ItemTerraSword) ModItems.terraSword).trySpawnBurst(ctx.get().getSender()));
		}
		ctx.get().setPacketHandled(true);
	}
}
