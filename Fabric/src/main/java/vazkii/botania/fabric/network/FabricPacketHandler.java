/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import vazkii.botania.network.TriConsumer;
import vazkii.botania.network.clientbound.*;
import vazkii.botania.network.serverbound.*;

import java.util.function.Consumer;
import java.util.function.Function;

public final class FabricPacketHandler {
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(PacketDodge.ID, makeServerBoundHandler(PacketDodge::decode, PacketDodge::handle));
		ServerPlayNetworking.registerGlobalReceiver(PacketIndexKeybindRequest.ID, makeServerBoundHandler(PacketIndexKeybindRequest::decode, PacketIndexKeybindRequest::handle));
		ServerPlayNetworking.registerGlobalReceiver(PacketJump.ID, makeServerBoundHandler(PacketJump::decode, PacketJump::handle));
		ServerPlayNetworking.registerGlobalReceiver(PacketLeftClick.ID, makeServerBoundHandler(PacketLeftClick::decode, PacketLeftClick::handle));
	}

	private static <T> ServerPlayNetworking.PlayChannelHandler makeServerBoundHandler(Function<FriendlyByteBuf, T> decoder, TriConsumer<T, MinecraftServer, ServerPlayer> handle) {
		return (server, player, _handler, buf, _responseSender) -> handle.accept(decoder.apply(buf), server, player);
	}

	public static void initClient() {
		ClientPlayNetworking.registerGlobalReceiver(PacketAvatarTornadoRod.ID, makeClientBoundHandler(PacketAvatarTornadoRod::decode, PacketAvatarTornadoRod.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(PacketBotaniaEffect.ID, makeClientBoundHandler(PacketBotaniaEffect::decode, PacketBotaniaEffect.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(PacketGogWorld.ID, makeClientBoundHandler(PacketGogWorld::decode, PacketGogWorld.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(PacketItemAge.ID, makeClientBoundHandler(PacketItemAge::decode, PacketItemAge.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(PacketSpawnDoppleganger.ID, makeClientBoundHandler(PacketSpawnDoppleganger::decode, PacketSpawnDoppleganger.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(PacketUpdateItemsRemaining.ID, makeClientBoundHandler(PacketUpdateItemsRemaining::decode, PacketUpdateItemsRemaining.Handler::handle));
	}

	private static <T> ClientPlayNetworking.PlayChannelHandler makeClientBoundHandler(Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
		return (_client, _handler, buf, _responseSender) -> handler.accept(decoder.apply(buf));
	}

	private FabricPacketHandler() {}

}
