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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.state.enums.PylonVariant;

import java.util.List;
import java.util.function.Function;

public class ModelPylon implements IPylonModel {

	private static final Function<ResourceLocation, TextureAtlasSprite> TEXTUREGETTER = input -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
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
			OBJModel model = ((OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation("botania:models/block/pylon.obj")));

			// Apply the texture and flip the v's of the model
			IModel manaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon"))).process(ImmutableMap.of("flip-v", "true"));
			IModel naturaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon1"))).process(ImmutableMap.of("flip-v", "true"));
			IModel gaiaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon2"))).process(ImmutableMap.of("flip-v", "true"));

			// All groups are off by default
			// (OBJState constructor is derp, so the boolean condition is inverted - pass false to turn on a group)

			// Turn on "Crystal"
			manaCrystal = manaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER::apply);
			naturaCrystal = naturaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER::apply);
			gaiaCrystal = gaiaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER::apply);

			// Turn on "Crystal_Ring", and "Ring_Panel0x"
			manaRingsAndPanes = manaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal_Ring", "Ring_Panel01", "Ring_Panel02", "Ring_Panel03", "Ring_Panel04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER::apply);
			naturaRingsAndPanes = naturaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal_Ring", "Ring_Panel01", "Ring_Panel02", "Ring_Panel03", "Ring_Panel04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER::apply);
			gaiaRingsAndPanes = gaiaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal_Ring", "Ring_Panel01", "Ring_Panel02", "Ring_Panel03", "Ring_Panel04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER::apply);

			// Turn on "Ring_Gem0x"
			manaGems = manaModel.bake(new OBJModel.OBJState(ImmutableList.of("Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER::apply);
			naturaGems = naturaModel.bake(new OBJModel.OBJState(ImmutableList.of("Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER::apply);
			gaiaGems = gaiaModel.bake(new OBJModel.OBJState(ImmutableList.of("Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER::apply);
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
		renderModel(model, -1);
	}

	private void renderModel(IBakedModel model, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		this.renderQuads(worldrenderer, model.getQuads(null, null, 0), color);
		tessellator.draw();
	}

	private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color) {
		for(BakedQuad bakedquad : quads)
			LightUtil.renderQuadColor(renderer, bakedquad, color);
	}

}
