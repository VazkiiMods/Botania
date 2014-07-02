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

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class ParticleRenderDispatcher {

	// Called from LightningHandler.onRenderWorldLast since that was
	// already registered. /shrug
	public static void dispatch() {
		Tessellator tessellator = Tessellator.instance;
		
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		FXSparkle.dispatchQueuedRenders(tessellator);
		FXWisp.dispatchQueuedRenders(tessellator);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}
	
}
