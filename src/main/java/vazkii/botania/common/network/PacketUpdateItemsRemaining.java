/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;

import javax.annotation.Nullable;

import java.util.function.Supplier;

public class PacketUpdateItemsRemaining {
	private final ItemStack stack;
	private final int count;
	@Nullable
	private final Text tooltip;

	public PacketUpdateItemsRemaining(ItemStack stack, int count, @Nullable Text tooltip) {
		this.stack = stack;
		this.count = count;
		this.tooltip = tooltip;
	}

	public static PacketUpdateItemsRemaining decode(PacketByteBuf buf) {
		return new PacketUpdateItemsRemaining(buf.readItemStack(), buf.readVarInt(), buf.readText());
	}

	public void encode(PacketByteBuf buf) {
		buf.writeItemStack(stack);
		buf.writeVarInt(count);
		buf.writeText(tooltip);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			ctx.get().enqueueWork(() -> ItemsRemainingRenderHandler.set(stack, count, tooltip));
			ctx.get().setPacketHandled(true);
		}
	}
}
