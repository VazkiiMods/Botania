/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import vazkii.botania.api.state.BotaniaStateProps;

import java.util.List;

public class PlatformModel implements ISmartBlockModel {

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		EnumWorldBlockLayer layer = MinecraftForgeClient.getRenderLayer();
		IBlockState heldState = ((IExtendedBlockState) state).getValue(BotaniaStateProps.HELD_STATE);

		Minecraft mc = Minecraft.getMinecraft();
		if(heldState == null && layer == EnumWorldBlockLayer.SOLID) {
			// No camo
			ModelResourceLocation path = new ModelResourceLocation("botania:platform", "variant=" + state.getValue(BotaniaStateProps.PLATFORM_VARIANT).getName());
			return mc.getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(path);
		} else if(heldState != null) {
			if(heldState.getBlock().canRenderInLayer(layer)) {
				// Steal camo's model
				IBakedModel model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(heldState);

				if(model instanceof ISmartBlockModel) {
					// Their model can be smart too
					model = ((ISmartBlockModel) model).handleBlockState(heldState);
				}

				return model;
			}
		}

		return this; // This smart model has no quads as seen below, so nothing actually renders
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) {
		return ImmutableList.of();
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		return ImmutableList.of();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("botania:blocks/livingwood0");
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

}
