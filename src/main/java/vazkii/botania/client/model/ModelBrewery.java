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

	final ModelRenderer Pole;
	final ModelRenderer Top;
	final ModelRenderer Bottom;
	final ModelRenderer Plate;

	public ModelBrewery() {
		textureWidth = 64;
		textureHeight = 32;

		Pole = new ModelRenderer(this, 0, 6);
		Pole.addBox(0F, 0F, 0F, 2, 10, 2);
		Pole.setRotationPoint(-1F, 10F, -1F);
		Pole.setTextureSize(64, 32);
		Top = new ModelRenderer(this, 18, 0);
		Top.addBox(0F, 0F, 0F, 4, 1, 4);
		Top.setRotationPoint(-2F, 9F, -2F);
		Top.setTextureSize(64, 32);
		Bottom = new ModelRenderer(this, 18, 7);
		Bottom.addBox(0F, 0F, 0F, 4, 1, 4);
		Bottom.setRotationPoint(-2F, 20F, -2F);
		Bottom.setTextureSize(64, 32);
		Plate = new ModelRenderer(this, 0, 0);
		Plate.addBox(5F, 0F, -2F, 4, 1, 4);
		Plate.setRotationPoint(0F, 17F, 0F);
		Plate.setTextureSize(64, 32);
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
		if(hasTile && render.brewery.getItemHandler().getStackInSlot(0) != null) {
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(0, -0.45, 0);
			//GlStateManager.translate(-1F / 8F, -0.5F, 1F / 128F);
			render.renderItemStack(render.brewery.getItemHandler().getStackInSlot(0));
			//GlStateManager.translate(1F / 8F, 0.5F, -1F / 128F);
			GlStateManager.translate(0, 0.45, 0);
			GlStateManager.rotate(-180F, 1F, 0F, 0F);
		}

		Pole.render(f);
		Top.render(f);
		Bottom.render(f);
		GlStateManager.rotate(-polerot, 0F, 1F, 0F);

		float degper = (float) (2F * Math.PI) / plates;
		for(int i = 0; i < plates; i++) {
			Plate.rotateAngleY = deg;
			float offset1 = (float) Math.sin(time / 20 + i * 40F) * 0.2F - 0.2F;
			if(time == -1)
				offset1 = 0F;

			GlStateManager.translate(0F, offset1, 0F);
			if(hasTile && render.brewery.getItemHandler().getStackInSlot(i + 1) != null) {
				float rot = Plate.rotateAngleY * 180F / (float) Math.PI;
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

			Plate.render(f);
			GlStateManager.translate(0F, -offset1, 0F);

			deg += degper;
		}
		GlStateManager.translate(0F, -offset, 0F);
	}

}
