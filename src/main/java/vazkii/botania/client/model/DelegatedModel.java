/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Random;

public class DelegatedModel implements IBakedModel {
	protected final IBakedModel originalModel;

	public DelegatedModel(IBakedModel originalModel) {
		this.originalModel = originalModel;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return originalModel.isAmbientOcclusion();
	}

	@Override
	public boolean isAmbientOcclusion(BlockState state) {
		return originalModel.isAmbientOcclusion(state);
	}

	@Override
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean func_230044_c_() {
		return originalModel.func_230044_c_();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return originalModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return originalModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return originalModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return originalModel.getOverrides();
	}

	@Override
	public boolean doesHandlePerspectives() {
		return originalModel.doesHandlePerspectives();
	}

	@Override
	public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
		return originalModel.handlePerspective(cameraTransformType, mat);
	}

	@Override
	public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
		return originalModel.getParticleTexture(data);
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
		return originalModel.getQuads(state, side, rand, extraData);
	}

	@Nonnull
	@Override
	public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
		return originalModel.getModelData(world, pos, state, tileData);
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
		return null;
	}
}
