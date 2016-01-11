/**
 * This class was created by <wiiv>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.client.model;

import java.util.ArrayList;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPump extends ModelBase{

	private ArrayList<ModelRenderer> parts = new ArrayList<>();

	private ArrayList<ModelRenderer> innerRing = new ArrayList<>();
	private ArrayList<ModelRenderer> outerRing = new ArrayList<>();

	public ModelPump() {
		textureWidth = 64;
		textureHeight = 32;

		ModelRenderer main = new ModelRenderer(this, 0, 0);
		main.addBox(-2.0F, -2.0F, -7.0F, 4, 4, 14);
		main.setRotationPoint(0.0F, 4.0F, 0.0F);

		for(float r = 0; r <= Math.PI * 2; r += Math.PI) {
			ModelRenderer side = new ModelRenderer(this, 22, 0);

			side.addBox(-4.0F, -4.0F, 7.0F, 8, 8, 1);
			side.setRotationPoint(0.0F, 0.0F, 0.0F);
			side.rotateAngleY = r;
			main.addChild(side);
		}

		for(float r = 0; r <= Math.PI * 2; r += Math.PI / 2) {
			ModelRenderer innerPlate = new ModelRenderer(this, 0, 18);
			ModelRenderer outerPlate = new ModelRenderer(this, 22, 18);

			innerPlate.addBox(-3.0F, -3.0F, -7.0F, 5, 1, 6);
			innerPlate.setRotationPoint(0.0F, 4.0F, 0.0F);
			innerPlate.rotateAngleZ = r;
			innerRing.add(innerPlate);
			parts.add(innerPlate);


			outerPlate.addBox(-4.0F, -4.0F, 3.0F, 7, 1, 4);
			outerPlate.setRotationPoint(0.0F, 4.0F, 0.0F);
			outerPlate.rotateAngleZ = r;
			outerRing.add(outerPlate);
			parts.add(outerPlate);
		}

		parts.add(main);

	}

	public void render(float ringPos) {
		for(ModelRenderer iRing : innerRing)
			iRing.rotationPointZ = ringPos;

		for(ModelRenderer part : parts)
			part.render(1F / 16F);
	}
}