package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Random;

public class LexiconModel implements IBakedModel {
	private final IBakedModel original;

	public LexiconModel(IBakedModel original) {
		this.original = original;
	}

	@Nonnull
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		if((cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
				|| cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
				&& ConfigHandler.CLIENT.lexicon3dModel.get())
			return Pair.of(this, null);
		return original.handlePerspective(cameraTransformType);
	}

	@Nonnull @Override public List<BakedQuad> getQuads(BlockState state, Direction side, @Nonnull Random rand) { return ImmutableList.of(); }
	@Override public boolean isAmbientOcclusion() { return false; }
	@Override public boolean isGui3d() { return false; }
	@Override public boolean isBuiltInRenderer() { return false; }
	@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return original.getParticleTexture(); }
	@SuppressWarnings("deprecation") @Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
	@Nonnull @Override public ItemOverrideList getOverrides() { return original.getOverrides(); }
}
