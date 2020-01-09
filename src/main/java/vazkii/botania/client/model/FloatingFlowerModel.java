package vazkii.botania.client.model;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.BotaniaStateProps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class FloatingFlowerModel implements IUnbakedModel {
    private static final FloatingFlowerModel INIT = new FloatingFlowerModel(null);

    @Nullable
    private final ResourceLocation flower;

    @Nullable
    private IUnbakedModel unbakedFlower = null;

    private final Map<IFloatingFlower.IslandType, IUnbakedModel> unbakedIslands = new HashMap<>();

    private FloatingFlowerModel(@Nullable ResourceLocation flower) {
        this.flower = flower;
        if (flower != null)
            this.unbakedFlower = ModelLoaderRegistry.getModelOrMissing(flower);
        for (Map.Entry<IFloatingFlower.IslandType, ResourceLocation> e : BotaniaAPIClient.getRegisteredIslandTypeModels().entrySet()) {
            IUnbakedModel unbakedIsland = ModelLoaderRegistry.getModelOrMissing(e.getValue());
            unbakedIslands.put(e.getKey(), unbakedIsland);
        }
    }

    @Nonnull
    @Override
    public Collection<ResourceLocation> getDependencies() {
        List<ResourceLocation> ret = new ArrayList<>(BotaniaAPIClient.getRegisteredIslandTypeModels().values());
        if (flower != null)
            ret.add(flower);
        return ret;
    }

    @Nonnull
    @Override
    public Collection<Material> getTextureDependencies(Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors){
        Set<Material> ret = new HashSet<>();
        for (IUnbakedModel island : unbakedIslands.values()) {
            ret.addAll(island.getTextureDependencies(modelGetter, missingTextureErrors));
        }
        ret.addAll(unbakedFlower.getTextureDependencies(modelGetter, missingTextureErrors));
        return ret;
    }

    @Nonnull
    @Override
    public IUnbakedModel process(ImmutableMap<String, String> customData) {
        // Forge is big dumb and passes this in *with the quote markers*...
        String flowerPath = customData.get("flower");
        return new FloatingFlowerModel(new ResourceLocation(flowerPath.substring(1, flowerPath.length() - 1)));
    }

    @Nullable
    @Override
    public IBakedModel bake(ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format) {
        final TRSRTransformation transform = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null));
        IModelState comp = new ModelStateComposition(transform, sprite.getState());
        IBakedModel bakedFlower = unbakedFlower.bake(bakery, spriteGetter, new BasicState(comp, false), format);

        Map<IFloatingFlower.IslandType, IBakedModel> bakedIslands = new HashMap<>();
        for (Map.Entry<IFloatingFlower.IslandType, IUnbakedModel> e : unbakedIslands.entrySet()) {
            IBakedModel bakedIsland = e.getValue().bake(bakery, spriteGetter, sprite, format);
            bakedIslands.put(e.getKey(), bakedIsland);
        }
        return new Baked(bakedFlower, bakedIslands);
    }

    public static class Baked extends BakedModelWrapper<IBakedModel> {
        private final Map<IFloatingFlower.IslandType, IBakedModel> islands;
        private final Map<IFloatingFlower.IslandType, List<BakedQuad>> genQuads = new HashMap<>();
        private final Map<IFloatingFlower.IslandType, Map<Direction, List<BakedQuad>>> faceQuads = new HashMap<>();

        Baked(IBakedModel flower, Map<IFloatingFlower.IslandType, IBakedModel> islands) {
            super(flower);
            this.islands = islands;
            Random rand = new Random();
            for (Map.Entry<IFloatingFlower.IslandType, IBakedModel> e : islands.entrySet()) {
                rand.setSeed(42);
                List<BakedQuad> gen = new ArrayList<>(flower.getQuads(null, null, rand, EmptyModelData.INSTANCE));
                rand.setSeed(42);
                gen.addAll(e.getValue().getQuads(null, null, rand, EmptyModelData.INSTANCE));
                genQuads.put(e.getKey(), gen);

                Map<Direction, List<BakedQuad>> fq = new EnumMap<>(Direction.class);
                for (Direction dir : Direction.values()) {
                    rand.setSeed(42);
                    List<BakedQuad> lst = new ArrayList<>(flower.getQuads(null, dir, rand, EmptyModelData.INSTANCE));
                    rand.setSeed(42);
                    lst.addAll(e.getValue().getQuads(null, dir, rand, EmptyModelData.INSTANCE));
                    fq.put(dir, lst);
                }
                faceQuads.put(e.getKey(), fq);
            }
        }

        @Nonnull
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
            // Default to GRASS island
            return getQuads(state, side, rand, EmptyModelData.INSTANCE);
        }

        @Nonnull
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
            IFloatingFlower.IslandType type = IFloatingFlower.IslandType.GRASS;
            if (extraData.hasProperty(BotaniaStateProps.FLOATING_DATA)) {
                type = extraData.getData(BotaniaStateProps.FLOATING_DATA).getIslandType();
            }

            if (side == null) {
                return genQuads.get(type);
            } else {
                return faceQuads.get(type).get(side);
            }
        }

        @Nonnull
        @Override
        public IBakedModel handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType, MatrixStack ms) {
            // Use the item transforms from the islands since it looks better
            this.islands.values().iterator().next().handlePerspective(cameraTransformType, ms);
            return this;
        }
    }

    public enum Loader implements ICustomModelLoader {
        INSTANCE;

        @Override
        public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {}

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getNamespace().equals("botania_special") && modelLocation.getPath().equals("models/floating_flower");
        }

        @Nonnull
        @Override
        public IUnbakedModel loadModel(@Nonnull ResourceLocation modelLocation) {
            return FloatingFlowerModel.INIT;
        }
    }
}
