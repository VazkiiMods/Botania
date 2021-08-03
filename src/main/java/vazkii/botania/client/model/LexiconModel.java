/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Random;

public class LexiconModel implements BakedModel {
	private final BakedModel original;

	public LexiconModel(BakedModel original) {
		this.original = original;
	}

	/*  todo 1.16-fabric
	@Nonnull
	@Override
	public BakedModel handlePerspective(ModelTransformation.Mode cameraTransformType, MatrixStack stack) {
		if ((cameraTransformType == ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND
				|| cameraTransformType == ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND)
				&& ConfigHandler.CLIENT.lexicon3dModel.getValue()) {
			return this;
		}
		return original.handlePerspective(cameraTransformType, stack);
	}
	*/

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, @Nonnull Random rand) {
		return original.getQuads(state, side, rand);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public boolean usesBlockLight() {
		return original.usesBlockLight();
	}

	@Nonnull
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return original.getParticleIcon();
	}

	@Nonnull
	@Override
	public ItemTransforms getTransforms() {
		return original.getTransforms();
	}

	@Nonnull
	@Override
	public ItemOverrides getOverrides() {
		return original.getOverrides();
	}
}
