/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 28, 2015, 5:16:17 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class ModelBellows extends ModelBase {

	final ModelRenderer Base;
	final ModelRenderer Top;
	final ModelRenderer Funnel;
	final ModelRenderer Pipe;
	final ModelRenderer Handle1;
	final ModelRenderer Handle2;
	final ModelRenderer Handle3;

	public ModelBellows() {
		textureWidth = 64;
		textureHeight = 32;

		Base = new ModelRenderer(this, 0, 0);
		Base.addBox(0F, 0F, 0F, 10, 2, 10);
		Base.setRotationPoint(-5F, 22F, -5F);
		Base.setTextureSize(64, 32);
		Base.mirror = true;
		Top = new ModelRenderer(this, 0, 14);
		Top.addBox(0F, 0F, 0F, 8, 1, 8);
		Top.setRotationPoint(-4F, 14F, -4F);
		Top.setTextureSize(64, 32);
		Top.mirror = true;
		Funnel = new ModelRenderer(this, 34, 14);
		Funnel.addBox(0F, 0F, 0F, 6, 7, 6);
		Funnel.setRotationPoint(0F, 0F, 0F);
		Funnel.setTextureSize(64, 32);
		Funnel.mirror = true;
		Pipe = new ModelRenderer(this, 43, 1);
		Pipe.addBox(0F, 0F, 0F, 2, 2, 3);
		Pipe.setRotationPoint(-1F, 22F, -8F);
		Pipe.setTextureSize(64, 32);
		Pipe.mirror = true;
		Handle1 = new ModelRenderer(this, 43, 8);
		Handle1.addBox(0F, 0F, -0.5F, 1, 2, 1);
		Handle1.setRotationPoint(-2F, 12F, 0F);
		Handle1.setTextureSize(64, 32);
		Handle1.mirror = true;
		Handle2 = new ModelRenderer(this, 48, 8);
		Handle2.addBox(1F, 0F, -0.5F, 2, 1, 1);
		Handle2.setRotationPoint(-2F, 12F, 0F);
		Handle2.setTextureSize(64, 32);
		Handle2.mirror = true;
		Handle3 = new ModelRenderer(this, 55, 8);
		Handle3.addBox(3F, 0F, -0.5F, 1, 2, 1);
		Handle3.setRotationPoint(-2F, 12F, 0F);
		Handle3.setTextureSize(64, 32);
		Handle3.mirror = true;
	}

	public void render(float fract) {
		float f5 = 1F / 16F;
		Base.render(f5);
		Pipe.render(f5);

		//float fract = Math.max(0.1F, (float) (Math.sin(((double) ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.5F);
		float mov = (1F - fract) * 0.5F;

		GlStateManager.translate(0F, mov, 0F);
		Top.render(f5);
		Handle1.render(f5);
		Handle2.render(f5);
		Handle3.render(f5);
		GlStateManager.translate(0F, -mov, 0F);

		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(-0.19F, -1.375F, -0.19F);
		GlStateManager.scale(1F, fract, 1F);
		Funnel.render(f5);
		GlStateManager.scale(1F, 1F / fract, 1F);
	}


}
