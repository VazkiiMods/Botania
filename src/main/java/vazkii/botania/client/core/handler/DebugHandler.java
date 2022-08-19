/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 21, 2014, 4:58:55 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;

import vazkii.botania.client.fx.ParticleRenderDispatcher;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.lib.LibMisc;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class DebugHandler {

	private static final String PREFIX = EnumChatFormatting.GREEN + "[Botania] " + EnumChatFormatting.RESET;

	@SubscribeEvent
	public void onDrawDebugText(RenderGameOverlayEvent.Text event) {
		World world = Minecraft.getMinecraft().theWorld;
		if(Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			event.left.add(null);
			String version = LibMisc.VERSION;
			if(version.contains("GRADLE"))
				version = "N/A";

			event.left.add(PREFIX + "pS: " + ParticleRenderDispatcher.sparkleFxCount + ", pFS: " + ParticleRenderDispatcher.fakeSparkleFxCount + ", pW: " + ParticleRenderDispatcher.wispFxCount + ", pDIW: " + ParticleRenderDispatcher.depthIgnoringWispFxCount + ", pLB: " + ParticleRenderDispatcher.lightningCount);
			event.left.add(PREFIX + "netColl: " + ManaNetworkHandler.instance.getAllCollectorsInWorld(world).size() + ", netPool: " + ManaNetworkHandler.instance.getAllPoolsInWorld(world).size() + ", rv: " + version);

			if(GuiScreen.isCtrlKeyDown() && GuiScreen.isShiftKeyDown()) {
				event.left.add(PREFIX + "Config Context");
				event.left.add("  shaders.enabled: " + ConfigHandler.useShaders);
				event.left.add("  shaders.secondaryUnit: " + ConfigHandler.glSecondaryTextureUnit);

				ContextCapabilities caps = GLContext.getCapabilities();
				event.left.add(PREFIX + "OpenGL Context");
				event.left.add("  GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
				event.left.add("  GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
				event.left.add("  GL_SHADING_LANGUAGE_VERSION: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
				event.left.add("  GL_MAX_TEXTURE_IMAGE_UNITS_ARB: " + GL11.glGetInteger(ARBFragmentShader.GL_MAX_TEXTURE_IMAGE_UNITS_ARB));
				event.left.add("  GL_ARB_multitexture: " + caps.GL_ARB_multitexture);
				event.left.add("  GL_ARB_texture_non_power_of_two: " + caps.GL_ARB_texture_non_power_of_two);
				event.left.add("  OpenGL13: " + caps.OpenGL13);
			} else if(Minecraft.isRunningOnMac)
				event.left.add(PREFIX + "SHIFT+CMD for context");
			else event.left.add(PREFIX + "SHIFT+CTRL for context");
		}
	}


}
