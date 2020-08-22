/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.client.core.SkyblockWorldInfo;

import java.util.function.Supplier;

public class PacketGogWorld {
	public static void encode(PacketGogWorld pkt, PacketBuffer buf) {}

	public static PacketGogWorld decode(PacketBuffer buf) {
		return new PacketGogWorld();
	}

	public static void handle(PacketGogWorld pkt, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isClient()) {
			ctx.get().enqueueWork(() -> {
				ClientWorld.ClientWorldInfo info = Minecraft.getInstance().world.getWorldInfo();
				if (info instanceof SkyblockWorldInfo) {
					((SkyblockWorldInfo) info).markGardenOfGlass();
				}
			});
		}
	}
}
