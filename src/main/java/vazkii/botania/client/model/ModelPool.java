/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 25, 2014, 1:53:32 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPool extends ModelBase {

	ModelRenderer base;
	ModelRenderer side1;
	ModelRenderer side2;
	ModelRenderer side3;
	ModelRenderer side4;

	public ModelPool() {
		textureWidth = 64;
		textureHeight = 32;

		base = new ModelRenderer(this, 0, 0);
		base.addBox(0F, 0F, 0F, 16, 1, 16);
		base.setRotationPoint(-8F, 23F, -8F);
		base.setTextureSize(64, 32);
		side1 = new ModelRenderer(this, 0, 0);
		side1.addBox(0F, 0F, 0F, 16, 7, 1);
		side1.setRotationPoint(-8F, 16F, 7F);
		side1.setTextureSize(64, 32);
		side2 = new ModelRenderer(this, 0, 0);
		side2.addBox(0F, 0F, 0F, 16, 7, 1);
		side2.setRotationPoint(-8F, 16F, -8F);
		side2.setTextureSize(64, 32);
		side3 = new ModelRenderer(this, 0, 0);
		side3.addBox(0F, 0F, 0F, 1, 7, 14);
		side3.setRotationPoint(-8F, 16F, -7F);
		side3.setTextureSize(64, 32);
		side4 = new ModelRenderer(this, 0, 0);
		side4.addBox(0F, 0F, 0F, 1, 7, 14);
		side4.setRotationPoint(7F, 16F, -7F);
		side4.setTextureSize(64, 32);
	}

	public void render() {
		float f = 1F / 16F;
		base.render(f);
		side1.render(f);
		side2.render(f);
		side3.render(f);
		side4.render(f);
	}
}