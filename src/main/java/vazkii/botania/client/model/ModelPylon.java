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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.state.enums.PylonVariant;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ModelPylon implements IPylonModel {

	private IFlexibleBakedModel manaCrystal;
	private IFlexibleBakedModel manaRingsAndPanes;
	private IFlexibleBakedModel manaGems;

	private IFlexibleBakedModel naturaCrystal;
	private IFlexibleBakedModel naturaRingsAndPanes;
	private IFlexibleBakedModel naturaGems;

	private IFlexibleBakedModel gaiaCrystal;
	private IFlexibleBakedModel gaiaRingsAndPanes;
	private IFlexibleBakedModel gaiaGems;

	private static final Function<ResourceLocation, TextureAtlasSprite> TEXTUREGETTER = new Function<ResourceLocation, TextureAtlasSprite>() {
		@Override
		public TextureAtlasSprite apply(ResourceLocation input) {
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
		}
	};

	private static final Set<String> GROUP_NAMES = ImmutableSet.of("Crystal", "Crystal_Ring", "Ring_Panel01", "Ring_Panel02",
			"Ring_Panel03", "Ring_Panel04", "Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04");

	public ModelPylon() {
		try {
			// Load the OBJ
			OBJModel model = ((OBJModel) OBJLoader.instance.loadModel(new ResourceLocation("botania:models/block/pylon.obj")));

			// Apply the texture and flip the v's of the model
			IModel manaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon"))).process(ImmutableMap.of("flip-v", "true"));
			IModel naturaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon1"))).process(ImmutableMap.of("flip-v", "true"));
			IModel gaiaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon2"))).process(ImmutableMap.of("flip-v", "true"));

			// All groups are off by default
			// (OBJState constructor is derp, so the boolean condition is inverted - pass false to turn on a group)

			// Turn on "Crystal"
			manaCrystal = manaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);
			naturaCrystal = naturaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);
			gaiaCrystal = gaiaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);

			// Turn on "Crystal_Ring", and "Ring_Panel0x"
			manaRingsAndPanes = manaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal_Ring", "Ring_Panel01", "Ring_Panel02", "Ring_Panel03", "Ring_Panel04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);
			naturaRingsAndPanes = naturaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal_Ring", "Ring_Panel01", "Ring_Panel02", "Ring_Panel03", "Ring_Panel04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);
			gaiaRingsAndPanes = gaiaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal_Ring", "Ring_Panel01", "Ring_Panel02", "Ring_Panel03", "Ring_Panel04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);

			// Turn on "Ring_Gem0x"
			manaGems = manaModel.bake(new OBJModel.OBJState(ImmutableList.of("Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);
			naturaGems = naturaModel.bake(new OBJModel.OBJState(ImmutableList.of("Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);
			gaiaGems = gaiaModel.bake(new OBJModel.OBJState(ImmutableList.of("Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);
		} catch (IOException e) {
			throw new ReportedException(new CrashReport("Error making pylon submodels for TESR!", e));
		}
	}

	@Override
	public void renderCrystal(PylonVariant variant) {
		switch (variant) {
			case MANA: renderModel(manaCrystal); break;
			case NATURA: renderModel(naturaCrystal); break;
			case GAIA: renderModel(gaiaCrystal); break;
		}
	}

	@Override
	public void renderRing(PylonVariant variant) {
		GlStateManager.disableLighting();
		switch (variant) {
			case MANA: renderModel(manaRingsAndPanes); break;
			case NATURA: renderModel(naturaRingsAndPanes); break;
			case GAIA: renderModel(gaiaRingsAndPanes); break;
		}
		GlStateManager.enableLighting();
	}

	@Override
	public void renderGems(PylonVariant variant) {
		GlStateManager.disableLighting();
		switch (variant) {
			case MANA: renderModel(manaGems); break;
			case NATURA: renderModel(naturaGems); break;
			case GAIA: renderModel(gaiaGems); break;
		}
		GlStateManager.enableLighting();
	}

	private void renderModel(IFlexibleBakedModel model)
	{
		renderModel(model, -1);
	}

	private void renderModel(IFlexibleBakedModel model, int color)
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(GL11.GL_QUADS, model.getFormat());

		this.renderQuads(worldrenderer, model.getGeneralQuads(), color);
		tessellator.draw();
	}

	private void renderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color)
	{
		for (BakedQuad bakedquad : quads)
			LightUtil.renderQuadColor(renderer, bakedquad, color);
	}

}
