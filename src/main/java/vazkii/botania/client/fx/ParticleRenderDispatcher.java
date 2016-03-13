/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 2, 2014, 12:12:45 AM (GMT)]
 */
package vazkii.botania.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.profiler.Profiler;

import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;
import vazkii.botania.client.core.handler.ClientMethodHandles;
import vazkii.botania.client.core.handler.ClientTickHandler;

public final class ParticleRenderDispatcher {

	public static int wispFxCount = 0;
	public static int depthIgnoringWispFxCount = 0;
	public static int sparkleFxCount = 0;
	public static int fakeSparkleFxCount = 0;
	public static int lightningCount = 0;

	// Called from LightningHandler.onRenderWorldLast since that was
	// already registered. /shrug
	public static void dispatch() {
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator tessellator = Tessellator.getInstance();

		float fovModifier, farPlane;

		try {
			fovModifier = ((float) ClientMethodHandles.getFOVModifier.invokeExact(mc.entityRenderer, ClientTickHandler.partialTicks, true));
			farPlane = ((float) ClientMethodHandles.farPlaneDistance_getter.invokeExact(mc.entityRenderer));
		} catch (Throwable t) {
			return;
		}

		Profiler profiler = mc.mcProfiler;

		GL11.glPushAttrib(GL11.GL_LIGHTING);
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		GlStateManager.disableLighting();

		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.loadIdentity();
		Project.gluPerspective(fovModifier, mc.displayWidth / mc.displayHeight, 0.05F, farPlane * 12); // Extend far plane so starfield and beacons work at lower render dists

		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();

		profiler.startSection("sparkle");
		FXSparkle.dispatchQueuedRenders(tessellator);
		profiler.endStartSection("wisp");
		FXWisp.dispatchQueuedRenders(tessellator);
		profiler.endSection();

		GlStateManager.popMatrix();

		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.loadIdentity();
		Project.gluPerspective(fovModifier, mc.displayWidth / mc.displayHeight, 0.05F, farPlane * MathHelper.SQRT_2);

		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GL11.glPopAttrib();
	}

}
