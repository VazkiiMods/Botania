/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
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
		gobletBase.setTextureSize(256, 128);
		gobletBase2 = new ModelRenderer(this, 0, 18);
		gobletBase2.addBox(0F, 0F, 0F, 14, 1, 14);
		gobletBase2.setTextureSize(256, 128);
		gobletStand = new ModelRenderer(this, 68, 0);
		gobletStand.addBox(0F, 0F, 0F, 6, 8, 6);
		gobletStand.setTextureSize(256, 128);
		gobletBase3 = new ModelRenderer(this, 0, 33);
		gobletBase3.addBox(0F, 0F, 0F, 12, 1, 12);
		gobletBase3.setTextureSize(256, 128);
		kelkBase = new ModelRenderer(this, 72, 45);
		kelkBase.addBox(0F, 0F, 0F, 8, 1, 8);
		kelkBase.setTextureSize(256, 128);
		kelkBase2 = new ModelRenderer(this, 72, 34);
		kelkBase2.addBox(0F, 0F, 0F, 10, 1, 10);
		kelkBase2.setTextureSize(256, 128);
		kelkWall1 = new ModelRenderer(this, 72, 18);
		kelkWall1.addBox(0F, 0F, 0F, 1, 6, 10);
		kelkWall1.setTextureSize(256, 128);
		kelkWall2 = new ModelRenderer(this, 72, 18);
		kelkWall2.addBox(0F, 0F, 0F, 1, 6, 10);
		kelkWall2.setTextureSize(256, 128);
		kelkWall3 = new ModelRenderer(this, 94, 18);
		kelkWall3.addBox(0F, 0F, 0F, 10, 6, 1);
		kelkWall3.setTextureSize(256, 128);
		kelkWall4 = new ModelRenderer(this, 94, 18);
		kelkWall4.addBox(0F, 0F, 0F, 10, 6, 1);
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
