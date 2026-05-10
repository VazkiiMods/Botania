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
import net.minecraft.world.entity.player.Player;

import vazkii.botania.common.item.rod.SkiesRodItem;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record AvatarSkiesRodUpdatePacket(boolean elytra) implements CustomPacketPayload {

	public static final Type<AvatarSkiesRodUpdatePacket> ID = new Type<>(botaniaRL("asu"));
	public static final StreamCodec<ByteBuf, AvatarSkiesRodUpdatePacket> STREAM_CODEC = ByteBufCodecs.BOOL
			.map(AvatarSkiesRodUpdatePacket::new, AvatarSkiesRodUpdatePacket::elytra);

	@Override
	public Type<AvatarSkiesRodUpdatePacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(AvatarSkiesRodUpdatePacket packet, Player localPlayer) {
			if (packet.elytra()) {
				SkiesRodItem.doAvatarElytraBoost(localPlayer, localPlayer.level());
			} else {
				SkiesRodItem.doAvatarJump(localPlayer, localPlayer.level());
			}
		}
	}
}
