/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.common.entity.EntityDoppleganger;

import java.io.IOException;
import java.util.UUID;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketSpawnDoppleganger {
	public static final Identifier ID = prefix("spg");

	public static Packet<?> make(EntityDoppleganger entity, int playerCount, boolean hardMode,
			BlockPos source, UUID bossInfoId) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		try {
			new MobSpawnS2CPacket(entity).write(buf);
		} catch (IOException ignored) {} // isn't actually thrown by write

		buf.writeVarInt(playerCount);
		buf.writeBoolean(hardMode);
		buf.writeBlockPos(source);
		buf.writeUuid(bossInfoId);
		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	public static class Handler {
		public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			MobSpawnS2CPacket pkt = new MobSpawnS2CPacket();
			try {
				pkt.read(buf);
			} catch (IOException ignored) {} // isn't actually thrown
			int playerCount = buf.readVarInt();
			boolean hardMode = buf.readBoolean();
			BlockPos source = buf.readBlockPos();
			UUID bossInfoUuid = buf.readUuid();

			client.execute(() -> {
				ClientPlayerEntity player = client.player;
				if (player != null) {
					player.networkHandler.onMobSpawn(pkt);
					int eid = pkt.getId();
					Entity e = player.world.getEntityById(eid);
					if (e instanceof EntityDoppleganger) {
						((EntityDoppleganger) e).readSpawnData(playerCount, hardMode, source, bossInfoUuid);
					}
				}
			});
		}
	}
}
