/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 8, 2015, 4:44:23 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelCocoon;
import vazkii.botania.common.block.tile.TileCocoon;

public class RenderTileCocoon extends TileEntitySpecialRenderer<TileCocoon> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_COCOON);
	ModelCocoon model = new ModelCocoon();

	@Override
	public void renderTileEntityAt(TileCocoon cocoon, double d0, double d1, double d2, float f, int digProgress) {
		float rot = 0F;
		float modval = 60F - (float) cocoon.timePassed / (float) TileCocoon.TOTAL_TIME * 30F;
		if(cocoon.timePassed % modval < 10) {
			float mod = (cocoon.timePassed + f) % modval;
			float v = mod / 5 * (float) Math.PI * 2;
			rot = (float) Math.sin(v) * (float) Math.log(cocoon.timePassed + f);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.translate(0.5F, -0.5F - 3F / 16F, -0.5F + 1F / 16F);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		model.render();
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}
}
