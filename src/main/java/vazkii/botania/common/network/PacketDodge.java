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
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketDodge {
	public static final Identifier ID = prefix("do");

	public static void send() {
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, PacketHandler.EMPTY_BUF);
	}

	public static void handle(PacketContext ctx, PacketByteBuf buf) {
		ctx.getTaskQueue().execute(() -> {
			ServerPlayerEntity player = (ServerPlayerEntity) ctx.getPlayer();
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);

			ItemStack ringStack = EquipmentHandler.findOrEmpty(ModItems.dodgeRing, player);
			if (ringStack.isEmpty()) {
				player.networkHandler.disconnect(new TranslatableText("botaniamisc.invalidDodge"));
				return;
			}

			player.addExhaustion(0.3F);
			ItemNBTHelper.setInt(ringStack, ItemDodgeRing.TAG_DODGE_COOLDOWN, ItemDodgeRing.MAX_CD);
		});
	}
}
