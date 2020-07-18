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
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
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

public class GunModel implements IBakedModel {
	private static final ModelResourceLocation DESU = new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun", "inventory");
	private static final ModelResourceLocation DESU_CLIP = new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun_clip", "inventory");

	private final ModelBakery bakery;
	private final IBakedModel originalModel;
	private final IBakedModel originalModelClip;

	public GunModel(ModelBakery bakery, IBakedModel originalModel, IBakedModel originalModelClip) {
		this.bakery = bakery;
		this.originalModel = Preconditions.checkNotNull(originalModel);
		this.originalModelClip = Preconditions.checkNotNull(originalModelClip);
	}

	private final ItemOverrideList itemHandler = new ItemOverrideList() {
		@Nonnull
		@Override
		public IBakedModel func_239290_a_(IBakedModel model, ItemStack stack, @Nullable ClientWorld worldIn, @Nullable LivingEntity entityIn) {
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
	public ItemOverrideList getOverrides() {
		return itemHandler;
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
		return originalModel.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return originalModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return originalModel.isBuiltInRenderer();
	}

	@Nonnull
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return originalModel.getParticleTexture();
	}

	@Nonnull
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return originalModel.getItemCameraTransforms();
	}

	@Override
	public boolean func_230044_c_() {
		return originalModel.func_230044_c_();
	}

	private final HashMap<Pair<Item, Boolean>, CompositeBakedModel> cache = new HashMap<>();

	private CompositeBakedModel getModel(ItemStack lens, boolean clip) {
		return cache.computeIfAbsent(Pair.of(lens.getItem(), clip), p -> new CompositeBakedModel(bakery, lens, clip ? originalModelClip : originalModel));
	}

	private static class CompositeBakedModel extends DelegatedModel {
		private final List<BakedQuad> genQuads = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		CompositeBakedModel(ModelBakery bakery, ItemStack lens, IBakedModel gun) {
			super(gun);

			ResourceLocation lensId = Registry.ITEM.getKey(lens.getItem());
			IUnbakedModel lensUnbaked = bakery.getUnbakedModel(new ModelResourceLocation(lensId, "inventory"));
			IModelTransform transform = new SimpleModelTransform(new TransformationMatrix(new Vector3f(-0.4F, 0.2F, 0.0F), Vector3f.YP.rotation((float) Math.PI / 2), new Vector3f(0.625F, 0.625F, 0.625F), null));
			ResourceLocation name = prefix("gun_with_" + lensId.toString().replace(':', '_'));

			IBakedModel lensBaked;
			if (lensUnbaked instanceof BlockModel && ((BlockModel) lensUnbaked).getRootModel() == ModelBakery.MODEL_GENERATED) {
				BlockModel bm = (BlockModel) lensUnbaked;
				lensBaked = new ItemModelGenerator()
						.makeItemModel(ModelLoader.defaultTextureGetter(), bm)
						.bakeModel(bakery, bm, ModelLoader.defaultTextureGetter(), transform, name, false);
			} else {
				lensBaked = lensUnbaked.bakeModel(bakery, ModelLoader.defaultTextureGetter(), transform, name);
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
		public IBakedModel handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType, MatrixStack stack) {
			super.handlePerspective(cameraTransformType, stack);
			return this;
		}
	}

}
