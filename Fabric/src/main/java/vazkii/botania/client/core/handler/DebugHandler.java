/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.List;

public final class DebugHandler {

	private DebugHandler() {}

	private static final String PREFIX = ChatFormatting.GREEN + "[Botania] " + ChatFormatting.RESET;

	public static void onDrawDebugText(List<String> left) {
		Level world = Minecraft.getInstance().level;
		if (ConfigHandler.CLIENT.debugInfo.getValue()) {
			left.add("");
			String version = IXplatAbstractions.INSTANCE.getBotaniaVersion();

			left.add(PREFIX + "(CLIENT) netColl: " + ManaNetworkHandler.instance.getAllCollectorsInWorld(world).size() + ", netPool: " + ManaNetworkHandler.instance.getAllPoolsInWorld(world).size() + ", rv: " + version);

			if (Minecraft.getInstance().hasSingleplayerServer()) {
				ResourceKey<Level> dim = Minecraft.getInstance().level.dimension();
				ResourceLocation dimName = dim.location();
				if (Minecraft.getInstance().getSingleplayerServer() != null) {
					Level serverWorld = Minecraft.getInstance().getSingleplayerServer().getLevel(dim);
					left.add(PREFIX + String.format("(INTEGRATED SERVER %s) netColl : %d, netPool: %d", dimName, ManaNetworkHandler.instance.getAllCollectorsInWorld(serverWorld).size(), ManaNetworkHandler.instance.getAllPoolsInWorld(serverWorld).size()));
				}
			}
		}
	}

}
