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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketLeftClick {
	public static final Identifier ID = prefix("lc");

	public static void send() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, PacketHandler.EMPTY_BUF);
	}

	public static void handle(PacketContext ctx, PacketByteBuf buf) {
		ctx.getTaskQueue().execute(() -> ((ItemTerraSword) ModItems.terraSword).trySpawnBurst(ctx.getPlayer()));
	}
}
