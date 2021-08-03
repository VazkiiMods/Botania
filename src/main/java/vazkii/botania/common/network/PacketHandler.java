/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;


import io.netty.buffer.Unpooled;

public final class PacketHandler {
	public static final FriendlyByteBuf EMPTY_BUF = new FriendlyByteBuf(Unpooled.buffer(0, 0));

	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(PacketLeftClick.ID, PacketLeftClick::handle);
		ServerPlayNetworking.registerGlobalReceiver(PacketDodge.ID, PacketDodge::handle);
		ServerPlayNetworking.registerGlobalReceiver(PacketIndexKeybindRequest.ID, PacketIndexKeybindRequest::handle);
		ServerPlayNetworking.registerGlobalReceiver(PacketJump.ID, PacketJump::handle);
	}

	public static void initClient() {
		ClientPlayNetworking.registerGlobalReceiver(PacketBotaniaEffect.ID, PacketBotaniaEffect.Handler::handle);
		ClientPlayNetworking.registerGlobalReceiver(PacketItemAge.ID, PacketItemAge.Handler::handle);
		ClientPlayNetworking.registerGlobalReceiver(PacketSpawnEntity.ID, PacketSpawnEntity.Handler::handle);
		ClientPlayNetworking.registerGlobalReceiver(PacketSpawnDoppleganger.ID, PacketSpawnDoppleganger.Handler::handle);
		ClientPlayNetworking.registerGlobalReceiver(PacketUpdateItemsRemaining.ID, PacketUpdateItemsRemaining.Handler::handle);
		ClientPlayNetworking.registerGlobalReceiver(PacketGogWorld.ID, PacketGogWorld.Handler::handle);
		ClientPlayNetworking.registerGlobalReceiver(PacketOrechidData.ID, PacketOrechidData::handle);
		ClientPlayNetworking.registerGlobalReceiver(PacketAvatarTornadoRod.ID, PacketAvatarTornadoRod::handle);
	}

	private PacketHandler() {}

}
