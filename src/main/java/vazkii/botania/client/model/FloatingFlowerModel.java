/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

/* todo 1.16-fabric
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.AffineTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.BotaniaStateProps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;
import java.util.function.Function;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class FloatingFlowerModel implements IModelGeometry<FloatingFlowerModel> {
	private UnbakedModel unbakedFlower;
	private final Map<IFloatingFlower.IslandType, UnbakedModel> unbakedIslands = new HashMap<>();

	private FloatingFlowerModel(UnbakedModel flower) {
		this.unbakedFlower = flower;
	}

	@Nonnull
	@Override
	public Collection<SpriteIdentifier> getTextures(IModelConfiguration owner, Function<Identifier, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		Set<SpriteIdentifier> ret = new HashSet<>();
		for (Map.Entry<IFloatingFlower.IslandType, Identifier> e : BotaniaAPIClient.instance().getRegisteredIslandTypeModels().entrySet()) {
			UnbakedModel unbakedIsland = modelGetter.apply(e.getValue());
			ret.addAll(unbakedIsland.getTextureDependencies(modelGetter, missingTextureErrors));
			unbakedIslands.put(e.getKey(), unbakedIsland);
		}
		ret.addAll(unbakedFlower.getTextureDependencies(modelGetter, missingTextureErrors));
		return ret;
	}

	@Nullable
	@Override
	public BakedModel bake(IModelConfiguration owner, ModelLoader bakery, Function<SpriteIdentifier, Sprite> spriteGetter, ModelBakeSettings transform, ModelOverrideList overrides, Identifier name) {
		final AffineTransformation moveFlower = new AffineTransformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null);
		ModelBakeSettings comp = new ModelTransformComposition(new SimpleModelTransform(moveFlower), transform);
		BakedModel bakedFlower = unbakedFlower.bake(bakery, spriteGetter, comp, name);

		Map<IFloatingFlower.IslandType, BakedModel> bakedIslands = new HashMap<>();
		for (Map.Entry<IFloatingFlower.IslandType, UnbakedModel> e : unbakedIslands.entrySet()) {
			BakedModel bakedIsland = e.getValue().bake(bakery, spriteGetter, transform, name);
			bakedIslands.put(e.getKey(), bakedIsland);
		}
		return new Baked(bakedFlower, bakedIslands);
	}

	public static class Baked extends DelegatedModel {
		private final Map<IFloatingFlower.IslandType, List<BakedQuad>> genQuads = new HashMap<>();
		private final Map<IFloatingFlower.IslandType, Map<Direction, List<BakedQuad>>> faceQuads = new HashMap<>();

		Baked(BakedModel flower, Map<IFloatingFlower.IslandType, BakedModel> islands) {
			super(flower);
			Random rand = new Random();
			for (Map.Entry<IFloatingFlower.IslandType, BakedModel> e : islands.entrySet()) {
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
		public BakedModel handlePerspective(@Nonnull ModelTransformation.Mode cameraTransformType, MatrixStack ms) {
			super.handlePerspective(cameraTransformType, ms);
			return this;
		}
	}

	public enum Loader implements IModelLoader<FloatingFlowerModel> {
		INSTANCE;

		public static final Identifier ID = prefix("floating_flower");

		@Override
		public void apply(@Nonnull ResourceManager resourceManager) {}

		@Nonnull
		@Override
		public FloatingFlowerModel read(JsonDeserializationContext ctx, JsonObject model) {
			JsonUnbakedModel flower = ctx.deserialize(model.getAsJsonObject("flower"), JsonUnbakedModel.class);
			return new FloatingFlowerModel(flower);
		}
	}
}
*/
