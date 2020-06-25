/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.BotaniaStateProps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;
import java.util.function.Function;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class FloatingFlowerModel implements IModelGeometry<FloatingFlowerModel> {
	private IUnbakedModel unbakedFlower;
	private final Map<IFloatingFlower.IslandType, IUnbakedModel> unbakedIslands = new HashMap<>();

	private FloatingFlowerModel(IUnbakedModel flower) {
		this.unbakedFlower = flower;
	}

	@Nonnull
	@Override
	public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		Set<Material> ret = new HashSet<>();
		for (Map.Entry<IFloatingFlower.IslandType, ResourceLocation> e : BotaniaAPIClient.instance().getRegisteredIslandTypeModels().entrySet()) {
			IUnbakedModel unbakedIsland = modelGetter.apply(e.getValue());
			ret.addAll(unbakedIsland.getTextures(modelGetter, missingTextureErrors));
			unbakedIslands.put(e.getKey(), unbakedIsland);
		}
		ret.addAll(unbakedFlower.getTextures(modelGetter, missingTextureErrors));
		return ret;
	}

	@Nullable
	@Override
	public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform transform, ItemOverrideList overrides, ResourceLocation name) {
		final TransformationMatrix moveFlower = new TransformationMatrix(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null);
		IModelTransform comp = new ModelTransformComposition(new SimpleModelTransform(moveFlower), transform);
		IBakedModel bakedFlower = unbakedFlower.bakeModel(bakery, spriteGetter, comp, name);

		Map<IFloatingFlower.IslandType, IBakedModel> bakedIslands = new HashMap<>();
		for (Map.Entry<IFloatingFlower.IslandType, IUnbakedModel> e : unbakedIslands.entrySet()) {
			IBakedModel bakedIsland = e.getValue().bakeModel(bakery, spriteGetter, transform, name);
			bakedIslands.put(e.getKey(), bakedIsland);
		}
		return new Baked(bakedFlower, bakedIslands);
	}

	public static class Baked extends DelegatedModel {
		private final Map<IFloatingFlower.IslandType, List<BakedQuad>> genQuads = new HashMap<>();
		private final Map<IFloatingFlower.IslandType, Map<Direction, List<BakedQuad>>> faceQuads = new HashMap<>();

		Baked(IBakedModel flower, Map<IFloatingFlower.IslandType, IBakedModel> islands) {
			super(flower);
			Random rand = new Random();
			for (Map.Entry<IFloatingFlower.IslandType, IBakedModel> e : islands.entrySet()) {
				rand.setSeed(42);
				List<BakedQuad> gen = new ArrayList<>(flower.getQuads(null, null, rand, EmptyModelData.INSTANCE));
				rand.setSeed(42);
				gen.addAll(e.getValue().getQuads(null, null, rand, EmptyModelData.INSTANCE));
				genQuads.put(e.getKey(), gen);

				Map<Direction, List<BakedQuad>> fq = new EnumMap<>(Direction.class);
				for (Direction dir : Direction.values()) {
					rand.setSeed(42);
					List<BakedQuad> lst = new ArrayList<>(flower.getQuads(null, dir, rand, EmptyModelData.INSTANCE));
					rand.setSeed(42);
					lst.addAll(e.getValue().getQuads(null, dir, rand, EmptyModelData.INSTANCE));
					fq.put(dir, lst);
				}
				faceQuads.put(e.getKey(), fq);
			}
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
			// Default to GRASS island
			return getQuads(state, side, rand, EmptyModelData.INSTANCE);
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
			IFloatingFlower.IslandType type = IFloatingFlower.IslandType.GRASS;
			if (extraData.hasProperty(BotaniaStateProps.FLOATING_DATA)) {
				type = extraData.getData(BotaniaStateProps.FLOATING_DATA).getIslandType();
			}

			if (side == null) {
				return genQuads.get(type);
			} else {
				return faceQuads.get(type).get(side);
			}
		}

		@Nonnull
		@Override
		public IBakedModel handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType, MatrixStack ms) {
			super.handlePerspective(cameraTransformType, ms);
			return this;
		}
	}

	public enum Loader implements IModelLoader<FloatingFlowerModel> {
		INSTANCE;

		public static final ResourceLocation ID = prefix("floating_flower");

		@Override
		public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {}

		@Nonnull
		@Override
		public FloatingFlowerModel read(JsonDeserializationContext ctx, JsonObject model) {
			BlockModel flower = ctx.deserialize(model.getAsJsonObject("flower"), BlockModel.class);
			return new FloatingFlowerModel(flower);
		}
	}
}
