/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.network.clientbound;

import it.unimi.dsi.fastutil.ints.IntImmutableList;
import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.proxy.Proxy;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record ThundercallerEffectPacket(int attackerId, IntList targetIds) implements CustomPacketPayload {

	public static final Type<ThundercallerEffectPacket> ID = new Type<>(botaniaRL("tc"));
	public static final StreamCodec<ByteBuf, ThundercallerEffectPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, ThundercallerEffectPacket::attackerId,
			ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list(16)), ThundercallerEffectPacket::targetIds,
			(attackerId, targetIds) -> new ThundercallerEffectPacket(attackerId, new IntImmutableList(targetIds))
	);

	@Override
	public Type<ThundercallerEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(ThundercallerEffectPacket packet, Player localPlayer) {
			Level world = localPlayer.level();
			Entity attacker = world.getEntity(packet.attackerId());
			if (attacker == null) {
				return;
			}
			Vec3 source = VecHelper.fromEntityCenter(attacker);
			for (int id : packet.targetIds()) {
				var entity = world.getEntity(id);
				if (entity == null) {
					return;
				}
				var entityPos = VecHelper.fromEntityCenter(entity);
				Proxy.INSTANCE.lightningFX(world, source, entityPos, 1, 0x0179C4, 0xAADFFF);
				source = entityPos;
			}
		}
	}
}
