package vazkii.botania.client.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.common.item.ItemManaGun;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GunModel implements IBakedModel {

	private final IBakedModel originalModel;

	public GunModel(IBakedModel originalModel) {
		this.originalModel = Preconditions.checkNotNull(originalModel);
	}

	private final ItemOverrideList itemHandler = new ItemOverrideList() {
		@Nonnull
		@Override
		public IBakedModel getModelWithOverrides(IBakedModel model, ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
			// TODO 1.13 this faffing around with double overrides is dumb. Move back from json overrides once a "load this model please" PR gets merged?

			// First, let json overrides on original model apply. This gets us to the clip GunModel instance
			IBakedModel jsonOverride = originalModel.getOverrides().getModelWithOverrides(originalModel, stack, worldIn, entityIn);
			if (jsonOverride != null && jsonOverride != originalModel) {
				// Now, apply overrides again. This will add lenses.
				return jsonOverride.getOverrides().getModelWithOverrides(jsonOverride, stack, worldIn, entityIn);
			}

			// Done doing override stuff, look up our composite models
			ItemStack lens = ItemManaGun.getLens(stack);
			if(!lens.isEmpty()) {
				ModelResourceLocation mrl = new ModelResourceLocation(lens.getItem().getRegistryName(), "inventory");
				IUnbakedModel unbaked = ModelLoaderRegistry.getModelOrMissing(mrl);
				return GunModel.this.getModel(unbaked);
			}
			else return GunModel.this;
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

	private final IdentityHashMap<IUnbakedModel, CompositeBakedModel> cache = new IdentityHashMap<>();

	private CompositeBakedModel getModel(IUnbakedModel lens) {
		return cache.computeIfAbsent(lens, l -> new CompositeBakedModel(l, originalModel));
	}

	private static class CompositeBakedModel implements IBakedModel {

		private final IBakedModel gun;
		private final List<BakedQuad> genQuads = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		CompositeBakedModel(IUnbakedModel lensUnbaked, IBakedModel gun) {
			this.gun = gun;

			final TRSRTransformation transform = new TRSRTransformation(new Vector3f(-0.2F, 0.4F, 0.8F), TRSRTransformation.quatFromXYZ(0, (float) Math.PI / 2, 0), new Vector3f(0.625F, 0.625F, 0.625F), null);
			IBakedModel lens = lensUnbaked.bake(ModelLoader.defaultModelGetter(), ModelLoader.defaultTextureGetter(), transform, false, DefaultVertexFormats.ITEM);

			for(Direction e : Direction.values())
				faceQuads.put(e, new ArrayList<>());

			Random rand = new Random(0);
			genQuads.addAll(lens.getQuads(null, null, rand));

			for(Direction e : Direction.values()) {
				rand.setSeed(0);
				faceQuads.get(e).addAll(lens.getQuads(null, e, rand));
			}

			// Add gun quads
			rand.setSeed(0);
			genQuads.addAll(gun.getQuads(null, null, rand));
			for(Direction e : Direction.values()) {
				rand.setSeed(0);
				faceQuads.get(e).addAll(gun.getQuads(null, e, rand));
			}
		}

		@Nonnull @Override public List<BakedQuad> getQuads(BlockState state, Direction face, @Nonnull Random rand) { return face == null ? genQuads : faceQuads.get(face); }

		// Forward all to gun model
		@Override public boolean isAmbientOcclusion() { return gun.isAmbientOcclusion(); }
		@Override public boolean isGui3d() { return gun.isGui3d(); }
		@Override public boolean isBuiltInRenderer() { return gun.isBuiltInRenderer(); }
		@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return gun.getParticleTexture();}
		@Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return gun.getItemCameraTransforms(); }
		@Nonnull @Override public ItemOverrideList getOverrides() { return ItemOverrideList.EMPTY; }

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType) {
			Pair<? extends IBakedModel, Matrix4f> pair = gun.handlePerspective(cameraTransformType);
			if(pair != null && pair.getRight() != null)
				return Pair.of(this, pair.getRight());
			return Pair.of(this, TRSRTransformation.identity().getMatrixVec());
		}
	}

}
