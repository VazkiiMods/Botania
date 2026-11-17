/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.clientbound;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.client.gui.ItemsRemainingRenderHandler;

import java.util.Optional;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public record UpdateItemsRemainingPacket(ItemStack stack, int count, @Nullable Component tooltip) implements CustomPacketPayload {

	public static final Type<UpdateItemsRemainingPacket> ID = new Type<>(botaniaRL("rem"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateItemsRemainingPacket> STREAM_CODEC = StreamCodec.composite(
			ItemStack.OPTIONAL_STREAM_CODEC, UpdateItemsRemainingPacket::stack,
			ByteBufCodecs.VAR_INT, UpdateItemsRemainingPacket::count,
			ComponentSerialization.OPTIONAL_STREAM_CODEC.map(o -> o.orElse(null), Optional::ofNullable), UpdateItemsRemainingPacket::tooltip,
			UpdateItemsRemainingPacket::new
	);

	@Override
	public Type<UpdateItemsRemainingPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(UpdateItemsRemainingPacket packet, Player localPlayer) {
			ItemsRemainingRenderHandler.set(packet.stack(), packet.count(), packet.tooltip());
		}
	}
}
