/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 1, 2014, 6:21:48 PM (GMT)]
 */
package vazkii.botania.client.model;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import vazkii.botania.client.lib.LibResources;

public class ModelPylon implements IPylonModel {

	private IModelCustom model;

	public ModelPylon() {
		model = AdvancedModelLoader.loadModel(new ResourceLocation(LibResources.OBJ_MODEL_PYLON));
	}

	@Override
	public void renderCrystal() {
		model.renderPart("Crystal");
	}

	@Override
	public void renderRing() {
		model.renderAllExcept("Crystal", "Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04");
	}

	@Override
	public void renderGems() {
		for(int i = 1; i < 5; i++)
			model.renderPart("Ring_Gem0" + i);
	}
}
