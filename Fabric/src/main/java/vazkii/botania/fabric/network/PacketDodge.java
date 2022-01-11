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
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketDodge {
	public static final ResourceLocation ID = prefix("do");

	public static void send() {
		ClientPlayNetworking.send(ID, PacketHandler.EMPTY_BUF);
	}

	public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
		server.execute(() -> {
			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.dash, SoundSource.PLAYERS, 1F, 1F);

			ItemStack ringStack = EquipmentHandler.findOrEmpty(ModItems.dodgeRing, player);
			if (ringStack.isEmpty()) {
				player.connection.disconnect(new TranslatableComponent("botaniamisc.invalidDodge"));
				return;
			}

			player.causeFoodExhaustion(0.3F);
			ItemNBTHelper.setInt(ringStack, ItemDodgeRing.TAG_DODGE_COOLDOWN, ItemDodgeRing.MAX_CD);
		});
	}
}
