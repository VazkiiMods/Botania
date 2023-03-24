/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.api.block.FloatingFlower;
import vazkii.botania.xplat.ClientXplatAbstractions;

import java.util.*;
import java.util.function.Supplier;

/*
 * NB: We extend BlockModel only as an artifact of where we inject our mixin.
 * Pretty much all of the data of the superclass is ignored.
 */
public class FabricFloatingFlowerModel extends BlockModel {
	private final UnbakedModel unbakedFlower;
	private final Map<FloatingFlower.IslandType, UnbakedModel> unbakedIslands = new HashMap<>();

	private FabricFloatingFlowerModel(UnbakedModel flower) {
		super(null, Collections.emptyList(), Collections.emptyMap(), false, GuiLight.SIDE, ItemTransforms.NO_TRANSFORMS, Collections.emptyList());
		this.unbakedFlower = flower;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	// TODO 1.19.3 figure out if anything needs doing with a model here
	public static class Baked extends ForwardingBakedModel {
		private final Map<FloatingFlower.IslandType, BakedModel> islands;

		Baked(BakedModel flower, Map<FloatingFlower.IslandType, BakedModel> islands) {
			this.wrapped = flower;
			this.islands = islands;
		}

		private void emit(FloatingFlower.IslandType type, RenderContext ctx) {
			ctx.fallbackConsumer().accept(wrapped);
			ctx.fallbackConsumer().accept(islands.get(type));
		}

		@NotNull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
			List<BakedQuad> flower = wrapped.getQuads(null, null, rand);
			List<BakedQuad> island = islands.get(FloatingFlower.IslandType.GRASS).getQuads(null, null, rand);
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
		public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
			emit(FloatingFlower.IslandType.GRASS, context);
		}

		@Override
		public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
			Object data = ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);
			if (data instanceof FloatingFlower.IslandType type) {
				emit(type, context);
			}
		}
	}

	public static void hookModelLoad(JsonElement jsonElement, JsonDeserializationContext context, CallbackInfoReturnable<BlockModel> cir) {
		JsonObject json = jsonElement.getAsJsonObject();
		JsonElement loader = json.get("loader");
		if (loader != null && loader.isJsonPrimitive()
				&& loader.getAsString().equals(ClientXplatAbstractions.FLOATING_FLOWER_MODEL_LOADER_ID.toString())) {
			BlockModel flowerModel = context.deserialize(json.getAsJsonObject("flower"), BlockModel.class);
			cir.setReturnValue(new FabricFloatingFlowerModel(flowerModel));
		}
	}
}
