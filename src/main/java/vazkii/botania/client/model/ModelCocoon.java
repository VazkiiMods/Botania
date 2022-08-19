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

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCocoon extends ModelBase {

	public ModelRenderer shape;

	public ModelCocoon() {
		textureWidth = 64;
		textureHeight = 32;
		shape = new ModelRenderer(this, 0, 0);
		shape.setRotationPoint(0.0F, 22.0F, 0.0F);
		shape.addBox(-5.0F, -8.0F, -7.0F, 10, 10, 14, 0.0F);
	}

	public void render() {
		shape.render(1F / 16F);
	}

}
