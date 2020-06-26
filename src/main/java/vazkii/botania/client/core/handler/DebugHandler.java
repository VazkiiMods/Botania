/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.forgespi.language.IModInfo;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLCapabilities;

import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.lib.LibMisc;

public final class DebugHandler {

	private DebugHandler() {}

	private static final String PREFIX = TextFormatting.GREEN + "[Botania] " + TextFormatting.RESET;

	public static void onDrawDebugText(RenderGameOverlayEvent.Text event) {
		World world = Minecraft.getInstance().world;
		if (ConfigHandler.CLIENT.debugInfo.get() && Minecraft.getInstance().gameSettings.showDebugInfo) {
			event.getLeft().add("");
			String version = ModList.get().getModContainerById(LibMisc.MOD_ID)
					.map(ModContainer::getModInfo)
					.map(IModInfo::getVersion)
					.map(Object::toString)
					.orElse("N/A");

			event.getLeft().add(PREFIX + "(CLIENT) netColl: " + ManaNetworkHandler.instance.getAllCollectorsInWorld(world).size() + ", netPool: " + ManaNetworkHandler.instance.getAllPoolsInWorld(world).size() + ", rv: " + version);

			if (Minecraft.getInstance().isSingleplayer()) {
				RegistryKey<World> dim = Minecraft.getInstance().world.func_234923_W_();
				ResourceLocation dimName = dim.func_240901_a_();
				if (ServerLifecycleHooks.getCurrentServer() != null) {
					World serverWorld = ServerLifecycleHooks.getCurrentServer().getWorld(dim);
					event.getLeft().add(PREFIX + String.format("(INTEGRATED SERVER %s) netColl : %d, netPool: %d", dimName, ManaNetworkHandler.instance.getAllCollectorsInWorld(serverWorld).size(), ManaNetworkHandler.instance.getAllPoolsInWorld(serverWorld).size()));
				}
			}

			if (Screen.func_231172_r_() && Screen.func_231173_s_()) {
				event.getLeft().add(PREFIX + "Config Context");
				event.getLeft().add("  shaders.enabled: " + ConfigHandler.CLIENT.useShaders.get());

				GLCapabilities caps = GL.getCapabilities();
				event.getLeft().add(PREFIX + "OpenGL Context");
				event.getLeft().add("  GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
				event.getLeft().add("  GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
				event.getLeft().add("  GL_SHADING_LANGUAGE_VERSION: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
				event.getLeft().add("  GL_ARB_multitexture: " + caps.GL_ARB_multitexture);
				event.getLeft().add("  GL_ARB_texture_non_power_of_two: " + caps.GL_ARB_texture_non_power_of_two);
				event.getLeft().add("  OpenGL13: " + caps.OpenGL13);
			} else if (Minecraft.IS_RUNNING_ON_MAC) {
				event.getLeft().add(PREFIX + "SHIFT+CMD for context");
			} else {
				event.getLeft().add(PREFIX + "SHIFT+CTRL for context");
			}
		}
	}

}
