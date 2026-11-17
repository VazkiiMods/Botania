/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.network.serverbound;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.common.handler.EquipmentHandler;
import org.violetmoon.botania.common.helper.PlayerHelper;
import org.violetmoon.botania.common.item.equipment.bauble.CirrusAmuletItem;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public class JumpPacket implements CustomPacketPayload {
	public static final JumpPacket INSTANCE = new JumpPacket();
	public static final Type<JumpPacket> ID = new Type<>(botaniaRL("jmp"));
	public static final StreamCodec<ByteBuf, JumpPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	private JumpPacket() {}

	@Override
	public Type<JumpPacket> type() {
		return ID;
	}

	public void handle(ServerPlayer player) {
		ItemStack amuletStack = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof CirrusAmuletItem, player);
		if (!amuletStack.isEmpty()) {
			player.causeFoodExhaustion(0.3F);
			player.fallDistance = 0;

			PlayerHelper.setCurrentImpulseImpactPos(player, 0, player);
			CirrusAmuletItem.setJumping(player);
		}
	}
}
