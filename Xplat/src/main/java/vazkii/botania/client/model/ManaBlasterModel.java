/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.google.common.base.Preconditions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.item.ManaBlasterItem;

import java.util.*;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaBlasterModel implements BakedModel {
	private static final ModelResourceLocation DESU = new ModelResourceLocation(prefix(":desu_gun"), "inventory");
	private static final ModelResourceLocation DESU_CLIP = new ModelResourceLocation(prefix(":desu_gun_clip"), "inventory");

	private final BakedModel originalModel;
	private final BakedModel originalModelClip;
	private final Map<Pair<Item, Boolean>, BakedModel> cache = new HashMap<>();

	public ManaBlasterModel(ModelBakery bakery, BakedModel originalModel, BakedModel originalModelClip) {
		this.originalModel = Preconditions.checkNotNull(originalModel);
		this.originalModelClip = Preconditions.checkNotNull(originalModelClip);

		for (var item : BuiltInRegistries.ITEM) {
			var lens = item.getDefaultInstance();
			if (ManaBlasterItem.isValidLens(lens)) {
				var baked = new CompositeBakedModel(bakery, lens, originalModel);
				var bakedClip = new CompositeBakedModel(bakery, lens, originalModelClip);
				cache.put(Pair.of(item, false), baked);
				cache.put(Pair.of(item, true), bakedClip);
			}
		}
	}

	private final ItemOverrides itemHandler = new ItemOverrides() {
		@NotNull
		@Override
		public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel worldIn, @Nullable LivingEntity entityIn, int seed) {
			boolean clip = ManaBlasterItem.hasClip(stack);

			if (ManaBlasterItem.isSugoiKawaiiDesuNe(stack)) {
				return Minecraft.getInstance().getModelManager().getModel(clip ? DESU_CLIP : DESU);
			}

			ItemStack lens = ManaBlasterItem.getLens(stack);
			if (!lens.isEmpty()) {
				return ManaBlasterModel.this.cache.getOrDefault(Pair.of(lens.getItem(), clip),
						Minecraft.getInstance().getModelManager().getMissingModel());
			} else {
				return clip ? originalModelClip : originalModel;
			}
		}
	};

	@NotNull
	@Override
	public ItemOverrides getOverrides() {
		return itemHandler;
	}

	@NotNull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
		return originalModel.getQuads(state, side, rand);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return originalModel.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean isCustomRenderer() {
		return originalModel.isCustomRenderer();
	}

	@NotNull
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return originalModel.getParticleIcon();
	}

	@NotNull
	@Override
	public ItemTransforms getTransforms() {
		return originalModel.getTransforms();
	}

	@Override
	public boolean usesBlockLight() {
		return originalModel.usesBlockLight();
	}

	private static class CompositeBakedModel extends DelegatedModel {
		private final List<BakedQuad> genQuads = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		CompositeBakedModel(ModelBakery bakery, ItemStack lens, BakedModel gun) {
			super(gun);
			// TODO: 1.19.3 figure out how to do this
			/*
			ResourceLocation lensId = BuiltInRegistries.ITEM.getKey(lens.getItem());
			UnbakedModel lensUnbaked = bakery.getModel(new ModelResourceLocation(lensId, "inventory"));
			ModelState transform = new ModelState() {
				@Override
				public Transformation getRotation() {
					return new Transformation(new Vector3f(-0.4F, 0.2F, 0.0F), VecHelper.rotateY(90), new Vector3f(0.625F, 0.625F, 0.625F), null);
				}
			};
			ResourceLocation name = prefix("gun_with_" + lensId.toString().replace(':', '_'));
			
			Function<Material, TextureAtlasSprite> textureGetter = ((ModelBakeryAccessor) bakery).getSpriteAtlasManager()::getSprite;
			BakedModel lensBaked;
			if (lensUnbaked instanceof BlockModel bm && bm.getRootModel() == net.minecraft.client.resources.model.ModelBakery.GENERATION_MARKER) {
				lensBaked = new ItemModelGenerator()
						.generateBlockModel(textureGetter, bm)
						.bake(bakery, bm, textureGetter, transform, name, false);
			} else {
				lensBaked = lensUnbaked.bake(bakery, textureGetter, transform, name);
			}
			
			for (Direction e : Direction.values()) {
				faceQuads.put(e, new ArrayList<>());
			}
			
			var rand = RandomSource.create();
			rand.setSeed(0);
			genQuads.addAll(lensBaked.getQuads(null, null, rand));
			
			for (Direction e : Direction.values()) {
				rand.setSeed(0);
				faceQuads.get(e).addAll(lensBaked.getQuads(null, e, rand));
			}
			
			// Add gun quads
			rand.setSeed(0);
			genQuads.addAll(gun.getQuads(null, null, rand));
			for (Direction e : Direction.values()) {
				rand.setSeed(0);
				faceQuads.get(e).addAll(gun.getQuads(null, e, rand));
			}
			*/
		}

		@NotNull
		@Override
		public List<BakedQuad> getQuads(BlockState state, Direction face, @NotNull RandomSource rand) {
			return face == null ? genQuads : faceQuads.get(face);
		}
	}

}
