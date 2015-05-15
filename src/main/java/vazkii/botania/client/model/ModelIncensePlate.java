/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 15, 2015, 4:17:37 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelIncensePlate extends ModelBase {

	ModelRenderer Plate;
	ModelRenderer High;

	public ModelIncensePlate() {
		textureWidth = 64;
		textureHeight = 32;

		Plate = new ModelRenderer(this, 0, 0);
		Plate.addBox(0F, 0F, 0F, 12, 1, 4);
		Plate.setRotationPoint(-6F, 23F, -2F);
		Plate.setTextureSize(64, 32);
		Plate.mirror = true;
		High = new ModelRenderer(this, 0, 6);
		High.addBox(0F, 0F, 0F, 3, 1, 4);
		High.setRotationPoint(-6F, 22F, -2F);
		High.setTextureSize(64, 32);
		High.mirror = true;
	}

	public void render() {
		float f5 = 1F / 16F;
		Plate.render(f5);
		High.render(f5);
	}

}
