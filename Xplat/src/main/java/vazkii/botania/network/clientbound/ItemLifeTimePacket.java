/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.clientbound;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

import vazkii.botania.xplat.XplatAbstractions;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record ItemLifeTimePacket(int entityId, short timeCounter) implements CustomPacketPayload {

	public static final Type<ItemLifeTimePacket> ID = new Type<>(botaniaRL("ia"));
	public static final StreamCodec<ByteBuf, ItemLifeTimePacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, ItemLifeTimePacket::entityId,
			ByteBufCodecs.SHORT, ItemLifeTimePacket::timeCounter,
			ItemLifeTimePacket::new
	);

	/**
	 * Make sure the client has the correct item lifetime in case something needs to know, e.g. a Daffomill trying to
	 * push an item dropped by a powered Open Crate, or an item only starting to be tracked some time after it spawned.
	 */
	public static void onItemTrack(Entity entity, ServerPlayer player) {
		if (entity instanceof ItemEntity item) {
			short lifeTime = XplatAbstractions.INSTANCE.getItemLifeTime(item);
			if (lifeTime != 0) {
				// only send packet if necessary
				// (Fabric calls this before actually sending the entity packet, so do this later during the same tick)
				player.server.tell(new TickTask(0, () -> XplatAbstractions.INSTANCE.sendToPlayer(player,
						new ItemLifeTimePacket(item.getId(), lifeTime))));
			}
		}
	}

	@Override
	public Type<ItemLifeTimePacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(ItemLifeTimePacket packet, Player localPlayer) {
			Entity entity = localPlayer.level().getEntity(packet.entityId());
			if (entity instanceof ItemEntity item) {
				XplatAbstractions.INSTANCE.setItemLifeTime(item, packet.timeCounter());
			}
		}
	}

}
