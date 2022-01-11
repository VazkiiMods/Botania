/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import vazkii.botania.common.item.rod.ItemTornadoRod;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketAvatarTornadoRod {
	public static final ResourceLocation ID = prefix("avatar_tornado_rod");

	public static void sendTo(ServerPlayer player, boolean elytra) {
		if (!player.level.isClientSide) {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeBoolean(elytra);
			ServerPlayNetworking.send(player, ID, buf);
		}
	}

	public static class Handler {
		public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			boolean elytra = buf.readBoolean();
			client.execute(() -> {
				Player player = Minecraft.getInstance().player;
				Level world = Minecraft.getInstance().level;
				if (elytra) {
					ItemTornadoRod.doAvatarElytraBoost(player, world);
				} else {
					ItemTornadoRod.doAvatarJump(player, world);
				}
			});
		}
	}
}
