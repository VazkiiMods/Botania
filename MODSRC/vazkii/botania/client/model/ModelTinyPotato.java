/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 18, 2014, 7:55:34 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTinyPotato extends ModelBase {
	
	ModelRenderer potato;

	public ModelTinyPotato() {
		textureWidth = 64;
		textureHeight = 32;

		potato = new ModelRenderer(this, 0, 0);
		potato.addBox(0F, 0F, 0F, 4, 6, 4);
		potato.setRotationPoint(-2F, 18F, -2F);
		potato.setTextureSize(64, 32);
	}

	public void render() {
		potato.render(1F / 16F);
	}

}
