/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 8, 2014, 10:07:54 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelMiniIsland extends ModelBase {

	ModelRenderer island;

	public ModelMiniIsland() {
		textureWidth = 64;
		textureHeight = 32;

		setTextureOffset("island.Shape0", 0, 0);
		setTextureOffset("island.Shape1-1", 8, 0);
		setTextureOffset("island.Shape1-2", 16, 0);
		setTextureOffset("island.Shape1-3", 24, 0);
		setTextureOffset("island.Shape1-4", 32, 0);
		setTextureOffset("island.Shape2-1", 8, 6);
		setTextureOffset("island.Shape2-2", 16, 6);
		setTextureOffset("island.Shape2-3", 24, 6);
		setTextureOffset("island.Shape2-4", 32, 6);
		setTextureOffset("island.Shape2-5", 8, 11);
		setTextureOffset("island.Shape2-6", 16, 11);
		setTextureOffset("island.Shape2-7", 24, 11);
		setTextureOffset("island.Shape2-8", 32, 11);
		setTextureOffset("island.Shape3-1", 8, 16);
		setTextureOffset("island.Shape3-2", 16, 16);
		setTextureOffset("island.Shape3-3", 24, 16);
		setTextureOffset("island.Shape3-4", 32, 16);
		setTextureOffset("island.Shape3-5", 8, 20);
		setTextureOffset("island.Shape3-6", 16, 20);
		setTextureOffset("island.Shape3-7", 24, 20);
		setTextureOffset("island.Shape3-8", 32, 20);
		setTextureOffset("island.Shape4-1", 8, 24);
		setTextureOffset("island.Shape4-2", 16, 24);
		setTextureOffset("island.Shape4-3", 24, 24);
		setTextureOffset("island.Shape4-4", 32, 24);

		island = new ModelRenderer(this, "island");
		island.setRotationPoint(0F, 16F, 0F);
		//		island.mirror = true;
		island.addBox("Shape0", -1F, 0F, -1F, 2, 5, 2);
		island.addBox("Shape1-1", -1F, 0F, -3F, 2, 4, 2);
		island.addBox("Shape1-2", 1F, 0F, -1F, 2, 4, 2);
		island.addBox("Shape1-3", -1F, 0F, 1F, 2, 4, 2);
		island.addBox("Shape1-4", -3F, 0F, -1F, 2, 4, 2);
		island.addBox("Shape2-1", -1F, 0F, -5F, 2, 3, 2);
		island.addBox("Shape2-2", 3F, 0F, -1F, 2, 3, 2);
		island.addBox("Shape2-3", -1F, 0F, 3F, 2, 3, 2);
		island.addBox("Shape2-4", -5F, 0F, -1F, 2, 3, 2);
		island.addBox("Shape2-5", 1F, 0F, -3F, 2, 3, 2);
		island.addBox("Shape2-6", 1F, 0F, 1F, 2, 3, 2);
		island.addBox("Shape2-7", -3F, 0F, 1F, 2, 3, 2);
		island.addBox("Shape2-8", -3F, 0F, -3F, 2, 3, 2);
		island.addBox("Shape3-1", 1F, 0F, -5F, 2, 2, 2);
		island.addBox("Shape3-2", 3F, 0F, 1F, 2, 2, 2);
		island.addBox("Shape3-3", -3F, 0F, 3F, 2, 2, 2);
		island.addBox("Shape3-4", -5F, 0F, -3F, 2, 2, 2);
		island.addBox("Shape3-5", 3F, 0F, -3F, 2, 2, 2);
		island.addBox("Shape3-6", 1F, 0F, 3F, 2, 2, 2);
		island.addBox("Shape3-7", -5F, 0F, 1F, 2, 2, 2);
		island.addBox("Shape3-8", -3F, 0F, -5F, 2, 2, 2);
		island.addBox("Shape4-1", 3F, 0F, -5F, 2, 1, 2);
		island.addBox("Shape4-2", 3F, 0F, 3F, 2, 1, 2);
		island.addBox("Shape4-3", -5F, 0F, 3F, 2, 1, 2);
		island.addBox("Shape4-4", -5F, 0F, -5F, 2, 1, 2);
	}

	public void render() {
		island.render(1F / 16F);
	}

}
