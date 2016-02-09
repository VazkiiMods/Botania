package vazkii.botania.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.animation.Event;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.vecmath.Matrix4f;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This class handles choosing between the lexicon's default model and the special first person one
 */
public class LexiconModel implements IPerspectiveAwareModel {

	private static final Map<ItemCameraTransforms.TransformType, TRSRTransformation> missingTransforms;

	static {
		ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> builder = ImmutableMap.builder();
		for (ItemCameraTransforms.TransformType t :ItemCameraTransforms.TransformType.values()) {
			builder.put(t, TRSRTransformation.identity());
		}
		missingTransforms = builder.build();
	}

	private IFlexibleBakedModel normalModel;
	private Map<ItemCameraTransforms.TransformType, TRSRTransformation> normalModelTransforms;

	private IModel specialModel;
	private Map<ItemCameraTransforms.TransformType, TRSRTransformation> specialModelTransforms;

	private IAnimationStateMachine asm;

	private boolean hasInit = false;

	private void initModels() {
		if(hasInit)
			return;

		normalModel = new IFlexibleBakedModel.Wrapper(Minecraft.getMinecraft()
				.getRenderItem().getItemModelMesher().getModelManager()
				.getModel(new ModelResourceLocation("botania:lexicon_default", "inventory")), Attributes.DEFAULT_BAKED_FORMAT);

		asm = Animation.INSTANCE.load(new ResourceLocation("botania", "asms/item/lexicon_firstperson.json"), ImmutableMap.of());

		try {
			normalModelTransforms = IPerspectiveAwareModel.MapWrapper.getTransforms(ModelLoaderRegistry.getModel(new ModelResourceLocation("botania:lexicon_default", "inventory")).getDefaultState());
			specialModel = ModelLoaderRegistry.getModel(new ModelResourceLocation("botania:lexicon_firstperson", "inventory"));
			specialModelTransforms = IPerspectiveAwareModel.MapWrapper.getTransforms(specialModel.getDefaultState());
		} catch (IOException ex) {
			FMLLog.warning("[Botania] Lexicon model couldn't be loaded");
			if(normalModelTransforms == null)
				normalModelTransforms = missingTransforms;
			if(specialModel == null)
				specialModel = ModelLoaderRegistry.getMissingModel();
			if(specialModelTransforms == null)
				specialModelTransforms = missingTransforms;
		}

		hasInit = true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		initModels();
		if (ConfigHandler.lexicon3dModel && cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON) {
			//System.out.println("m8");
			float time = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) / 20F;

			Pair<IModelState, Iterable<Event>> animate = asm.apply(time);
			IFlexibleBakedModel baked = specialModel.bake(animate.getLeft(), Attributes.DEFAULT_BAKED_FORMAT, ModelLoader.defaultTextureGetter());

			TRSRTransformation transform = specialModelTransforms.get(cameraTransformType) == null ? TRSRTransformation.identity() : specialModelTransforms.get(cameraTransformType);
			return Pair.of(baked, transform.getMatrix());

		} else {
			TRSRTransformation transform = normalModelTransforms.get(cameraTransformType) == null ? TRSRTransformation.identity() : normalModelTransforms.get(cameraTransformType);
			return Pair.of(normalModel, transform.getMatrix());
		}
	}

	@Override public VertexFormat getFormat() { return Attributes.DEFAULT_BAKED_FORMAT; }
	@Override public List<BakedQuad> getFaceQuads(EnumFacing e) { initModels(); return normalModel.getFaceQuads(e); }
	@Override public List<BakedQuad> getGeneralQuads() { initModels(); return normalModel.getGeneralQuads(); }
	@Override public boolean isAmbientOcclusion() { initModels(); return normalModel.isAmbientOcclusion(); }
	@Override public boolean isGui3d() { initModels(); return normalModel.isGui3d(); }
	@Override public boolean isBuiltInRenderer() { initModels(); return normalModel.isBuiltInRenderer(); }
	@Override public TextureAtlasSprite getParticleTexture() { initModels(); return normalModel.getParticleTexture(); }
	@SuppressWarnings("deprecation")
	@Override public ItemCameraTransforms getItemCameraTransforms() { initModels(); return normalModel.getItemCameraTransforms(); }
}
