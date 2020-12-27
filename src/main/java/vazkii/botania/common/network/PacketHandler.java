/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;


import io.netty.buffer.Unpooled;

public final class PacketHandler {
	public static final PacketByteBuf EMPTY_BUF = new PacketByteBuf(Unpooled.buffer(0, 0));

	public static void init() {
		ServerSidePacketRegistry.INSTANCE.register(PacketLeftClick.ID, PacketLeftClick::handle);
		ServerSidePacketRegistry.INSTANCE.register(PacketDodge.ID, PacketDodge::handle);
		ServerSidePacketRegistry.INSTANCE.register(PacketIndexKeybindRequest.ID, PacketIndexKeybindRequest::handle);
		ServerSidePacketRegistry.INSTANCE.register(PacketJump.ID, PacketJump::handle);
	}

	public static void initClient() {
		ClientSidePacketRegistry.INSTANCE.register(PacketBotaniaEffect.ID, PacketBotaniaEffect.Handler::handle);
		ClientSidePacketRegistry.INSTANCE.register(PacketItemAge.ID, PacketItemAge.Handler::handle);
		ClientSidePacketRegistry.INSTANCE.register(PacketSpawnEntity.ID, PacketSpawnEntity.Handler::handle);
		ClientSidePacketRegistry.INSTANCE.register(PacketSpawnDoppleganger.ID, PacketSpawnDoppleganger.Handler::handle);
		ClientSidePacketRegistry.INSTANCE.register(PacketUpdateItemsRemaining.ID, PacketUpdateItemsRemaining::handle);
		ClientSidePacketRegistry.INSTANCE.register(PacketGogWorld.ID, PacketGogWorld.Handler::handle);
	}

	private PacketHandler() {}

}
