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
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.SimpleModelTransform;
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
				// TODO 1.14 : currently broken because it goes through the vanilla codepath which doesn't handle builtin/generated models
				// todo 1.15 : check above statement lol
				ResourceLocation loc = new ResourceLocation(lens.getItem().getRegistryName().getNamespace(), "models/item/" + lens.getItem().getRegistryName().getPath() + ".json");
				IUnbakedModel unbaked;
				try (IResource json = Minecraft.getInstance().getResourceManager().getResource(loc)) {
					unbaked = BlockModel.deserialize(new InputStreamReader(json.getInputStream()));
				} catch (IOException e) {
					Botania.LOGGER.error("Missing flower model {}", loc);
					unbaked = BlockModel.deserialize(ModelBakery.MISSING_MODEL_MESH);
				}
				return GunModel.this.getModel(unbaked, clip);
			}
			else return clip ? originalModelClip : originalModel;
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

	private final HashMap<Pair<IUnbakedModel, Boolean>, CompositeBakedModel> cache = new HashMap<>();

	private CompositeBakedModel getModel(IUnbakedModel lens, boolean clip) {
		return cache.computeIfAbsent(Pair.of(lens, clip), p -> new CompositeBakedModel(bakery, p.getLeft(), p.getRight() ? originalModelClip : originalModel));
	}

	private static class CompositeBakedModel implements IBakedModel {

		private final IBakedModel gun;
		private final List<BakedQuad> genQuads = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		CompositeBakedModel(ModelBakery bakery, IUnbakedModel lensUnbaked, IBakedModel gun) {
			this.gun = gun;

			final TransformationMatrix transform = new TransformationMatrix(new Vector3f(-0.2F, 0.4F, 0.8F), Vector3f.POSITIVE_Y.getDegreesQuaternion((float) Math.PI / 2), new Vector3f(0.625F, 0.625F, 0.625F), null);
			IBakedModel lens = lensUnbaked.bake(bakery, ModelLoader.defaultTextureGetter(), new SimpleModelTransform(transform), prefix("gun_lens_" + lensUnbaked.toString()));

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
		public IBakedModel handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType, MatrixStack stack) {
			// apply gun transforms to stack but use self model still
			gun.handlePerspective(cameraTransformType, stack);
			return this;
		}
	}

}
