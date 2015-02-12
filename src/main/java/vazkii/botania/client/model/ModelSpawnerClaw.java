/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 23, 2014, 5:46:38 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelSpawnerClaw extends ModelBase {
	ModelRenderer Plate;
	ModelRenderer Claw1;
	ModelRenderer Claw2;
	ModelRenderer Claw3;
	ModelRenderer Claw4;
	ModelRenderer Claw5;
	ModelRenderer Claw6;
	ModelRenderer Claw7;
	ModelRenderer Claw8;

	public ModelSpawnerClaw() {
		textureWidth = 64;
		textureHeight = 32;

		Plate = new ModelRenderer(this, 0, 0);
		Plate.addBox(0F, 0F, 0F, 12, 1, 12);
		Plate.setRotationPoint(-6F, 23F, -6F);
		Plate.setTextureSize(64, 32);
		Claw1 = new ModelRenderer(this, 0, 14);
		Claw1.addBox(0F, 0F, 0F, 2, 1, 3);
		Claw1.setRotationPoint(-1F, 23F, 6F);
		Claw1.setTextureSize(64, 32);
		Claw2 = new ModelRenderer(this, 11, 14);
		Claw2.addBox(0F, 0F, 0F, 2, 1, 3);
		Claw2.setRotationPoint(-1F, 23F, -9F);
		Claw2.setTextureSize(64, 32);
		Claw3 = new ModelRenderer(this, 0, 19);
		Claw3.addBox(0F, 0F, 0F, 3, 1, 2);
		Claw3.setRotationPoint(-9F, 23F, -1F);
		Claw3.setTextureSize(64, 32);
		Claw4 = new ModelRenderer(this, 11, 19);
		Claw4.addBox(0F, 0F, 0F, 3, 1, 2);
		Claw4.setRotationPoint(6F, 23F, -1F);
		Claw4.setTextureSize(64, 32);
		Claw5 = new ModelRenderer(this, 23, 16);
		Claw5.addBox(0F, 0F, 0F, 2, 5, 1);
		Claw5.setRotationPoint(-1F, 24F, 8F);
		Claw5.setTextureSize(64, 32);
		Claw6 = new ModelRenderer(this, 30, 16);
		Claw6.addBox(0F, 0F, 0F, 1, 5, 2);
		Claw6.setRotationPoint(8F, 24F, -1F);
		Claw6.setTextureSize(64, 32);
		Claw7 = new ModelRenderer(this, 37, 16);
		Claw7.addBox(0F, 0F, 0F, 2, 5, 1);
		Claw7.setRotationPoint(-1F, 24F, -9F);
		Claw7.setTextureSize(64, 32);
		Claw8 = new ModelRenderer(this, 44, 16);
		Claw8.addBox(0F, 0F, 0F, 1, 5, 2);
		Claw8.setRotationPoint(-9F, 24F, -1F);
		Claw8.setTextureSize(64, 32);
	}

	public void render() {
		float f5 = 1F / 16F;
		Plate.render(f5);
		Claw1.render(f5);
		Claw2.render(f5);
		Claw3.render(f5);
		Claw4.render(f5);
		Claw5.render(f5);
		Claw6.render(f5);
		Claw7.render(f5);
		Claw8.render(f5);
	}

}
