/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.Map;

import io.netty.buffer.Unpooled;

public class PacketOrechidOreWeights {
	public static final Identifier ID = ResourceLocationHelper.prefix("s2c_ore_weights");

	public static void send(PlayerEntity player, Map<Identifier, Integer> weights) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, make(weights));
	}

	public static Packet<?> make(Map<Identifier, Integer> weights) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(weights.equals(BotaniaAPI.instance().getNetherOreWeights()));
		buf.writeInt(weights.size());
		for (Map.Entry<Identifier, Integer> e : weights.entrySet()) {
			buf.writeIdentifier(e.getKey());
			buf.writeInt(e.getValue());
		}
		return ServerSidePacketRegistry.INSTANCE.toPacket(ID, buf);
	}

	public static void handle(PacketContext ctx, PacketByteBuf buf) {
		boolean isIgnem = buf.readBoolean();
		int loop = buf.readInt();
		ctx.getTaskQueue().execute(() -> {
			for (int i = 0; i < loop; i++) {
				Botania.LOGGER.info(buf.readIdentifier() + "");
				if (isIgnem) {
					BotaniaAPI.instance().registerNetherOreWeight(buf.readIdentifier(), buf.readInt());
				} else {
					BotaniaAPI.instance().registerOreWeight(buf.readIdentifier(), buf.readInt());
				}
			}
		});
	}
}
