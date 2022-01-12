/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.serverbound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;
import vazkii.botania.network.IPacket;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketLeftClick implements IPacket {
	public static final PacketLeftClick INSTANCE = new PacketLeftClick();
	public static final ResourceLocation ID = prefix("lc");

	@Override
	public void encode(FriendlyByteBuf buf) {

	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public static PacketLeftClick decode(FriendlyByteBuf buf) {
		return INSTANCE;
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> ItemTerraSword.trySpawnBurst(player));
	}
}
