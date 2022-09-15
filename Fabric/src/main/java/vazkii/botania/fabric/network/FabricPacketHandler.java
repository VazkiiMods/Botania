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
		ServerPlayNetworking.registerGlobalReceiver(DodgePacket.ID, makeServerBoundHandler(DodgePacket::decode, DodgePacket::handle));
		ServerPlayNetworking.registerGlobalReceiver(IndexKeybindRequestPacket.ID, makeServerBoundHandler(IndexKeybindRequestPacket::decode, IndexKeybindRequestPacket::handle));
		ServerPlayNetworking.registerGlobalReceiver(IndexStringRequestPacket.ID, makeServerBoundHandler(IndexStringRequestPacket::decode, IndexStringRequestPacket::handle));
		ServerPlayNetworking.registerGlobalReceiver(JumpPacket.ID, makeServerBoundHandler(JumpPacket::decode, JumpPacket::handle));
		ServerPlayNetworking.registerGlobalReceiver(LeftClickPacket.ID, makeServerBoundHandler(LeftClickPacket::decode, LeftClickPacket::handle));
	}

	private static <T> ServerPlayNetworking.PlayChannelHandler makeServerBoundHandler(Function<FriendlyByteBuf, T> decoder, TriConsumer<T, MinecraftServer, ServerPlayer> handle) {
		return (server, player, _handler, buf, _responseSender) -> handle.accept(decoder.apply(buf), server, player);
	}

	public static void initClient() {
		ClientPlayNetworking.registerGlobalReceiver(AvatarSkiesRodPacket.ID, makeClientBoundHandler(AvatarSkiesRodPacket::decode, AvatarSkiesRodPacket.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(BotaniaEffectPacket.ID, makeClientBoundHandler(BotaniaEffectPacket::decode, BotaniaEffectPacket.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(GogWorldPacket.ID, makeClientBoundHandler(GogWorldPacket::decode, GogWorldPacket.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(ItemAgePacket.ID, makeClientBoundHandler(ItemAgePacket::decode, ItemAgePacket.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(SpawnGaiaGuardianPacket.ID, makeClientBoundHandler(SpawnGaiaGuardianPacket::decode, SpawnGaiaGuardianPacket.Handler::handle));
		ClientPlayNetworking.registerGlobalReceiver(UpdateItemsRemainingPacket.ID, makeClientBoundHandler(UpdateItemsRemainingPacket::decode, UpdateItemsRemainingPacket.Handler::handle));
	}

	private static <T> ClientPlayNetworking.PlayChannelHandler makeClientBoundHandler(Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
		return (_client, _handler, buf, _responseSender) -> handler.accept(decoder.apply(buf));
	}

	private FabricPacketHandler() {}

}
