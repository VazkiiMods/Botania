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

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import vazkii.botania.client.fx.ParticleRenderDispatcher;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.lib.LibMisc;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public final class DebugHandler {

	private DebugHandler() {}

	private static final String PREFIX = TextFormatting.GREEN + "[Botania] " + TextFormatting.RESET;

	@SubscribeEvent
	public static void onDrawDebugText(RenderGameOverlayEvent.Text event) {
		World world = Minecraft.getMinecraft().world;
		if(ConfigHandler.debugInfo && Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			event.getLeft().add("");
			String version = LibMisc.VERSION;
			if(version.contains("GRADLE"))
				version = "N/A";

			event.getLeft().add(PREFIX + "pS: " + ParticleRenderDispatcher.sparkleFxCount + ", pFS: " + ParticleRenderDispatcher.fakeSparkleFxCount + ", pW: " + ParticleRenderDispatcher.wispFxCount + ", pDIW: " + ParticleRenderDispatcher.depthIgnoringWispFxCount + ", pLB: " + ParticleRenderDispatcher.lightningCount);
			event.getLeft().add(PREFIX + "(CLIENT) netColl: " + ManaNetworkHandler.instance.getAllCollectorsInWorld(world).size() + ", netPool: " + ManaNetworkHandler.instance.getAllPoolsInWorld(world).size() + ", rv: " + version);

			if (Minecraft.getMinecraft().isSingleplayer()) {
				UUID id = Minecraft.getMinecraft().player.getUniqueID();
				Entity ent = Minecraft.getMinecraft().getIntegratedServer().getEntityFromUuid(id);
				if (ent != null) {
					World serverWorld = Minecraft.getMinecraft().getIntegratedServer().getEntityFromUuid(id).world;
					event.getLeft().add(PREFIX + String.format("(INTEGRATED SERVER DIM %d) netColl : %d, netPool: %d", serverWorld.provider.getDimension(), ManaNetworkHandler.instance.getAllCollectorsInWorld(serverWorld).size(), ManaNetworkHandler.instance.getAllPoolsInWorld(serverWorld).size()));
				}
			}

			if(GuiScreen.isCtrlKeyDown() && GuiScreen.isShiftKeyDown()) {
				event.getLeft().add(PREFIX + "Config Context");
				event.getLeft().add("  shaders.enabled: " + ConfigHandler.useShaders);
				event.getLeft().add("  shaders.secondaryUnit: " + ConfigHandler.glSecondaryTextureUnit);

				ContextCapabilities caps = GLContext.getCapabilities();
				event.getLeft().add(PREFIX + "OpenGL Context");
				event.getLeft().add("  GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
				event.getLeft().add("  GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
				event.getLeft().add("  GL_SHADING_LANGUAGE_VERSION: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
				event.getLeft().add("  GL_MAX_TEXTURE_IMAGE_UNITS_ARB: " + GL11.glGetInteger(ARBFragmentShader.GL_MAX_TEXTURE_IMAGE_UNITS_ARB));
				event.getLeft().add("  GL_ARB_multitexture: " + caps.GL_ARB_multitexture);
				event.getLeft().add("  GL_ARB_texture_non_power_of_two: " + caps.GL_ARB_texture_non_power_of_two);
				event.getLeft().add("  OpenGL13: " + caps.OpenGL13);

				if (Minecraft.getMinecraft().objectMouseOver != null
						&& Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null) {
					BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
					IBlockState state = world.getBlockState(pos);
					state = state.getActualState(world, pos);
					state = state.getBlock().getExtendedState(state, world, pos);
					if (state instanceof IExtendedBlockState) {
						try {
							for (Map.Entry<IUnlistedProperty<?>, Optional<?>> e : ((IExtendedBlockState) state).getUnlistedProperties().entrySet()) {
								event.getRight().add(TextFormatting.LIGHT_PURPLE + e.getKey().getName() + ": " + TextFormatting.RESET + e.getValue().orElse(null));
							}
						} catch (Throwable t) {
							event.getRight().add("Error getting extended state");
						}
					}
				}
			} else if(Minecraft.IS_RUNNING_ON_MAC)
				event.getLeft().add(PREFIX + "SHIFT+CMD for context");
			else event.getLeft().add(PREFIX + "SHIFT+CTRL for context");
		}
	}


}
