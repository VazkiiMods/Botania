/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 31, 2014, 4:33:55 PM (GMT)]
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.entity.model.RendererModel;
import vazkii.botania.client.render.tile.RenderTileBrewery;

public class ModelBrewery extends Model {

	final RendererModel top;
	final RendererModel pole;
	final RendererModel bottom;
	
	final RendererModel plate;

	public ModelBrewery() {
		
		textureWidth = 32;
		textureHeight = 16;
		
		top = new RendererModel(this, 8, 0);
        top.setRotationPoint(0.0F, 16.0F, 0.0F);
        top.addBox(-2.0F, -7.0F, -2.0F, 4, 1, 4, 0.0F);
		pole = new RendererModel(this, 0, 0);
        pole.setRotationPoint(0.0F, 16.0F, 0.0F);
        pole.addBox(-1.0F, -6.0F, -1.0F, 2, 10, 2, 0.0F);
        bottom = new RendererModel(this, 8, 5);
        bottom.setRotationPoint(0.0F, 16.0F, 0.0F);
        bottom.addBox(-2.0F, 4.0F, -2.0F, 4, 1, 4, 0.0F);
        
        plate = new RendererModel(this, 8, 5);
        plate.setRotationPoint(0.0F, 17.0F, 0.0F);
        plate.addBox(5.0F, 0.0F, -2.0F, 4, 1, 4, 0.0F);
        
	}

	public void render(RenderTileBrewery render, double time) {
		float f = 1F / 16F;

		float offset = (float) Math.sin(time / 40) * 0.1F + 0.05F;
		boolean hasTile = render.brewery != null;
		int plates = hasTile ? render.brewery.getSizeInventory() - 1 : 7;
		float deg = (float) time / 16F;
		float polerot = -deg * 25F;

		GlStateManager.translatef(0F, offset, 0F);
		GlStateManager.rotatef(polerot, 0F, 1F, 0F);
		if(hasTile && !render.brewery.getItemHandler().getStackInSlot(0).isEmpty()) {
			GlStateManager.rotatef(180F, 1F, 0F, 0F);
			GlStateManager.translatef(0, -0.45F, 0);
			//GlStateManager.translate(-1F / 8F, -0.5F, 1F / 128F);
			render.renderItemStack(render.brewery.getItemHandler().getStackInSlot(0));
			//GlStateManager.translate(1F / 8F, 0.5F, -1F / 128F);
			GlStateManager.translatef(0, 0.45F, 0);
			GlStateManager.rotatef(-180F, 1F, 0F, 0F);
		}

		pole.render(f);
		top.render(f);
		bottom.render(f);
		GlStateManager.rotatef(-polerot, 0F, 1F, 0F);

		float degper = (float) (2F * Math.PI) / plates;
		for(int i = 0; i < plates; i++) {
			plate.rotateAngleY = deg;
			float offset1 = (float) Math.sin(time / 20 + i * 40F) * 0.2F - 0.2F;
			if(time == -1)
				offset1 = 0F;

			GlStateManager.translatef(0F, offset1, 0F);
			if(hasTile && !render.brewery.getItemHandler().getStackInSlot(i + 1).isEmpty()) {
				float rot = plate.rotateAngleY * 180F / (float) Math.PI;
				float transX = 0.3125F;
				float transY = 1.06F;
				float transZ = 0.1245F;
				GlStateManager.rotatef(rot, 0F, 1F, 0F);
				GlStateManager.translatef(transX, transY, transZ);
				GlStateManager.rotatef(-90F, 1F, 0F, 0F);
				GlStateManager.translatef(0.125F, 0.125F, 0);
				render.renderItemStack(render.brewery.getItemHandler().getStackInSlot(i + 1));
				GlStateManager.translatef(-0.125F, -0.125F, 0);
				GlStateManager.rotatef(90F, 1F, 0F, 0F);
				GlStateManager.translatef(-transX, -transY, -transZ);
				GlStateManager.rotatef(-rot, 0F, 1F, 0F);
			}
			plate.render(f);
			GlStateManager.translatef(0F, -offset1, 0F);

			deg += degper;
		}
		GlStateManager.translatef(0F, -offset, 0F);
	}

}
