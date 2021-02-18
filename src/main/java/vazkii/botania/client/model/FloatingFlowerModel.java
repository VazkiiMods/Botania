/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.AffineTransformation;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class FloatingFlowerModel implements UnbakedModel {
	private final UnbakedModel unbakedFlower;
	private final Map<IFloatingFlower.IslandType, UnbakedModel> unbakedIslands = new HashMap<>();

	private FloatingFlowerModel(UnbakedModel flower) {
		this.unbakedFlower = flower;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
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
	public BakedModel bake(ModelLoader bakery, Function<SpriteIdentifier, Sprite> spriteGetter, ModelBakeSettings transform, Identifier name) {
		final AffineTransformation moveFlower = new AffineTransformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null);
		AffineTransformation mul = moveFlower.multiply(transform.getRotation());
		ModelBakeSettings newTransform = new ModelBakeSettings() {
			@Override
			public AffineTransformation getRotation() {
				return mul;
			}

			@Override
			public boolean isShaded() {
				return transform.isShaded();
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

	public static class Baked extends ForwardingBakedModel {
		private final Map<IFloatingFlower.IslandType, BakedModel> islands;

		Baked(BakedModel flower, Map<IFloatingFlower.IslandType, BakedModel> islands) {
			this.wrapped = flower;
			this.islands = islands;
		}

		private void emit(IFloatingFlower.IslandType type, RenderContext ctx) {
			ctx.fallbackConsumer().accept(wrapped);
			ctx.fallbackConsumer().accept(islands.get(type));
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
			List<BakedQuad> flower = wrapped.getQuads(null, null, rand);
			List<BakedQuad> island = islands.get(IFloatingFlower.IslandType.GRASS).getQuads(null, null, rand);
			List<BakedQuad> ret = new ArrayList<>(flower.size() + island.size());
			ret.addAll(flower);
			ret.addAll(island);
			return ret;
		}

		@Override
		public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
			emit(IFloatingFlower.IslandType.GRASS, context);
		}

		@Override
		public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
			Object data = ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);
			if (data instanceof IFloatingFlower.IslandType) {
				emit((IFloatingFlower.IslandType) data, context);
			}
		}
	}

	public enum Loader implements ModelResourceProvider {
		INSTANCE;

		private static final Gson GSON = new Gson();

		@Nullable
		@Override
		public UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) throws ModelProviderException {
			// blockstate json or child model will specify e.g. "botania:block/floating_daffomill__botania_floating"
			// which we then load by reading "/assets/botania/models/block/floating_daffomill.json
			// and looking at specific json entries inside of it
			String suffix = "__botania_floating";
			if (resourceId.getPath().endsWith(suffix)) {
				String realPath = resourceId.getPath().substring(0, resourceId.getPath().length() - suffix.length());
				Identifier fsId = new Identifier(resourceId.getNamespace(), "models/" + realPath + ".json");
				try {
					Resource resource = MinecraftClient.getInstance().getResourceManager().getResource(fsId);
					try (InputStreamReader ir = new InputStreamReader(resource.getInputStream())) {
						JsonObject json = GSON.fromJson(ir, JsonElement.class).getAsJsonObject();
						String flower = JsonHelper.getString(json, "flower");
						UnbakedModel flowerModel = context.loadModel(new Identifier(flower));
						return new FloatingFlowerModel(flowerModel);
					}
				} catch (Exception ex) {
					throw new ModelProviderException("Failed to load floating model " + resourceId, ex);
				}
			}
			return null;
		}
	}
}
