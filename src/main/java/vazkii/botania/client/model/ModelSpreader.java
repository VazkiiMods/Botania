/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 1:55:05 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelSpreader extends ModelBase {

	final ModelRenderer cubeSide1;
	final ModelRenderer cubeSide2;
	final ModelRenderer cubeSide3;
	final ModelRenderer cubeSide4;
	final ModelRenderer cubeSide5;
	final ModelRenderer cubeHole1;
	final ModelRenderer cubeHole2;
	final ModelRenderer cubeHole3;
	final ModelRenderer cubeHole4;
	final ModelRenderer cubeInside;

	public ModelSpreader() {
		textureWidth = 64;
		textureHeight = 32;

		cubeSide1 = new ModelRenderer(this, 0, 0);
		cubeSide1.addBox(0F, 0F, 0F, 14, 1, 14);
		cubeSide1.setRotationPoint(-7F, 22F, -7F);
		cubeSide1.setTextureSize(64, 32);
		cubeSide2 = new ModelRenderer(this, 0, 0);
		cubeSide2.addBox(0F, 0F, 0F, 14, 13, 1);
		cubeSide2.setRotationPoint(-7F, 9F, -7F);
		cubeSide2.setTextureSize(64, 32);
		cubeSide3 = new ModelRenderer(this, 0, 0);
		cubeSide3.addBox(0F, 0F, 0F, 1, 13, 13);
		cubeSide3.setRotationPoint(-7F, 9F, -6F);
		cubeSide3.setTextureSize(64, 32);
		cubeSide4 = new ModelRenderer(this, 0, 0);
		cubeSide4.addBox(0F, 0F, 0F, 1, 13, 13);
		cubeSide4.setRotationPoint(6F, 9F, -6F);
		cubeSide4.setTextureSize(64, 32);
		cubeSide5 = new ModelRenderer(this, 0, 0);
		cubeSide5.addBox(0F, 0F, 0F, 12, 1, 13);
		cubeSide5.setRotationPoint(-6F, 9F, -6F);
		cubeSide5.setTextureSize(64, 32);
		cubeHole1 = new ModelRenderer(this, 0, 0);
		cubeHole1.addBox(0F, 0F, 0F, 4, 12, 1);
		cubeHole1.setRotationPoint(2F, 10F, 6F);
		cubeHole1.setTextureSize(64, 32);
		cubeHole2 = new ModelRenderer(this, 0, 0);
		cubeHole2.addBox(0F, 0F, 0F, 4, 12, 1);
		cubeHole2.setRotationPoint(-6F, 10F, 6F);
		cubeHole2.setTextureSize(64, 32);
		cubeHole3 = new ModelRenderer(this, 0, 0);
		cubeHole3.addBox(0F, 0F, 0F, 4, 4, 1);
		cubeHole3.setRotationPoint(-2F, 18F, 6F);
		cubeHole3.setTextureSize(64, 32);
		cubeHole4 = new ModelRenderer(this, 0, 0);
		cubeHole4.addBox(0F, 0F, 0F, 4, 4, 1);
		cubeHole4.setRotationPoint(-2F, 10F, 6F);
		cubeHole4.setTextureSize(64, 32);
		cubeInside = new ModelRenderer(this, 30, 17);
		cubeInside.addBox(0F, 0F, 0F, 6, 6, 6);
		cubeInside.setRotationPoint(-3F, 13F, -3F);
		cubeInside.setTextureSize(64, 32);
	}

	public void render() {
		float f = 1F / 16F;
		cubeSide1.render(f);
		cubeSide2.render(f);
		cubeSide3.render(f);
		cubeSide4.render(f);
		cubeSide5.render(f);
		cubeHole1.render(f);
		cubeHole2.render(f);
		cubeHole3.render(f);
		cubeHole4.render(f);
	}

	public void renderCube() {
		float f = 1F / 16F;
		cubeInside.render(f);
	}
}