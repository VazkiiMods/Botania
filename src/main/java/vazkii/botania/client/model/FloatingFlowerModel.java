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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.IFloatingFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 * NB Fabric: We extend BlockModel only as an artifact of where we inject our mixin.
 * Pretty much all of the data of the superclass is ignored.
 */
public class FloatingFlowerModel extends BlockModel {
	private final UnbakedModel unbakedFlower;
	private final Map<IFloatingFlower.IslandType, UnbakedModel> unbakedIslands = new HashMap<>();

	private FloatingFlowerModel(UnbakedModel flower) {
		super(null, Collections.emptyList(), Collections.emptyMap(), false, GuiLight.SIDE, ItemTransforms.NO_TRANSFORMS, Collections.emptyList());
		this.unbakedFlower = flower;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
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
	public BakedModel bake(ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState transform, ResourceLocation name) {
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
		public boolean isVanillaAdapter() {
			return false;
		}

		@Override
		public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
			emit(IFloatingFlower.IslandType.GRASS, context);
		}

		@Override
		public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
			Object data = ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);
			if (data instanceof IFloatingFlower.IslandType type) {
				emit(type, context);
			}
		}
	}

	public static final String MAGIC_STRING = "botania:floating_flower";

	public static void hookModelLoad(JsonElement jsonElement, JsonDeserializationContext context, CallbackInfoReturnable<BlockModel> cir) {
		JsonObject json = jsonElement.getAsJsonObject();
		JsonElement loader = json.get("loader");
		if (loader != null && loader.isJsonPrimitive() && loader.getAsString().equals(MAGIC_STRING)) {
			BlockModel flowerModel = context.deserialize(json.getAsJsonObject("flower"), BlockModel.class);
			cir.setReturnValue(new FloatingFlowerModel(flowerModel));
		}
	}
}
