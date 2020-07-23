/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLCapabilities;

import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.lib.LibMisc;

public final class DebugHandler {

	private DebugHandler() {}

	private static final String PREFIX = Formatting.GREEN + "[Botania] " + Formatting.RESET;

	public static void onDrawDebugText(RenderGameOverlayEvent.Text event) {
		World world = MinecraftClient.getInstance().world;
		if (ConfigHandler.CLIENT.debugInfo.getValue() && MinecraftClient.getInstance().options.debugEnabled) {
			event.getLeft().add("");
			String version = ModList.get().getModContainerById(LibMisc.MOD_ID)
					.map(ModContainer::getModInfo)
					.map(IModInfo::getVersion)
					.map(Object::toString)
					.orElse("N/A");

			event.getLeft().add(PREFIX + "(CLIENT) netColl: " + ManaNetworkHandler.instance.getAllCollectorsInWorld(world).size() + ", netPool: " + ManaNetworkHandler.instance.getAllPoolsInWorld(world).size() + ", rv: " + version);

			if (MinecraftClient.getInstance().isIntegratedServerRunning()) {
				RegistryKey<World> dim = MinecraftClient.getInstance().world.getRegistryKey();
				Identifier dimName = dim.getValue();
				if (ServerLifecycleHooks.getCurrentServer() != null) {
					World serverWorld = ServerLifecycleHooks.getCurrentServer().getWorld(dim);
					event.getLeft().add(PREFIX + String.format("(INTEGRATED SERVER %s) netColl : %d, netPool: %d", dimName, ManaNetworkHandler.instance.getAllCollectorsInWorld(serverWorld).size(), ManaNetworkHandler.instance.getAllPoolsInWorld(serverWorld).size()));
				}
			}

			if (Screen.hasControlDown() && Screen.hasShiftDown()) {
				event.getLeft().add(PREFIX + "Config Context");
				event.getLeft().add("  shaders.enabled: " + ConfigHandler.CLIENT.useShaders.getValue());

				GLCapabilities caps = GL.getCapabilities();
				event.getLeft().add(PREFIX + "OpenGL Context");
				event.getLeft().add("  GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
				event.getLeft().add("  GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
				event.getLeft().add("  GL_SHADING_LANGUAGE_VERSION: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
				event.getLeft().add("  GL_ARB_multitexture: " + caps.GL_ARB_multitexture);
				event.getLeft().add("  GL_ARB_texture_non_power_of_two: " + caps.GL_ARB_texture_non_power_of_two);
				event.getLeft().add("  OpenGL13: " + caps.OpenGL13);
			} else if (MinecraftClient.IS_SYSTEM_MAC) {
				event.getLeft().add(PREFIX + "SHIFT+CMD for context");
			} else {
				event.getLeft().add(PREFIX + "SHIFT+CTRL for context");
			}
		}
	}

}
