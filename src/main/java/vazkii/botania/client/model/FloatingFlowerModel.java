/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FloatingFlowerModel implements IDynamicBakedModel {
	private final ModelBakery bakery;
	private final Table<IFloatingFlower.IslandType, ResourceLocation, CompositeBakedModel> CACHE = HashBasedTable.create();

	public FloatingFlowerModel(ModelBakery bakery) {
		this.bakery = bakery;
	}

	protected static BakedQuad transform(BakedQuad quad, final TRSRTransformation transform) {
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
		final IVertexConsumer consumer = new VertexTransformer(builder) {
			@Override
			public void put(int element, float... data) {
				VertexFormatElement formatElement = DefaultVertexFormats.ITEM.getElement(element);
				switch(formatElement.getUsage()) {
				case POSITION: {
					float[] newData = new float[4];
					Vector4f vec = new Vector4f(data);
					transform.getMatrixVec().transform(vec);
					vec.get(newData);
					parent.put(element, newData);
					break;
				}
				default: {
					parent.put(element, data);
					break;
				}
				}
			}
		};
		quad.pipe(consumer);
		return builder.build();
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, @Nonnull Random rand, @Nonnull IModelData data) {
		if(!data.hasProperty(BotaniaStateProps.FLOATING_DATA))
			return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, face, rand, data);
		IFloatingFlower floatingData = data.getData(BotaniaStateProps.FLOATING_DATA);
		return getModel(floatingData.getIslandType(), floatingData.getDisplayStack()).getQuads(state, face, rand);
	}

	// Get the model for this islandtype + flower type combination. If it's not cached already, generate it.
	private CompositeBakedModel getModel(IFloatingFlower.IslandType islandType, ItemStack flower) {
		ModelManager modelManager = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager();

		if(islandType == null) // This and the next one can be null if obtained from a non-extended state
			islandType = IFloatingFlower.IslandType.GRASS;

		if(CACHE.contains(islandType, flower.getItem().getRegistryName())) {
			return CACHE.get(islandType, flower.getItem().getRegistryName());
		} else {
			IBakedModel islandModel;
			try {
				islandModel = ModelLoaderRegistry.getModel(BotaniaAPIClient.getRegisteredIslandTypeModels().get(islandType))
						.bake(bakery, ModelLoader.defaultTextureGetter(),
								new BasicState(TRSRTransformation.identity(), false), DefaultVertexFormats.ITEM);
			} catch (Exception e) {
				islandModel = modelManager.getMissingModel();
			}

			IBakedModel flowerModel = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(flower);

			// Enhance!
			CompositeBakedModel model = new CompositeBakedModel(flowerModel, islandModel);
			Botania.LOGGER.debug("Cached floating flower model for islandtype {} and flowertype {}", islandType, flower.getItem().getRegistryName());
			CACHE.put(islandType, flower.getItem().getRegistryName(), model);
			return model;
		}
	}

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return itemHandler;
	}

	@Override public boolean isAmbientOcclusion() { return false; }
	@Override public boolean isGui3d() { return true; }
	@Override public boolean isBuiltInRenderer() { return false; }
	@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return Minecraft.getInstance().getTextureMap().getAtlasSprite("minecraft:blocks/dirt"); }
	@Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }

	private static class CompositeBakedModel implements IBakedModel {

		private final IBakedModel flower;
		private final IBakedModel island;
		private final List<BakedQuad> genQuads;
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		public CompositeBakedModel(IBakedModel flower, IBakedModel island) {
			this.flower = flower;
			this.island = island;

			ImmutableList.Builder<BakedQuad> genBuilder = ImmutableList.builder();
			final TRSRTransformation transform = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null));

			for(Direction e : Direction.values())
				faceQuads.put(e, new ArrayList<>());

			// Add flower quads, scaled and translated
			flower.getQuads(null, null, new Random(0)).stream().map(q -> transform(q, transform)).forEach(genBuilder::add);
			for(Direction e : Direction.values()) {
				List<BakedQuad> faceQ = faceQuads.get(e);
				flower.getQuads(null, e, new Random(0)).stream().map(input -> transform(input, transform)).forEach(faceQ::add);
			}

			// Add island quads
			genBuilder.addAll(island.getQuads(null, null, new Random(0)));
			for(Direction e : Direction.values()) {
				faceQuads.get(e).addAll(island.getQuads(null, e, new Random(0)));
			}

			genQuads = genBuilder.build();
		}

		// Forward all to flower model
		@Nonnull @Override public List<BakedQuad> getQuads(BlockState state, Direction face, @Nonnull Random rand) {
			return face == null ? genQuads : faceQuads.get(face);
		}
		@Override public boolean isAmbientOcclusion() {
			return flower.isAmbientOcclusion();
		}
		@Override public boolean isGui3d() {
			return flower.isGui3d();
		}
		@Override public boolean isBuiltInRenderer() {
			return flower.isBuiltInRenderer();
		}
		@Nonnull @Override public TextureAtlasSprite getParticleTexture() {
			return flower.getParticleTexture();
		}
		@Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() {
			return flower.getItemCameraTransforms();
		}
		@Nonnull @Override public ItemOverrideList getOverrides() { return ItemOverrideList.EMPTY; }

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
			Pair<? extends IBakedModel, Matrix4f> pair = island.handlePerspective(cameraTransformType);
			if(pair != null && pair.getRight() != null)
				return Pair.of(this, pair.getRight());
			return Pair.of(this, TRSRTransformation.identity().getMatrixVec());
		}
	}

	private final ItemOverrideList itemHandler = new ItemOverrideList() {
		@Nonnull
		@Override
		public IBakedModel getModelWithOverrides(IBakedModel model, ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
			// Items always have GRASS island
			return FloatingFlowerModel.this.getModel(IFloatingFlower.IslandType.GRASS, stack);
		}
	};
}
