/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.serverbound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ItemCloudPendant;
import vazkii.botania.network.IPacket;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketJump implements IPacket {
	public static final PacketJump INSTANCE = new PacketJump();
	public static final ResourceLocation ID = prefix("jmp");

	@Override
	public void encode(FriendlyByteBuf buf) {

	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public static PacketJump decode(FriendlyByteBuf buf) {
		return INSTANCE;
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> {
			ItemStack amuletStack = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemCloudPendant, player);
			if (!amuletStack.isEmpty()) {
				player.causeFoodExhaustion(0.3F);
				player.fallDistance = 0;

				ItemCloudPendant.setJumping(player);
			}
		});
	}
}
