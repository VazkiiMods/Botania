/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.serverbound;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import vazkii.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public class LeftClickPacket implements CustomPacketPayload {
	public static final LeftClickPacket INSTANCE = new LeftClickPacket();
	public static final Type<LeftClickPacket> ID = new Type<>(botaniaRL("lc"));
	public static final StreamCodec<ByteBuf, LeftClickPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	private LeftClickPacket() {}

	@Override
	public Type<LeftClickPacket> type() {
		return ID;
	}

	public void handle(ServerPlayer player) {
		TerraBladeItem.trySpawnBurst(player, player.getAttackStrengthScale(0F));
	}
}
