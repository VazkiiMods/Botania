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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SpecialFlowerModel implements IModel {
	private static final ModelResourceLocation MISSING = new ModelResourceLocation("builtin/missing", "missing");
	private final ModelResourceLocation baseModel;
	private final ImmutableMap<String, ModelResourceLocation> blockModels;
	private final ImmutableMap<String, ModelResourceLocation> itemModels;

	private SpecialFlowerModel(ModelResourceLocation baseModel,
								ImmutableMap<String, ModelResourceLocation> blockModels,
								ImmutableMap<String, ModelResourceLocation> itemModels) {
		this.baseModel = baseModel;
		this.blockModels = blockModels;
		this.itemModels = itemModels;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
		if(!MISSING.equals(baseModel))
			builder.add(baseModel);
		builder.addAll(blockModels.values());
		builder.addAll(itemModels.values());

		// Force island models to be loaded and baked. See FloatingFlowerModel.
		builder.addAll(BotaniaAPIClient.getRegisteredIslandTypeModels().values());

		return builder.build();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableList.of();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms = PerspectiveMapWrapper.getTransforms(state);
		IModelState transformState = new SimpleModelState(transforms);

		IBakedModel baseModelBaked = ModelLoaderRegistry.getModelOrMissing(baseModel)
				.bake(transformState, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());

		ImmutableMap.Builder<String, IBakedModel> bakedBlockBuilder = ImmutableMap.builder();
		for(Map.Entry<String, ModelResourceLocation> e : blockModels.entrySet()) {
			IModel model = ModelLoaderRegistry.getModelOrMissing(e.getValue());
			if(model != ModelLoaderRegistry.getMissingModel())
				bakedBlockBuilder.put(e.getKey(), model.bake(transformState, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter()));
		}

		ImmutableMap.Builder<String, IBakedModel> bakedItemBuilder = ImmutableMap.builder();
		for(Map.Entry<String, ModelResourceLocation> e : itemModels.entrySet()) {
			IModel model = ModelLoaderRegistry.getModelOrMissing(e.getValue());
			if(model != ModelLoaderRegistry.getMissingModel())
				bakedItemBuilder.put(e.getKey(), model.bake(transformState, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter()));
		}

		return new SpecialFlowerBakedModel(baseModelBaked, bakedBlockBuilder.build(), bakedItemBuilder.build(), transforms);
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	@Override
	public IModel process(ImmutableMap<String, String> customData) {
		// Load the base variant from blockstate json, and also add all the model paths we received from external API

		ModelResourceLocation base = baseModel;
		if(customData.containsKey("base"))
			// Forge blockstate gives custom data in json form, have to drop the quotes
			base = new ModelResourceLocation(customData.get("base").substring(1, customData.get("base").length() - 1));

		ImmutableMap<String, ModelResourceLocation> blockModels = ImmutableMap.copyOf(BotaniaAPIClient.getRegisteredSubtileBlockModels());
		ImmutableMap<String, ModelResourceLocation> itemModels = ImmutableMap.copyOf(BotaniaAPIClient.getRegisteredSubtileItemModels());
		return new SpecialFlowerModel(base, blockModels, itemModels);
	}

	public enum Loader implements ICustomModelLoader {
		INSTANCE {
			@Override
			public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
			}

			@Override
			public boolean accepts(ResourceLocation modelLocation) {
				return modelLocation.getNamespace().equals("botania_special") && (
						modelLocation.getPath().equals("specialflower") ||
						modelLocation.getPath().equals("models/block/specialflower") ||
						modelLocation.getPath().equals("models/item/specialflower"));
			}

			@Override
			public IModel loadModel(ResourceLocation modelLocation) {
				// Load a dummy model for now, all actual blockModels added in process().
				return new SpecialFlowerModel(new ModelResourceLocation("builtin/missing", "missing"), ImmutableMap.of(), ImmutableMap.of());
			}
		}
	}

	private static class SpecialFlowerBakedModel implements IBakedModel {
		private final IBakedModel baseModel;
		private final ImmutableMap<String, IBakedModel> bakedBlockModels;
		private final ImmutableMap<String, IBakedModel> bakedItemModels;
		private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;

		SpecialFlowerBakedModel(IBakedModel baseModel,
								ImmutableMap<String, IBakedModel> bakedBlockModels,
								ImmutableMap<String, IBakedModel> bakedItemModels,
								ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> cameraTransforms) {
			this.baseModel = baseModel;
			this.bakedBlockModels = bakedBlockModels;
			this.bakedItemModels = bakedItemModels;
			this.transforms = cameraTransforms;
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing face, long rand) {
			IExtendedBlockState extendedState = (IExtendedBlockState) state;
			String subtileId = extendedState.getValue(BotaniaStateProps.SUBTILE_ID);

			IBakedModel model = bakedBlockModels.get(subtileId == null ? "" : subtileId);
			if(model == null)
				model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel();

			return model.getQuads(state, face, rand);
		}

		@Nonnull
		@Override
		public ItemOverrideList getOverrides() {
			return itemHandler;
		}

		private final ItemStack roseFallback = new ItemStack(Blocks.RED_FLOWER);

		private final ItemOverrideList itemHandler = new ItemOverrideList(ImmutableList.of()) {
			@Nonnull
			@Override
			public IBakedModel handleItemState(@Nonnull IBakedModel original, ItemStack stack, World world, EntityLivingBase living) {
				IBakedModel model = bakedItemModels.get(ItemBlockSpecialFlower.getType(stack));

				if(model == null)
					model = bakedBlockModels.get(ItemBlockSpecialFlower.getType(stack));
				if(model == null)
					model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(roseFallback);

				return model;
			}
		};

		@Override
		public boolean isAmbientOcclusion() {
			return baseModel.isAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return baseModel.isGui3d();
		}

		@Override
		public boolean isBuiltInRenderer() {
			return baseModel.isBuiltInRenderer();
		}

		@Nonnull
		@Override
		public TextureAtlasSprite getParticleTexture() {
			return baseModel.getParticleTexture();
		}

		@Nonnull
		@Override
		public ItemCameraTransforms getItemCameraTransforms() {
			return baseModel.getItemCameraTransforms();
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
			return PerspectiveMapWrapper.handlePerspective(this, transforms, cameraTransformType);
		}
	}

}
