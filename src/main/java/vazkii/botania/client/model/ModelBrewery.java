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

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.render.tile.RenderTileBrewery;

public class ModelBrewery extends ModelBase {

	ModelRenderer Pole;
	ModelRenderer Top;
	ModelRenderer Bottom;
	ModelRenderer Plate;

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
		int plates = render.brewery.getSizeInventory() - 1;
		float deg = (float) time / 16F;
		float polerot = -deg * 25F;

		GL11.glTranslatef(0F, offset, 0F);
		GL11.glRotatef(polerot, 0F, 1F, 0F);
		if(render.brewery.getStackInSlot(0) != null) {
			GL11.glRotatef(180F, 1F, 0F, 0F);
			GL11.glTranslatef(-1F / 8F, -0.5F, 1F / 128F);
			render.renderItemStack(render.brewery.getStackInSlot(0));
			GL11.glTranslatef(1F / 8F, 0.5F, -1F / 128F);
			GL11.glRotatef(-180F, 1F, 0F, 0F);
		}

		Pole.render(f);
		Top.render(f);
		Bottom.render(f);
		GL11.glRotatef(-polerot, 0F, 1F, 0F);

		float degper = (float) (2F * Math.PI) / plates;
		for(int i = 0; i < plates; i++) {
			Plate.rotateAngleY = deg;
			float offset1 = (float) Math.sin(time / 20 + i * 40F) * 0.2F - 0.2F;
			if(time == -1)
				offset1 = 0F;

			GL11.glTranslatef(0F, offset1, 0F);
			if(render.brewery.getStackInSlot(i + 1) != null) {
				float rot = Plate.rotateAngleY * 180F / (float) Math.PI;
				float transX = 0.3125F;
				float transY = 1.06F;
				float transZ = 0.1245F;
				GL11.glRotatef(rot, 0F, 1F, 0F);
				GL11.glTranslatef(transX, transY, transZ);
				GL11.glRotatef(-90F, 1F, 0F, 0F);
				render.renderItemStack(render.brewery.getStackInSlot(i + 1));
				GL11.glRotatef(90F, 1F, 0F, 0F);
				GL11.glTranslatef(-transX, -transY, -transZ);
				GL11.glRotatef(-rot, 0F, 1F, 0F);
			}

			Plate.render(f);
			GL11.glTranslatef(0F, -offset1, 0F);

			deg += degper;
		}
		GL11.glTranslatef(0F, -offset, 0F);
	}

}
