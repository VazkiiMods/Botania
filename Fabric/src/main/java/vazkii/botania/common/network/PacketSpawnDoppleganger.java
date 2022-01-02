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
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import vazkii.botania.common.entity.EntityDoppleganger;

import java.util.UUID;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketSpawnDoppleganger {
	public static final ResourceLocation ID = prefix("spg");

	public static Packet<?> make(EntityDoppleganger entity, int playerCount, boolean hardMode,
			BlockPos source, UUID bossInfoId) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		new ClientboundAddMobPacket(entity).write(buf);

		buf.writeVarInt(playerCount);
		buf.writeBoolean(hardMode);
		buf.writeBlockPos(source);
		buf.writeUUID(bossInfoId);
		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	public static class Handler {
		public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			ClientboundAddMobPacket pkt = new ClientboundAddMobPacket(buf);
			int playerCount = buf.readVarInt();
			boolean hardMode = buf.readBoolean();
			BlockPos source = buf.readBlockPos();
			UUID bossInfoUuid = buf.readUUID();

			client.execute(() -> {
				LocalPlayer player = client.player;
				if (player != null) {
					player.connection.handleAddMob(pkt);
					int eid = pkt.getId();
					Entity e = player.level.getEntity(eid);
					if (e instanceof EntityDoppleganger) {
						((EntityDoppleganger) e).readSpawnData(playerCount, hardMode, source, bossInfoUuid);
					}
				}
			});
		}
	}
}
