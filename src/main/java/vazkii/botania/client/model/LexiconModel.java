package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class LexiconModel implements IBakedModel {
	private final IBakedModel original;

	public LexiconModel(IBakedModel original) {
		this.original = original;
	}

	@Nonnull
	@Override
	public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack stack) {
		if((cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
				|| cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
				&& ConfigHandler.CLIENT.lexicon3dModel.get())
			return this;
		return original.handlePerspective(cameraTransformType, stack);
	}

	@Nonnull @Override public List<BakedQuad> getQuads(BlockState state, Direction side, @Nonnull Random rand) { return ImmutableList.of(); }
	@Override public boolean isAmbientOcclusion() { return false; }
	@Override public boolean isGui3d() { return false; }
	@Override public boolean isBuiltInRenderer() { return false; }
	@Override public boolean isSideLit() { return original.isSideLit(); }
	@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return original.getParticleTexture(); }
	@SuppressWarnings("deprecation") @Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
	@Nonnull @Override public ItemOverrideList getOverrides() { return original.getOverrides(); }
}
