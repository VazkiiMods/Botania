package vazkii.botania.client.model;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemModelGenerator;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
		public IBakedModel getModelWithOverrides(IBakedModel model, ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
			boolean clip = ItemManaGun.hasClip(stack);

			if (ItemManaGun.isSugoiKawaiiDesuNe(stack)) {
				return Minecraft.getInstance().getModelManager().getModel(clip ? DESU_CLIP : DESU);
			}

			ItemStack lens = ItemManaGun.getLens(stack);
			if(!lens.isEmpty()) {
				return GunModel.this.getModel(lens, clip);
			} else return clip ? originalModelClip : originalModel;
		}
	};

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return itemHandler;
	}

	@Nonnull @Override public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) { return originalModel.getQuads(state, side, rand); }
	@Override public boolean isAmbientOcclusion() { return originalModel.isAmbientOcclusion(); }
	@Override public boolean isGui3d() { return originalModel.isGui3d(); }
	@Override public boolean isBuiltInRenderer() { return originalModel.isBuiltInRenderer(); }
	@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return originalModel.getParticleTexture(); }
	@Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return originalModel.getItemCameraTransforms(); }
	@Override public boolean isSideLit() { return originalModel.isSideLit(); }

	private final HashMap<Pair<Item, Boolean>, CompositeBakedModel> cache = new HashMap<>();

	private CompositeBakedModel getModel(ItemStack lens, boolean clip) {
		return cache.computeIfAbsent(Pair.of(lens.getItem(), clip), p -> new CompositeBakedModel(bakery, lens, clip ? originalModelClip : originalModel));
	}

	private static class CompositeBakedModel extends BakedModelWrapper<IBakedModel> {
		private static final BlockModel MODEL_GENERATED = ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class, null, "field_177606_o");

		private final List<BakedQuad> genQuads = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		CompositeBakedModel(ModelBakery bakery, ItemStack lens, IBakedModel gun) {
			super(gun);

			IUnbakedModel lensUnbaked = bakery.getUnbakedModel(new ModelResourceLocation(lens.getItem().getRegistryName(), "inventory"));
			IModelTransform transform = new SimpleModelTransform(new TransformationMatrix(new Vector3f(-0.4F, 0.2F, 0.0F), Vector3f.POSITIVE_Y.getRadialQuaternion((float) Math.PI / 2), new Vector3f(0.625F, 0.625F, 0.625F), null));
			ResourceLocation name = prefix("gun_with_" + lens.getItem().getRegistryName().toString().replace(':', '_'));

			IBakedModel lensBaked;
			if (lensUnbaked instanceof BlockModel && ((BlockModel) lensUnbaked).getRootModel() == MODEL_GENERATED) {
				BlockModel bm = (BlockModel) lensUnbaked;
				lensBaked = new ItemModelGenerator()
						.makeItemModel(ModelLoader.defaultTextureGetter(), bm)
						.bake(bakery, bm, ModelLoader.defaultTextureGetter(), transform, name, false);
			} else {
				lensBaked = lensUnbaked.bake(bakery, ModelLoader.defaultTextureGetter(), transform, name);
			}

			for(Direction e : Direction.values())
				faceQuads.put(e, new ArrayList<>());

			Random rand = new Random(0);
			genQuads.addAll(lensBaked.getQuads(null, null, rand));

			for(Direction e : Direction.values()) {
				rand.setSeed(0);
				faceQuads.get(e).addAll(lensBaked.getQuads(null, e, rand));
			}

			// Add gun quads
			rand.setSeed(0);
			genQuads.addAll(gun.getQuads(null, null, rand));
			for(Direction e : Direction.values()) {
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
