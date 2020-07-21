/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Random;

public class LexiconModel implements BakedModel {
	private final BakedModel original;

	public LexiconModel(BakedModel original) {
		this.original = original;
	}

	@Nonnull
	@Override
	public BakedModel handlePerspective(ModelTransformation.Mode cameraTransformType, MatrixStack stack) {
		if ((cameraTransformType == ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND
				|| cameraTransformType == ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND)
				&& ConfigHandler.CLIENT.lexicon3dModel.get()) {
			return this;
		}
		return original.handlePerspective(cameraTransformType, stack);
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, @Nonnull Random rand) {
		return ImmutableList.of();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return original.isSideLit();
	}

	@Nonnull
	@Override
	public Sprite getSprite() {
		return original.getSprite();
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public ModelTransformation getTransformation() {
		return ModelTransformation.NONE;
	}

	@Nonnull
	@Override
	public ModelOverrideList getOverrides() {
		return original.getOverrides();
	}
}
