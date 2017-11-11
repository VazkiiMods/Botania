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

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import vazkii.botania.client.render.tile.RenderTileBrewery;

public class ModelBrewery extends ModelBase {

	final ModelRenderer top;
	final ModelRenderer pole;
	final ModelRenderer bottom;
	
	final ModelRenderer plate;

	public ModelBrewery() {
		
		textureWidth = 32;
		textureHeight = 16;
		
		top = new ModelRenderer(this, 8, 0);
        top.setRotationPoint(0.0F, 16.0F, 0.0F);
        top.addBox(-2.0F, -7.0F, -2.0F, 4, 1, 4, 0.0F);
		pole = new ModelRenderer(this, 0, 0);
        pole.setRotationPoint(0.0F, 16.0F, 0.0F);
        pole.addBox(-1.0F, -6.0F, -1.0F, 2, 10, 2, 0.0F);
        bottom = new ModelRenderer(this, 8, 5);
        bottom.setRotationPoint(0.0F, 16.0F, 0.0F);
        bottom.addBox(-2.0F, 4.0F, -2.0F, 4, 1, 4, 0.0F);
        
        plate = new ModelRenderer(this, 8, 5);
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

		GlStateManager.translate(0F, offset, 0F);
		GlStateManager.rotate(polerot, 0F, 1F, 0F);
		if(hasTile && !render.brewery.getItemHandler().getStackInSlot(0).isEmpty()) {
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(0, -0.45, 0);
			//GlStateManager.translate(-1F / 8F, -0.5F, 1F / 128F);
			render.renderItemStack(render.brewery.getItemHandler().getStackInSlot(0));
			//GlStateManager.translate(1F / 8F, 0.5F, -1F / 128F);
			GlStateManager.translate(0, 0.45, 0);
			GlStateManager.rotate(-180F, 1F, 0F, 0F);
		}

		pole.render(f);
		top.render(f);
		bottom.render(f);
		GlStateManager.rotate(-polerot, 0F, 1F, 0F);

		float degper = (float) (2F * Math.PI) / plates;
		for(int i = 0; i < plates; i++) {
			plate.rotateAngleY = deg;
			float offset1 = (float) Math.sin(time / 20 + i * 40F) * 0.2F - 0.2F;
			if(time == -1)
				offset1 = 0F;

			GlStateManager.translate(0F, offset1, 0F);
			if(hasTile && !render.brewery.getItemHandler().getStackInSlot(i + 1).isEmpty()) {
				float rot = plate.rotateAngleY * 180F / (float) Math.PI;
				float transX = 0.3125F;
				float transY = 1.06F;
				float transZ = 0.1245F;
				GlStateManager.rotate(rot, 0F, 1F, 0F);
				GlStateManager.translate(transX, transY, transZ);
				GlStateManager.rotate(-90F, 1F, 0F, 0F);
				GlStateManager.translate(0.125, 0.125, 0);
				render.renderItemStack(render.brewery.getItemHandler().getStackInSlot(i + 1));
				GlStateManager.translate(-0.125, -0.125, 0);
				GlStateManager.rotate(90F, 1F, 0F, 0F);
				GlStateManager.translate(-transX, -transY, -transZ);
				GlStateManager.rotate(-rot, 0F, 1F, 0F);
			}
			plate.render(f);
			GlStateManager.translate(0F, -offset1, 0F);

			deg += degper;
		}
		GlStateManager.translate(0F, -offset, 0F);
	}

}
