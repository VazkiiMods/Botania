/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 18, 2014, 10:05:39 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import vazkii.botania.api.state.enums.PylonVariant;

public class ModelPylonOld extends ModelBase implements IPylonModel {

	final ModelRenderer crystal1;
	final ModelRenderer crystal2;
	final ModelRenderer crystal3;
	final ModelRenderer crystal4;
	final ModelRenderer crystal5;
	final ModelRenderer crystal6;
	final ModelRenderer crystal7;
	final ModelRenderer crystal8;
	final ModelRenderer outside1;
	final ModelRenderer outside2;
	final ModelRenderer outside3;
	final ModelRenderer outside4;
	final ModelRenderer outside5;
	final ModelRenderer outside6;
	final ModelRenderer outside7;
	final ModelRenderer outside8;

	public ModelPylonOld() {
		textureWidth = 64;
		textureHeight = 32;

		crystal1 = new ModelRenderer(this, 0, 0);
		crystal1.addBox(-1.5F, -7F, -1F, 3, 7, 2);
		crystal1.setRotationPoint(0F, 23F, 0F);
		crystal1.setTextureSize(256, 128);
		setRotation(crystal1, 0.1396263F, -0.418879F, 0F);
		crystal2 = new ModelRenderer(this, 0, 0);
		crystal2.addBox(-1.5F, -7F, -1F, 3, 7, 2);
		crystal2.setRotationPoint(0F, 23F, 0F);
		crystal2.setTextureSize(256, 128);
		setRotation(crystal2, -0.1396263F, 0.418879F, 0F);
		crystal3 = new ModelRenderer(this, 0, 0);
		crystal3.addBox(-1.5F, -7F, -1F, 3, 7, 2);
		crystal3.setRotationPoint(0F, 23F, 0F);
		crystal3.setTextureSize(256, 128);
		setRotation(crystal3, 0.1396263F, 0.418879F, 0F);
		crystal4 = new ModelRenderer(this, 0, 0);
		crystal4.addBox(-1.5F, -7F, -1F, 3, 7, 2);
		crystal4.setRotationPoint(0F, 23F, 0F);
		crystal4.setTextureSize(256, 128);
		setRotation(crystal4, -0.1396263F, -0.418879F, 0F);
		crystal5 = new ModelRenderer(this, 0, 0);
		crystal5.addBox(-1.5F, 0F, -1F, 3, 7, 2);
		crystal5.setRotationPoint(0F, 10F, 0F);
		crystal5.setTextureSize(256, 128);
		setRotation(crystal5, 0.1396263F, 0.418879F, 0F);
		crystal6 = new ModelRenderer(this, 0, 0);
		crystal6.addBox(-1.5F, 0F, -1F, 3, 7, 2);
		crystal6.setRotationPoint(0F, 10F, 0F);
		crystal6.setTextureSize(256, 128);
		setRotation(crystal6, 0.1396263F, -0.418879F, 0F);
		crystal7 = new ModelRenderer(this, 0, 0);
		crystal7.addBox(-1.5F, 0F, -1F, 3, 7, 2);
		crystal7.setRotationPoint(0F, 10F, 0F);
		crystal7.setTextureSize(256, 128);
		setRotation(crystal7, -0.1396263F, -0.418879F, 0F);
		crystal8 = new ModelRenderer(this, 0, 0);
		crystal8.addBox(-1.5F, 0F, -1F, 3, 7, 2);
		crystal8.setRotationPoint(0F, 10F, 0F);
		crystal8.setTextureSize(256, 128);
		setRotation(crystal8, -0.1396263F, 0.418879F, 0F);
		outside1 = new ModelRenderer(this, 17, 0);
		outside1.addBox(0F, -4F, -1.5F, 1, 8, 3);
		outside1.setRotationPoint(4F, 18F, 0F);
		outside1.setTextureSize(256, 128);
		setRotation(outside1, 0F, 0F, 0.1396263F);
		outside2 = new ModelRenderer(this, 17, 0);
		outside2.addBox(-1F, -4F, -1.5F, 1, 8, 3);
		outside2.setRotationPoint(-4F, 18F, 0F);
		outside2.setTextureSize(256, 128);
		setRotation(outside2, 0F, 0F, -0.1396263F);
		outside3 = new ModelRenderer(this, 26, 0);
		outside3.addBox(-1.5F, -3F, -1F, 3, 6, 1);
		outside3.setRotationPoint(0F, 18F, -4F);
		outside3.setTextureSize(256, 128);
		setRotation(outside3, 0.0698132F, 0F, 0F);
		outside4 = new ModelRenderer(this, 26, 0);
		outside4.addBox(-1.5F, -3F, 0F, 3, 6, 1);
		outside4.setRotationPoint(0F, 18F, 4F);
		outside4.setTextureSize(256, 128);
		setRotation(outside4, -0.0698132F, 0F, 0F);
		outside5 = new ModelRenderer(this, 27, 0);
		outside5.addBox(0F, 0F, -4F, 1, 2, 8);
		outside5.setRotationPoint(3F, 18F, 0F);
		outside5.setTextureSize(256, 128);
		setRotation(outside5, 0F, 0F, 0F);
		outside6 = new ModelRenderer(this, 27, 0);
		outside6.addBox(-1F, -1F, -4F, 1, 2, 8);
		outside6.setRotationPoint(-3F, 19F, 0F);
		outside6.setTextureSize(256, 128);
		setRotation(outside6, 0F, 0F, 0F);
		outside7 = new ModelRenderer(this, 17, 12);
		outside7.addBox(-3F, -1F, 0F, 6, 2, 1);
		outside7.setRotationPoint(0F, 19F, 3F);
		outside7.setTextureSize(256, 128);
		setRotation(outside7, 0F, 0F, 0F);
		outside8 = new ModelRenderer(this, 17, 12);
		outside8.addBox(-3F, -1F, -1F, 6, 2, 1);
		outside8.setRotationPoint(0F, 19F, -3F);
		outside8.setTextureSize(256, 128);
		setRotation(outside8, 0F, 0F, 0F);
	}

	@Override
	public void renderCrystal(PylonVariant variant) {
		float f = 1F / 16F;
		crystal1.render(f);
		crystal2.render(f);
		crystal3.render(f);
		crystal4.render(f);
		crystal5.render(f);
		crystal6.render(f);
		crystal7.render(f);
		crystal8.render(f);
	}

	@Override
	public void renderRing(PylonVariant variant) {
		float f = 1F / 16F;
		outside1.render(f);
		outside2.render(f);
		outside3.render(f);
		outside4.render(f);
		outside5.render(f);
		outside6.render(f);
		outside7.render(f);
		outside8.render(f);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void renderGems(PylonVariant variant) {}

}