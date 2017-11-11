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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import javax.annotation.Nullable;

public class RenderTileCorporeaIndex extends TileEntitySpecialRenderer<TileCorporeaIndex> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_CORPOREA_INDEX);
	final ModelEnderCrystal crystal = new ModelEnderCrystal(0F, false);
	public static boolean move = true;

	@Override
	public void render(@Nullable TileCorporeaIndex index, double x, double y, double z, float partticks, int digProgress, float unused) {
		move = index != null;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y, z + 0.5);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		float translation = move ? (float) ((Math.cos((index.ticksWithCloseby + (index.hasCloseby ? partticks : 0)) / 10F) * 0.5 + 0.5) * 0.25) : 0F;
		float rotation = move ? index.ticks * 2 + partticks : 0F;
		float scale = 0.6F;
		GlStateManager.scale(scale, scale, scale);
		crystal.render(null, 0F, rotation, translation, 0F, 0F, 1F / 16F);
		GlStateManager.scale(1F / scale, 1F / scale, 1F / scale);

		if(index != null && index.closeby > 0F) {
			float starScale = 0.02F;
			float starRadius = (float) TileCorporeaIndex.RADIUS * index.closeby + (index.closeby == 1F ? 0F : index.hasCloseby ? partticks : -partticks) * 0.2F;
			double rads = (index.ticksWithCloseby + partticks) * 2 * Math.PI / 180;
			double starX = Math.cos(rads) * starRadius;
			double starZ = Math.sin(rads) * starRadius;
			int color = 0xFF00FF;
			int seed = index.getPos().getX() ^ index.getPos().getY() ^ index.getPos().getZ();

			GlStateManager.translate(starX, 0.3, starZ);
			RenderHelper.renderStar(color, starScale, starScale, starScale, seed);
			GlStateManager.translate(-starX * 2, 0, -starZ * 2);
			RenderHelper.renderStar(color, starScale, starScale, starScale, seed);
			GlStateManager.translate(starX, 0, starZ);

			rads = -rads;
			starX = Math.cos(rads) * starRadius;
			starZ = Math.sin(rads) * starRadius;
			GlStateManager.translate(starX, 0, starZ);
			RenderHelper.renderStar(color, starScale, starScale, starScale, seed);
			GlStateManager.translate(-starX * 2, 0, -starZ * 2);
			RenderHelper.renderStar(color, starScale, starScale, starScale, seed);
			GlStateManager.translate(starX, 0, starZ);
		}
		GlStateManager.popMatrix();
	}

}
