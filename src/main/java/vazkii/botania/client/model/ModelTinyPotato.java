/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 18, 2014, 7:55:34 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTinyPotato extends Model {

	final ModelRenderer potato;

	public ModelTinyPotato() {
		textureWidth = 16;
		textureHeight = 16;

		potato = new ModelRenderer(this, 0, 0);
		potato.addCuboid(0F, 0F, 0F, 4, 6, 4);
		potato.setRotationPoint(-2F, 18F, -2F);
		potato.setTextureSize(64, 32);
	}

	public void render() {
		potato.render(1F / 16F);
	}

}
