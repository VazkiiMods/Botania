package vazkii.botania.forge.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.IFloatingFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.block.tile.TileFloatingFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;
import java.util.function.Function;

public class ForgeFloatingFlowerModel implements IUnbakedGeometry<ForgeFloatingFlowerModel> {
	public static final ModelProperty<IFloatingFlower> FLOATING_PROPERTY = new ModelProperty<>();
	private final UnbakedModel unbakedFlower;
	private final Map<IFloatingFlower.IslandType, UnbakedModel> unbakedIslands = new HashMap<>();

	private ForgeFloatingFlowerModel(UnbakedModel flower) {
		this.unbakedFlower = flower;
	}

	@Nonnull
	@Override
	public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter,
			Set<Pair<String, String>> missingTextureErrors) {
		Set<Material> ret = new HashSet<>();
		for (Map.Entry<IFloatingFlower.IslandType, ResourceLocation> e : BotaniaAPIClient.instance().getRegisteredIslandTypeModels().entrySet()) {
			UnbakedModel unbakedIsland = modelGetter.apply(e.getValue());
			ret.addAll(unbakedIsland.getMaterials(modelGetter, missingTextureErrors));
			unbakedIslands.put(e.getKey(), unbakedIsland);
		}
		ret.addAll(unbakedFlower.getMaterials(modelGetter, missingTextureErrors));
		return ret;
	}

	@Nullable
	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery,
			Function<Material, TextureAtlasSprite> spriteGetter, ModelState transform, ItemOverrides overrides, ResourceLocation name) {
		final Transformation moveFlower = new Transformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null);
		Transformation mul = moveFlower.compose(transform.getRotation());
		ModelState newTransform = new ModelState() {
			@Override
			public Transformation getRotation() {
				return mul;
			}

			@Override
			public boolean isUvLocked() {
				return transform.isUvLocked();
			}
		};
		BakedModel bakedFlower = unbakedFlower.bake(bakery, spriteGetter, newTransform, name);

		Map<IFloatingFlower.IslandType, BakedModel> bakedIslands = new HashMap<>();
		for (Map.Entry<IFloatingFlower.IslandType, UnbakedModel> e : unbakedIslands.entrySet()) {
			BakedModel bakedIsland = e.getValue().bake(bakery, spriteGetter, transform, name);
			bakedIslands.put(e.getKey(), bakedIsland);
		}
		return new Baked(bakedFlower, bakedIslands);
	}

	public static class Baked extends BakedModelWrapper<BakedModel> {
		private final Map<IFloatingFlower.IslandType, BakedModel> islands;

		Baked(BakedModel flower, Map<IFloatingFlower.IslandType, BakedModel> islands) {
			super(flower);
			this.islands = islands;
		}

		@Nonnull
		@Override
		public ModelData getModelData(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData modelData) {
			var be = level.getBlockEntity(pos);
			if (be instanceof TileFloatingFlower floating) {
				return ModelData.builder()
						.with(FLOATING_PROPERTY, floating.getFloatingData())
						.build();
			} else if (be instanceof TileEntitySpecialFlower special && special.isFloating()) {
				return ModelData.builder()
						.with(FLOATING_PROPERTY, special.getFloatingData())
						.build();
			}
			return modelData;
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand) {
			// Default to GRASS island
			// TODO 1.19 verify
			return getQuads(state, side, rand, ModelData.EMPTY, RenderType.cutout());
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
				@Nonnull RandomSource rand, @Nonnull ModelData extraData, @Nullable RenderType renderType) {
			IFloatingFlower.IslandType type = IFloatingFlower.IslandType.GRASS;
			if (extraData.has(FLOATING_PROPERTY)) {
				type = extraData.get(FLOATING_PROPERTY).getIslandType();
			}

			List<BakedQuad> flower = super.getQuads(null, null, rand, ModelData.EMPTY, renderType);
			List<BakedQuad> island = islands.get(type).getQuads(null, null, rand, ModelData.EMPTY, renderType);
			List<BakedQuad> ret = new ArrayList<>(flower.size() + island.size());
			ret.addAll(flower);
			ret.addAll(island);
			return ret;
		}
	}

	public enum Loader implements IGeometryLoader<ForgeFloatingFlowerModel> {
		INSTANCE;

		@Nonnull
		@Override
		public ForgeFloatingFlowerModel read(JsonObject model, JsonDeserializationContext ctx) {
			BlockModel flower = ctx.deserialize(model.getAsJsonObject("flower"), BlockModel.class);
			return new ForgeFloatingFlowerModel(flower);
		}
	}
}
