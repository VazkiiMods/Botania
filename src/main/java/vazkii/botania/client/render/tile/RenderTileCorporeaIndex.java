/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 15, 2015, 12:54:49 AM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

public class RenderTileCorporeaIndex extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_CORPOREA_INDEX);
	ModelEnderCrystal crystal = new ModelEnderCrystal(0F, false);
	public static boolean move = true;

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partticks) {
		TileCorporeaIndex index = (TileCorporeaIndex) tile;

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y, z + 0.5);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		float translation = move ? (float) ((Math.cos((index.ticksWithCloseby + (index.hasCloseby ? partticks : 0)) / 10F) * 0.5 + 0.5) * 0.25) : 0F;
		float rotation = move ? index.ticks * 2 + partticks : 0F;
		float scale = 0.6F;
		GL11.glScalef(scale, scale, scale);
		crystal.render(null, 0F, rotation, translation, 0F, 0F, 1F / 16F);
		GL11.glScalef(1F / scale, 1F / scale, 1F / scale);

		if(index.closeby > 0F) {
			float starScale = 0.02F;
			float starRadius = (float) TileCorporeaIndex.RADIUS * index.closeby + (index.closeby == 1F ? 0F : index.hasCloseby ? partticks : -partticks) * 0.2F;
			double rads = (index.ticksWithCloseby + partticks) * 2 * Math.PI / 180;
			double starX = Math.cos(rads) * starRadius;
			double starZ = Math.sin(rads) * starRadius;
			int color = 0xFF00FF;
			int seed = index.xCoord ^ index.yCoord ^ index.zCoord;

			GL11.glTranslated(starX, 0.3, starZ);
			RenderHelper.renderStar(color, starScale, starScale, starScale, seed);
			GL11.glTranslated(-starX * 2, 0, -starZ * 2);
			RenderHelper.renderStar(color, starScale, starScale, starScale, seed);
			GL11.glTranslated(starX, 0, starZ);

			rads = -rads;
			starX = Math.cos(rads) * starRadius;
			starZ = Math.sin(rads) * starRadius;
			GL11.glTranslated(starX, 0, starZ);
			RenderHelper.renderStar(color, starScale, starScale, starScale, seed);
			GL11.glTranslated(-starX * 2, 0, -starZ * 2);
			RenderHelper.renderStar(color, starScale, starScale, starScale, seed);
			GL11.glTranslated(starX, 0, starZ);
		}
		GL11.glPopMatrix();
	}

}
