package vazkii.botania.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
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
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.BotaniaStateProps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Collection;
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
    public Collection<ResourceLocation> getTextures(@Nonnull Function<ResourceLocation, IUnbakedModel> modelGetter, @Nonnull Set<String> missingTextureErrors) {
        Set<ResourceLocation> ret = new HashSet<>();
        for (IUnbakedModel island : unbakedIslands.values()) {
            ret.addAll(island.getTextures(modelGetter, missingTextureErrors));
        }
        ret.addAll(unbakedFlower.getTextures(modelGetter, missingTextureErrors));
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

    private static class Baked extends BakedModelWrapper<IBakedModel> {
        private final Map<IFloatingFlower.IslandType, IBakedModel> islands;

        Baked(IBakedModel flower, Map<IFloatingFlower.IslandType, IBakedModel> islands) {
            super(flower);
            this.islands = islands;
        }

        @Nonnull
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
            List<BakedQuad> quads = super.getQuads(state, side, rand, extraData);
            IFloatingFlower.IslandType type = IFloatingFlower.IslandType.GRASS;
            if (extraData.hasProperty(BotaniaStateProps.FLOATING_DATA)) {
                type = extraData.getData(BotaniaStateProps.FLOATING_DATA).getIslandType();
            }
            quads.addAll(islands.get(type).getQuads(state, side, rand, extraData));
            return quads;
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
