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
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FloatingFlowerModel implements IBakedModel {

	private static final String MUNDANE_PREFIX = "botania:shimmeringFlower_";
	private final Table<IFloatingFlower.IslandType, String, CompositeBakedModel> CACHE = HashBasedTable.create();

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
					transform.getMatrix().transform(vec);
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
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing face, long rand) {
		if(state.getBlock() != ModBlocks.floatingSpecialFlower && state.getBlock() != ModBlocks.floatingFlower)
			return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, face, rand);
		IExtendedBlockState realState = (IExtendedBlockState) state;
		IFloatingFlower.IslandType islandType = realState.getValue(BotaniaStateProps.ISLAND_TYPE);
		String identifier;

		if(state.getBlock() == ModBlocks.floatingSpecialFlower) {
			// Magic flower
			identifier = realState.getValue(BotaniaStateProps.SUBTILE_ID);
		} else {
			// Mundane flower
			identifier = MUNDANE_PREFIX + state.getValue(BotaniaStateProps.COLOR).getMetadata();
		}

		return getModel(islandType, identifier).getQuads(state, face, rand);
	}

	// Get the model for this islandtype + flower type combination. If it's not cached already, generate it.
	private CompositeBakedModel getModel(IFloatingFlower.IslandType islandType, String identifier) {
		ModelManager modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();

		if(islandType == null) // This and the next one can be null if obtained from a non-extended state
			islandType = IFloatingFlower.IslandType.GRASS;
		if(identifier == null)
			identifier = "";

		if(CACHE.contains(islandType, identifier)) {
			return CACHE.get(islandType, identifier);
		} else {
			IBakedModel islandModel;
			try {
				islandModel = ModelLoaderRegistry.getModel(BotaniaAPIClient.getRegisteredIslandTypeModels().get(islandType)).bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
			} catch (Exception e) {
				islandModel = modelManager.getMissingModel();
			}

			IBakedModel flowerModel;

			if(identifier.startsWith(MUNDANE_PREFIX)) {
				int meta = Integer.parseInt(identifier.substring(identifier.indexOf(MUNDANE_PREFIX) + MUNDANE_PREFIX.length()));
				flowerModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(ModBlocks.shinyFlower, 1, meta));
			} else {
				ItemStack stack = ItemBlockSpecialFlower.ofType(identifier);
				IBakedModel specialFlowerModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
				flowerModel = specialFlowerModel.getOverrides().handleItemState(specialFlowerModel, stack, null, null);
			}

			// Enhance!
			CompositeBakedModel model = new CompositeBakedModel(flowerModel, islandModel);
			Botania.LOGGER.debug("Cached floating flower model for islandtype {} and flowertype {}", islandType, identifier);
			CACHE.put(islandType, identifier, model);
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
	@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt"); }
	@Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }

	private static class CompositeBakedModel implements IBakedModel {

		private final IBakedModel flower;
		private final IBakedModel island;
		private final List<BakedQuad> genQuads;
		private final Map<EnumFacing, List<BakedQuad>> faceQuads = new EnumMap<>(EnumFacing.class);

		public CompositeBakedModel(IBakedModel flower, IBakedModel island) {
			this.flower = flower;
			this.island = island;

			ImmutableList.Builder<BakedQuad> genBuilder = ImmutableList.builder();
			final TRSRTransformation transform = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null));

			for(EnumFacing e : EnumFacing.VALUES)
				faceQuads.put(e, new ArrayList<>());

			// Add flower quads, scaled and translated
			flower.getQuads(null, null, 0).stream().map(q -> transform(q, transform)).forEach(genBuilder::add);
			for(EnumFacing e : EnumFacing.VALUES) {
				List<BakedQuad> faceQ = faceQuads.get(e);
				flower.getQuads(null, e, 0).stream().map(input -> transform(input, transform)).forEach(faceQ::add);
			}

			// Add island quads
			genBuilder.addAll(island.getQuads(null, null, 0));
			for(EnumFacing e : EnumFacing.VALUES) {
				faceQuads.get(e).addAll(island.getQuads(null, e, 0));
			}

			genQuads = genBuilder.build();
		}

		// Forward all to flower model
		@Nonnull @Override public List<BakedQuad> getQuads(IBlockState state, EnumFacing face, long rand) {
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
		@Nonnull @Override public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
			Pair<? extends IBakedModel, Matrix4f> pair = island.handlePerspective(cameraTransformType);
			if(pair != null && pair.getRight() != null)
				return Pair.of(this, pair.getRight());
			return Pair.of(this, TRSRTransformation.identity().getMatrix());
		}
	}

	private final ItemOverrideList itemHandler = new ItemOverrideList(ImmutableList.of()) {
		@Nonnull
		@Override
		public IBakedModel handleItemState(@Nonnull IBakedModel model, ItemStack stack, World world, EntityLivingBase entity) {
			// Items always have GRASS island
			IFloatingFlower.IslandType islandType = IFloatingFlower.IslandType.GRASS;
			String identifier;

			if(Block.getBlockFromItem(stack.getItem()) == ModBlocks.floatingSpecialFlower) {
				// Magic flower
				identifier = ItemBlockSpecialFlower.getType(stack);
			} else {
				// Mundane flower
				identifier = MUNDANE_PREFIX + stack.getItemDamage();
			}

			return FloatingFlowerModel.this.getModel(islandType, identifier);
		}
	};
}
