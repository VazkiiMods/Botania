/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;

import vazkii.botania.common.entity.GaiaGuardianEntity;

import java.util.UUID;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public record SpawnGaiaGuardianPacket(int entityId, int playerCount, boolean hardMode,
		BlockPos source, UUID bossInfoId) implements CustomPacketPayload {

	public static final Type<SpawnGaiaGuardianPacket> ID = new Type<>(botaniaRL("spg"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SpawnGaiaGuardianPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SpawnGaiaGuardianPacket::entityId,
			ByteBufCodecs.VAR_INT, SpawnGaiaGuardianPacket::playerCount,
			ByteBufCodecs.BOOL, SpawnGaiaGuardianPacket::hardMode,
			BlockPos.STREAM_CODEC, SpawnGaiaGuardianPacket::source,
			UUIDUtil.STREAM_CODEC, SpawnGaiaGuardianPacket::bossInfoId,
			SpawnGaiaGuardianPacket::new
	);

	@Override
	public Type<SpawnGaiaGuardianPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(SpawnGaiaGuardianPacket packet) {
			int playerCount = packet.playerCount();
			boolean hardMode = packet.hardMode();
			BlockPos source = packet.source();
			UUID bossInfoUuid = packet.bossInfoId();

			Minecraft.getInstance().execute(() -> {
				var player = Minecraft.getInstance().player;
				if (player != null) {
					Entity e = player.level().getEntity(packet.entityId());
					if (e instanceof GaiaGuardianEntity) {
						GaiaGuardianEntity dopple = (GaiaGuardianEntity) e;
						dopple.readSpawnData(playerCount, hardMode, source, bossInfoUuid);
					}
				}
			});
		}
	}
}
