/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.network.IPacket;

import javax.annotation.Nullable;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public record PacketUpdateItemsRemaining(ItemStack stack, int count, @Nullable Component tooltip) implements IPacket {

	public static final ResourceLocation ID = prefix("rem");

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeItem(stack);
		buf.writeVarInt(count);
		buf.writeBoolean(tooltip != null);
		if (tooltip != null) {
			buf.writeComponent(tooltip);
		}
	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public static PacketUpdateItemsRemaining decode(FriendlyByteBuf buf) {
		return new PacketUpdateItemsRemaining(
				buf.readItem(),
				buf.readVarInt(),
				buf.readBoolean() ? buf.readComponent() : null
		);
	}

	public static class Handler {
		public static void handle(PacketUpdateItemsRemaining packet) {
			ItemStack stack = packet.stack();
			int count = packet.count();
			Component tooltip = packet.tooltip();
			Minecraft.getInstance().execute(() -> ItemsRemainingRenderHandler.set(stack, count, tooltip));
		}
	}
}
