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
import net.minecraft.resources.ResourceLocation;

import vazkii.botania.client.core.SkyblockWorldInfo;
import vazkii.botania.network.IPacket;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketGogWorld implements IPacket {
	public static final PacketGogWorld INSTANCE = new PacketGogWorld();
	public static final ResourceLocation ID = prefix("gog");

	@Override
	public void encode(FriendlyByteBuf buf) {}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public static PacketGogWorld decode(FriendlyByteBuf buf) {
		return INSTANCE;
	}

	public static class Handler {
		public static void handle(PacketGogWorld packet) {
			Minecraft.getInstance().execute(() -> {
				if (Minecraft.getInstance().level.getLevelData() instanceof SkyblockWorldInfo skyblockInfo) {
					skyblockInfo.markGardenOfGlass();
				}
			});
		}
	}
}
