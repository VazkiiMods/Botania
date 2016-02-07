package vazkii.botania.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.animation.Event;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * This class handles choosing between the lexicon's default model and the special first person one
 */
public class LexiconModel implements IPerspectiveAwareModel {

	private IFlexibleBakedModel normalModel;
	private IFlexibleBakedModel specialModel;
	private IAnimationStateMachine asm;

	private IFlexibleBakedModel getNormalModel() {
		if (normalModel == null) {
			normalModel = new IFlexibleBakedModel.Wrapper(Minecraft.getMinecraft()
					.getRenderItem().getItemModelMesher().getModelManager()
					.getModel(new ModelResourceLocation("botania:lexicon_default", "inventory")), Attributes.DEFAULT_BAKED_FORMAT);
		}
		return normalModel;
	}

	private IFlexibleBakedModel getSpecialModel() {
		if (specialModel == null) {
			specialModel = new IFlexibleBakedModel.Wrapper(Minecraft.getMinecraft()
					.getRenderItem().getItemModelMesher().getModelManager()
					.getModel(new ModelResourceLocation("botania:lexicon_firstperson", "inventory")), Attributes.DEFAULT_BAKED_FORMAT);
		}

		if (asm == null) {
			asm = Animation.INSTANCE.load(new ResourceLocation("botania", "asms/item/lexicon_firstperson.json"), ImmutableMap.of());
		}
		return specialModel;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		if (ConfigHandler.lexicon3dModel && cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON) {
			IFlexibleBakedModel model = getSpecialModel();
			float time = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) / 20F;

			Pair<IModelState, Iterable<Event>> animate = asm.apply(time);
			return Pair.of(model, new TRSRTransformation(model.getItemCameraTransforms().getTransform(cameraTransformType)).getMatrix());

		} else {
			return Pair.of(getNormalModel(), new TRSRTransformation(getNormalModel().getItemCameraTransforms().getTransform(cameraTransformType)).getMatrix());
		}
	}

	@Override public VertexFormat getFormat() { return Attributes.DEFAULT_BAKED_FORMAT; }
	@Override public List<BakedQuad> getFaceQuads(EnumFacing e) { return getNormalModel().getFaceQuads(e); }
	@Override public List<BakedQuad> getGeneralQuads() { return getNormalModel().getGeneralQuads(); }
	@Override public boolean isAmbientOcclusion() { return getNormalModel().isAmbientOcclusion(); }
	@Override public boolean isGui3d() { return getNormalModel().isGui3d(); }
	@Override public boolean isBuiltInRenderer() { return getNormalModel().isBuiltInRenderer(); }
	@Override public TextureAtlasSprite getParticleTexture() { return getNormalModel().getParticleTexture(); }
	@SuppressWarnings("deprecation")
	@Override public ItemCameraTransforms getItemCameraTransforms() { return getNormalModel().getItemCameraTransforms(); }
}
