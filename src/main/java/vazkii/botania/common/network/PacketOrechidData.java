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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.impl.BotaniaAPIImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketOrechidData {
	public static final ResourceLocation ID = prefix("orechid");

	public static void sendNonLocal(ServerPlayer player) {
		if (player.server.isDedicatedServer() || !player.getGameProfile().getName().equals(player.server.getSingleplayerName())) {
			send(player);
		}
	}

	private static void send(ServerPlayer player) {
		List<OrechidOutput> normal = BotaniaAPI.instance().getOrechidWeights();
		List<OrechidOutput> nether = BotaniaAPI.instance().getNetherOrechidWeights();
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeVarInt(normal.size());
		for (OrechidOutput output : normal) {
			output.getOutput().write(buf);
			buf.writeVarInt(output.getWeight());
		}
		buf.writeVarInt(nether.size());
		for (OrechidOutput output : nether) {
			output.getOutput().write(buf);
			buf.writeVarInt(output.getWeight());
		}
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
		int count = buf.readVarInt();
		List<OrechidOutput> normal = Stream.generate(() -> {
			StateIngredient ingr = StateIngredientHelper.read(buf);
			int weight = buf.readVarInt();
			return new OrechidOutput(weight, ingr);
		}).limit(count).collect(Collectors.toList());
		count = buf.readVarInt();
		List<OrechidOutput> nether = Stream.generate(() -> {
			StateIngredient ingr = StateIngredientHelper.read(buf);
			int weight = buf.readVarInt();
			return new OrechidOutput(weight, ingr);
		}).limit(count).collect(Collectors.toList());
		client.execute(() -> {
			Botania.LOGGER.debug("Received orechid data with {} standard and {} nether results", normal.size(), nether.size());
			BotaniaAPIImpl.weights = normal;
			BotaniaAPIImpl.netherWeights = nether;
		});
	}

}
