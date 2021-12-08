/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import java.util.function.Supplier;

public class PacketIndexKeybindRequest {
	private final ItemStack stack;

	public PacketIndexKeybindRequest(ItemStack stack) {
		this.stack = stack;
	}

	public static PacketIndexKeybindRequest decode(PacketBuffer buf) {
		return new PacketIndexKeybindRequest(buf.readItemStack());
	}

	public static void encode(PacketIndexKeybindRequest msg, PacketBuffer buf) {
		buf.writeItemStack(msg.stack);
	}

	public static void handle(PacketIndexKeybindRequest message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isServer()) {
			ctx.get().enqueueWork(() -> {
				ServerPlayerEntity player = ctx.get().getSender();
				if (player.isSpectator()) {
					return;
				}

				for (TileCorporeaIndex index : TileCorporeaIndex.InputHandler.getNearbyIndexes(player)) {
					if (index.getSpark() != null) {
						boolean checkNBT = index.checksNBT();
						ItemStack stack = message.stack.copy();
						if (!checkNBT) {
							stack.setTag(null);
						}
						index.performPlayerRequest(player, CorporeaHelper.instance().createMatcher(stack, checkNBT), stack.getCount());
					}
				}
			});
		}
		ctx.get().setPacketHandled(true);
	}
}
