package vazkii.botania.client.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
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
		public IBakedModel getModelWithOverrides(IBakedModel model, ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
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
				IBakedModel lensModel = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(lens);
				return GunModel.this.getModel(lensModel);
			}
			else return GunModel.this;
		}
	};

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return itemHandler;
	}

	@Nonnull @Override public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, @Nonnull Random rand) { return originalModel.getQuads(state, side, rand); }
	@Override public boolean isAmbientOcclusion() { return originalModel.isAmbientOcclusion(); }
	@Override public boolean isGui3d() { return originalModel.isGui3d(); }
	@Override public boolean isBuiltInRenderer() { return originalModel.isBuiltInRenderer(); }
	@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return originalModel.getParticleTexture(); }
	@Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return originalModel.getItemCameraTransforms(); }

	private final IdentityHashMap<IBakedModel, CompositeBakedModel> cache = new IdentityHashMap<>();

	private CompositeBakedModel getModel(IBakedModel lens) {
		return cache.computeIfAbsent(lens, l -> new CompositeBakedModel(l, originalModel));
	}

	protected static BakedQuad transform(BakedQuad quad, final TRSRTransformation transform) {
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
		final IVertexConsumer consumer = new VertexTransformer(builder) {
			@Override
			public void put(int element, float... data) {
				VertexFormatElement formatElement = DefaultVertexFormats.ITEM.getElement(element);
				switch(formatElement.getUsage()) {
				case POSITION: {
					float[] newData = new float[4];
					Vector4f vec = new Vector4f(data);
					transform.getMatrixVec().transform(vec);
					vec.get(newData);
					parent.put(element, newData);
					break;
				}
				default: {
					parent.put(element, data);
					break;
				}
				}
			}
		};
		quad.pipe(consumer);
		return builder.build();
	}

	private static class CompositeBakedModel implements IBakedModel {

		private final IBakedModel gun;
		private final List<BakedQuad> genQuads;
		private final Map<EnumFacing, List<BakedQuad>> faceQuads = new EnumMap<>(EnumFacing.class);

		CompositeBakedModel(IBakedModel lens, IBakedModel gun) {
			this.gun = gun;

			ImmutableList.Builder<BakedQuad> genBuilder = ImmutableList.builder();
			final TRSRTransformation transform = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(-0.4F, 500, 0), null, new Vector3f(0.625F, 0.625F, 0.625F), TRSRTransformation.quatFromXYZ(0, (float) Math.PI / 2, 0)));

			for(EnumFacing e : EnumFacing.values())
				faceQuads.put(e, new ArrayList<>());

			Random rand = new Random(0);
			// Add lens quads, scaled and translated
			for(BakedQuad quad : lens.getQuads(null, null, rand)) {
				genBuilder.add(transform(quad, transform));
			}

			for(EnumFacing e : EnumFacing.values()) {
				rand.setSeed(0);
				faceQuads.get(e).addAll(lens.getQuads(null, e, rand).stream().map(input -> transform(input, transform)).collect(Collectors.toList()));
			}

			// Add gun quads
			rand.setSeed(0);
			genBuilder.addAll(gun.getQuads(null, null, rand));
			for(EnumFacing e : EnumFacing.values()) {
				rand.setSeed(0);
				faceQuads.get(e).addAll(gun.getQuads(null, e, rand));
			}

			genQuads = genBuilder.build();
		}

		@Nonnull @Override public List<BakedQuad> getQuads(IBlockState state, EnumFacing face, @Nonnull Random rand) { return face == null ? genQuads : faceQuads.get(face); }

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
