/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Apr 1, 2014, 6:21:48 PM (GMT)]
 */
package vazkii.botania.client.model;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.state.enums.PylonVariant;

import java.util.ArrayList;
import java.util.List;

public class ModelPylon implements IPylonModel {

	private IBakedModel manaCrystal;
	private IBakedModel manaRingsAndPanes;
	private IBakedModel manaGems;
	private IBakedModel naturaCrystal;
	private IBakedModel naturaRingsAndPanes;
	private IBakedModel naturaGems;
	private IBakedModel gaiaCrystal;
	private IBakedModel gaiaRingsAndPanes;
	private IBakedModel gaiaGems;

	public ModelPylon() {
		try {
			// Load the OBJ
			OBJModel model = (OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation("botania:models/block/pylon.obj"));

			// Apply the texture and flip the v's of the model
			IModel manaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon"))).process(ImmutableMap.of("flip-v", "true"));
			IModel naturaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon1"))).process(ImmutableMap.of("flip-v", "true"));
			IModel gaiaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon2"))).process(ImmutableMap.of("flip-v", "true"));

			// Hide necessary groups and bake
			VertexFormat format = Attributes.DEFAULT_BAKED_FORMAT;
			IModelState hideGroups = hideGroups(ImmutableList.of("Crystal_Ring", "Ring_Panel01", "Ring_Panel02", "Ring_Panel03", "Ring_Panel04",
					"Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04"));
			manaCrystal = manaModel.bake(hideGroups, format, ModelLoader.defaultTextureGetter());
			naturaCrystal = naturaModel.bake(hideGroups, format, ModelLoader.defaultTextureGetter());
			gaiaCrystal = gaiaModel.bake(hideGroups, format, ModelLoader.defaultTextureGetter());

			hideGroups = hideGroups(ImmutableList.of("Crystal", "Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04"));
			manaRingsAndPanes = manaModel.bake(hideGroups, format, ModelLoader.defaultTextureGetter());
			naturaRingsAndPanes = naturaModel.bake(hideGroups, format, ModelLoader.defaultTextureGetter());
			gaiaRingsAndPanes = gaiaModel.bake(hideGroups, format, ModelLoader.defaultTextureGetter());

			hideGroups = hideGroups(ImmutableList.of("Crystal", "Crystal_Ring", "Ring_Panel01", "Ring_Panel02", "Ring_Panel03", "Ring_Panel04"));
			manaGems = manaModel.bake(hideGroups, format, ModelLoader.defaultTextureGetter());
			naturaGems = naturaModel.bake(hideGroups, format, ModelLoader.defaultTextureGetter());
			gaiaGems = gaiaModel.bake(hideGroups, format, ModelLoader.defaultTextureGetter());
		} catch(Exception e) {
			throw new ReportedException(new CrashReport("Error making pylon submodels for TESR!", e));
		}
	}

	@Override
	public void renderCrystal(PylonVariant variant) {
		switch(variant) {
		case MANA:
			renderModel(manaCrystal);
			break;
		case NATURA:
			renderModel(naturaCrystal);
			break;
		case GAIA:
			renderModel(gaiaCrystal);
			break;
		}
	}

	@Override
	public void renderRing(PylonVariant variant) {
		GlStateManager.disableLighting();
		switch(variant) {
		case MANA:
			renderModel(manaRingsAndPanes);
			break;
		case NATURA:
			renderModel(naturaRingsAndPanes);
			break;
		case GAIA:
			renderModel(gaiaRingsAndPanes);
			break;
		}
		GlStateManager.enableLighting();
	}

	@Override
	public void renderGems(PylonVariant variant) {
		GlStateManager.disableLighting();
		switch(variant) {
		case MANA:
			renderModel(manaGems);
			break;
		case NATURA:
			renderModel(naturaGems);
			break;
		case GAIA:
			renderModel(gaiaGems);
			break;
		}
		GlStateManager.enableLighting();
	}

	private void renderModel(IBakedModel model) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for(BakedQuad bakedquad : model.getQuads(null, null, 0))
			LightUtil.renderQuadColor(worldrenderer, bakedquad, -1);
		tessellator.draw();
	}

	private IModelState hideGroups(List<String> groups) {
		return part -> {
			if (part.isPresent()) {
				List<String> parts = new ArrayList<>();
				Models.getParts(part.get()).forEachRemaining(parts::add);
				for (String s : parts) {
					if (groups.contains(s)) {
						// Hide it
						return Optional.of(TRSRTransformation.identity());
					}
				}
			}

			return Optional.absent();
		};
	}

}
