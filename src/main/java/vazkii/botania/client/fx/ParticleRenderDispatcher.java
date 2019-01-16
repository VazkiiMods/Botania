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
import org.lwjgl.opengl.GL11;

public final class ParticleRenderDispatcher {

	public static int wispFxCount = 0;
	public static int depthIgnoringWispFxCount = 0;
	public static int sparkleFxCount = 0;
	public static int fakeSparkleFxCount = 0;
	public static int lightningCount = 0;

	// Called from LightningHandler.onRenderWorldLast since that was
	// already registered. /shrug
	public static void dispatch() {
		Tessellator tessellator = Tessellator.getInstance();

		Profiler profiler = Minecraft.getMinecraft().profiler;

		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		GlStateManager.disableLighting();

		profiler.startSection("sparkle");
		FXSparkle.dispatchQueuedRenders(tessellator);
		profiler.endStartSection("wisp");
		FXWisp.dispatchQueuedRenders(tessellator);
		profiler.endSection();

		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GL11.glPopAttrib();
	}

}
