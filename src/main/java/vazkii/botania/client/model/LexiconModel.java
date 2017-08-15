package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;
import java.util.List;

public class LexiconModel implements IBakedModel {
	private final ModelResourceLocation path2D;

	public LexiconModel(ModelResourceLocation path) {
		this.path2D = path;
	}

	@Nonnull
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		if((cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
				|| cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
				&& ConfigHandler.lexicon3dModel)
			return Pair.of(this, null);
		IBakedModel original = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(path2D);
		return original.handlePerspective(cameraTransformType);
	}

	@Nonnull @Override public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) { return ImmutableList.of(); }
	@Override public boolean isAmbientOcclusion() { return false; }
	@Override public boolean isGui3d() { return false; }
	@Override public boolean isBuiltInRenderer() { return false; }
	@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("botania:items/lexicon"); }
	@SuppressWarnings("deprecation") @Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
	@Nonnull @Override public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }
}
