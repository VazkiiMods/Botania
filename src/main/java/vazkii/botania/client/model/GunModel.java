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
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import org.apache.commons.lang3.tuple.Pair;

import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.mixin.AccessorModelBakery;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;
import java.util.function.Function;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class GunModel implements BakedModel {
	private static final ModelResourceLocation DESU = new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun", "inventory");
	private static final ModelResourceLocation DESU_CLIP = new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun_clip", "inventory");

	private final net.minecraft.client.resources.model.ModelBakery bakery;
	private final BakedModel originalModel;
	private final BakedModel originalModelClip;

	public GunModel(net.minecraft.client.resources.model.ModelBakery bakery, BakedModel originalModel, BakedModel originalModelClip) {
		this.bakery = bakery;
		this.originalModel = Preconditions.checkNotNull(originalModel);
		this.originalModelClip = Preconditions.checkNotNull(originalModelClip);
	}

	private final ItemOverrides itemHandler = new ItemOverrides() {
		@Nonnull
		@Override
		public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel worldIn, @Nullable LivingEntity entityIn) {
			boolean clip = ItemManaGun.hasClip(stack);

			if (ItemManaGun.isSugoiKawaiiDesuNe(stack)) {
				return Minecraft.getInstance().getModelManager().getModel(clip ? DESU_CLIP : DESU);
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
	public ItemOverrides getOverrides() {
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
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean isCustomRenderer() {
		return originalModel.isCustomRenderer();
	}

	@Nonnull
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return originalModel.getParticleIcon();
	}

	@Nonnull
	@Override
	public ItemTransforms getTransforms() {
		return originalModel.getTransforms();
	}

	@Override
	public boolean usesBlockLight() {
		return originalModel.usesBlockLight();
	}

	private final HashMap<Pair<Item, Boolean>, CompositeBakedModel> cache = new HashMap<>();

	private CompositeBakedModel getModel(ItemStack lens, boolean clip) {
		return cache.computeIfAbsent(Pair.of(lens.getItem(), clip), p -> new CompositeBakedModel(bakery, lens, clip ? originalModelClip : originalModel));
	}

	private static class CompositeBakedModel extends DelegatedModel {
		private final List<BakedQuad> genQuads = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		CompositeBakedModel(net.minecraft.client.resources.model.ModelBakery bakery, ItemStack lens, BakedModel gun) {
			super(gun);

			ResourceLocation lensId = Registry.ITEM.getKey(lens.getItem());
			UnbakedModel lensUnbaked = bakery.getModel(new ModelResourceLocation(lensId, "inventory"));
			ModelState transform = new ModelState() {
				@Override
				public Transformation getRotation() {
					return new Transformation(new Vector3f(-0.4F, 0.2F, 0.0F), Vector3f.YP.rotation((float) Math.PI / 2), new Vector3f(0.625F, 0.625F, 0.625F), null);
				}
			};
			ResourceLocation name = prefix("gun_with_" + lensId.toString().replace(':', '_'));

			Function<Material, TextureAtlasSprite> textureGetter = ((AccessorModelBakery) bakery).getSpriteAtlasManager()::getSprite;
			BakedModel lensBaked;
			if (lensUnbaked instanceof BlockModel && ((BlockModel) lensUnbaked).getRootModel() == net.minecraft.client.resources.model.ModelBakery.GENERATION_MARKER) {
				BlockModel bm = (BlockModel) lensUnbaked;
				lensBaked = new ItemModelGenerator()
						.generateBlockModel(textureGetter, bm)
						.bake(bakery, bm, textureGetter, transform, name, false);
			} else {
				lensBaked = lensUnbaked.bake(bakery, textureGetter, transform, name);
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
	}

}
