/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 21, 2014, 7:56:15 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelAltar extends ModelBase {

	ModelRenderer gobletBase;
	ModelRenderer gobletBase2;
	ModelRenderer gobletBase3;
	ModelRenderer gobletStand;
	ModelRenderer kelkBase;
	ModelRenderer kelkBase2;
	ModelRenderer kelkWall1;
	ModelRenderer kelkWall2;
	ModelRenderer kelkWall3;
	ModelRenderer kelkWall4;

	public ModelAltar() {
		textureWidth = 256;
		textureHeight = 128;

		gobletBase = new ModelRenderer(this, 0, 0);
		gobletBase.addBox(0F, 0F, 0F, 16, 2, 16);
		gobletBase.setRotationPoint(-8F, 22F, -8F);
		gobletBase.setTextureSize(256, 128);
		gobletBase2 = new ModelRenderer(this, 0, 18);
		gobletBase2.addBox(0F, 0F, 0F, 14, 1, 14);
		gobletBase2.setRotationPoint(-7F, 21F, -7F);
		gobletBase2.setTextureSize(256, 128);
		gobletStand = new ModelRenderer(this, 68, 0);
		gobletStand.addBox(0F, 0F, 0F, 6, 8, 6);
		gobletStand.setRotationPoint(-3F, 12F, -3F);
		gobletStand.setTextureSize(256, 128);
		gobletBase3 = new ModelRenderer(this, 0, 33);
		gobletBase3.addBox(0F, 0F, 0F, 12, 1, 12);
		gobletBase3.setRotationPoint(-6F, 20F, -6F);
		gobletBase3.setTextureSize(256, 128);
		kelkBase = new ModelRenderer(this, 72, 45);
		kelkBase.addBox(0F, 0F, 0F, 8, 1, 8);
		kelkBase.setRotationPoint(-4F, 11F, -4F);
		kelkBase.setTextureSize(256, 128);
		kelkBase2 = new ModelRenderer(this, 72, 34);
		kelkBase2.addBox(0F, 0F, 0F, 10, 1, 10);
		kelkBase2.setRotationPoint(-5F, 10F, -5F);
		kelkBase2.setTextureSize(256, 128);
		kelkWall1 = new ModelRenderer(this, 72, 18);
		kelkWall1.addBox(0F, 0F, 0F, 1, 6, 10);
		kelkWall1.setRotationPoint(5F, 4F, -5F);
		kelkWall1.setTextureSize(256, 128);
		kelkWall2 = new ModelRenderer(this, 72, 18);
		kelkWall2.addBox(0F, 0F, 0F, 1, 6, 10);
		kelkWall2.setRotationPoint(-6F, 4F, -5F);
		kelkWall2.setTextureSize(256, 128);
		kelkWall3 = new ModelRenderer(this, 94, 18);
		kelkWall3.addBox(0F, 0F, 0F, 10, 6, 1);
		kelkWall3.setRotationPoint(-5F, 4F, 5F);
		kelkWall3.setTextureSize(256, 128);
		kelkWall4 = new ModelRenderer(this, 94, 18);
		kelkWall4.addBox(0F, 0F, 0F, 10, 6, 1);
		kelkWall4.setRotationPoint(-5F, 4F, -6F);
		kelkWall4.setTextureSize(256, 128);
	}

	public void render() {
		float f = 1F / 16F;
		gobletBase.render(f);
		gobletBase2.render(f);
		gobletBase3.render(f);
		gobletStand.render(f);
		kelkBase.render(f);
		kelkBase2.render(f);
		kelkWall1.render(f);
		kelkWall2.render(f);
		kelkWall3.render(f);
		kelkWall4.render(f);
	}
}
