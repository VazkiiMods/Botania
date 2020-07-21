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
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.AffineTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;

import org.apache.commons.lang3.tuple.Pair;

import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class GunModel implements BakedModel {
	private static final ModelIdentifier DESU = new ModelIdentifier(LibMisc.MOD_ID + ":desu_gun", "inventory");
	private static final ModelIdentifier DESU_CLIP = new ModelIdentifier(LibMisc.MOD_ID + ":desu_gun_clip", "inventory");

	private final net.minecraft.client.render.model.ModelLoader bakery;
	private final BakedModel originalModel;
	private final BakedModel originalModelClip;

	public GunModel(net.minecraft.client.render.model.ModelLoader bakery, BakedModel originalModel, BakedModel originalModelClip) {
		this.bakery = bakery;
		this.originalModel = Preconditions.checkNotNull(originalModel);
		this.originalModelClip = Preconditions.checkNotNull(originalModelClip);
	}

	private final ModelOverrideList itemHandler = new ModelOverrideList() {
		@Nonnull
		@Override
		public BakedModel apply(BakedModel model, ItemStack stack, @Nullable ClientWorld worldIn, @Nullable LivingEntity entityIn) {
			boolean clip = ItemManaGun.hasClip(stack);

			if (ItemManaGun.isSugoiKawaiiDesuNe(stack)) {
				return MinecraftClient.getInstance().getBakedModelManager().getModel(clip ? DESU_CLIP : DESU);
			}

			ItemStack lens = ItemManaGun.getLens(stack);
			if (!lens.isEmpty()) {
				return GunModel.this.getModel(lens, clip);
			} else {
				return clip ? originalModelClip : originalModel;
			}
		}
	};

	@Nonnull
	@Override
	public ModelOverrideList getOverrides() {
		return itemHandler;
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
		return originalModel.getQuads(state, side, rand);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return originalModel.useAmbientOcclusion();
	}

	@Override
	public boolean hasDepth() {
		return originalModel.hasDepth();
	}

	@Override
	public boolean isBuiltin() {
		return originalModel.isBuiltin();
	}

	@Nonnull
	@Override
	public Sprite getSprite() {
		return originalModel.getSprite();
	}

	@Nonnull
	@Override
	public ModelTransformation getTransformation() {
		return originalModel.getTransformation();
	}

	@Override
	public boolean isSideLit() {
		return originalModel.isSideLit();
	}

	private final HashMap<Pair<Item, Boolean>, CompositeBakedModel> cache = new HashMap<>();

	private CompositeBakedModel getModel(ItemStack lens, boolean clip) {
		return cache.computeIfAbsent(Pair.of(lens.getItem(), clip), p -> new CompositeBakedModel(bakery, lens, clip ? originalModelClip : originalModel));
	}

	private static class CompositeBakedModel extends DelegatedModel {
		private final List<BakedQuad> genQuads = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		CompositeBakedModel(net.minecraft.client.render.model.ModelLoader bakery, ItemStack lens, BakedModel gun) {
			super(gun);

			Identifier lensId = Registry.ITEM.getId(lens.getItem());
			UnbakedModel lensUnbaked = bakery.getOrLoadModel(new ModelIdentifier(lensId, "inventory"));
			ModelBakeSettings transform = new SimpleModelTransform(new AffineTransformation(new Vector3f(-0.4F, 0.2F, 0.0F), Vector3f.POSITIVE_Y.getRadialQuaternion((float) Math.PI / 2), new Vector3f(0.625F, 0.625F, 0.625F), null));
			Identifier name = prefix("gun_with_" + lensId.toString().replace(':', '_'));

			BakedModel lensBaked;
			if (lensUnbaked instanceof JsonUnbakedModel && ((JsonUnbakedModel) lensUnbaked).getRootModel() == net.minecraft.client.render.model.ModelLoader.GENERATION_MARKER) {
				JsonUnbakedModel bm = (JsonUnbakedModel) lensUnbaked;
				lensBaked = new ItemModelGenerator()
						.create(ModelLoader.defaultTextureGetter(), bm)
						.bake(bakery, bm, ModelLoader.defaultTextureGetter(), transform, name, false);
			} else {
				lensBaked = lensUnbaked.bake(bakery, ModelLoader.defaultTextureGetter(), transform, name);
			}

			for (Direction e : Direction.values()) {
				faceQuads.put(e, new ArrayList<>());
			}

			Random rand = new Random(0);
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
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(BlockState state, Direction face, @Nonnull Random rand) {
			return face == null ? genQuads : faceQuads.get(face);
		}

		@Override
		public BakedModel handlePerspective(@Nonnull ModelTransformation.Mode cameraTransformType, MatrixStack stack) {
			super.handlePerspective(cameraTransformType, stack);
			return this;
		}
	}

}
