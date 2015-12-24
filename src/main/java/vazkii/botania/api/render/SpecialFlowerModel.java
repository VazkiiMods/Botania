package vazkii.botania.api.render;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelCustomData;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.block.BlockSpecialFlower;

import javax.vecmath.Matrix4f;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SpecialFlowerModel implements IModelCustomData {

    /**
     * Internal implementation
     **/
    // SpecialFlowerModel for when there are no models registered for a subtile
    public static final SpecialFlowerModel INSTANCE = new SpecialFlowerModel(ImmutableMap.<Optional<Class<? extends SubTileEntity>>, ModelResourceLocation>of());
    // Models registered from the externel API thus far
    private static final Map<Class<? extends SubTileEntity>, ModelResourceLocation> queuedModels = Maps.newHashMap();
    private final ImmutableMap<Optional<Class<? extends SubTileEntity>>, ModelResourceLocation> models;


    public SpecialFlowerModel(ImmutableMap<Optional<Class<? extends SubTileEntity>>, ModelResourceLocation> models) {
        this.models = models;
    }

    /**
     * Register your model for the given subtile class here.
     * Call this DURING PREINIT. Calling it anytime after models have already baked does not guarantee that your model will work.
     * Your model json must specify key "tintindex" in all the faces it wants tint applied.
     * Tint is applied whenever a player recolors the flower using floral dye
     * todo 1.8.8 move this elsewhere or otherwise make it fit better with the rest of the subtile registration process
     *
     * @param subTileClass
     * @param model
     */
    public static void register(Class<? extends SubTileEntity> subTileClass, ModelResourceLocation model) {
        queuedModels.put(subTileClass, model);
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.<ResourceLocation>copyOf(models.values());
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableList.of();
    }

    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        // "Bake" the SpecialFlowerModel (in reality, just create another wrapper for all the delegate models)
        return new SpecialFlowerBakedModel(models, format, IPerspectiveAwareModel.MapWrapper.getTransforms(state));
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        // Load the base variant from blockstate json, and also add all the model paths we received from external API
        ImmutableMap.Builder<Optional<Class<? extends SubTileEntity>>, ModelResourceLocation> builder = ImmutableMap.builder();
        for (String key : customData.keySet()) {
            if ("base".equals(key)) {
                builder.put(Optional.<Class<? extends SubTileEntity>>absent(), getLocation(customData.get(key)));
            }
            for (Map.Entry<Class<? extends SubTileEntity>, ModelResourceLocation> e : queuedModels.entrySet()) {
                builder.put(Optional.<Class<? extends SubTileEntity>>of(e.getKey()), e.getValue());
            }
        }
        ImmutableMap<Optional<Class<? extends SubTileEntity>>, ModelResourceLocation> models = builder.build();
        if (models.isEmpty()) return INSTANCE;
        return new SpecialFlowerModel(models);
    }

    private ModelResourceLocation getLocation(String json) {
        JsonElement e = new JsonParser().parse(json);
        if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isString()) {
            return new ModelResourceLocation(e.getAsString());
        }
        FMLLog.severe("Expect ModelResourceLocation, got: ", json);
        return new ModelResourceLocation("builtin/missing", "missing");
    }

    public enum Loader implements ICustomModelLoader {
        INSTANCE {
            @Override
            public void onResourceManagerReload(IResourceManager resourceManager) {
            }

            @Override
            public boolean accepts(ResourceLocation modelLocation) {
                return "botania_SPECIAL".equals(modelLocation.getResourceDomain())
                        && "specialFlower".equals(modelLocation.getResourcePath());
            }

            @Override
            public IModel loadModel(ResourceLocation modelLocation) throws IOException {
                // Load a dummy model for now, all actual models added in process().
                return SpecialFlowerModel.INSTANCE;
            }
        }
    }

    public static class SpecialFlowerBakedModel implements IPerspectiveAwareModel, ISmartBlockModel {

        private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        private final ImmutableMap<Optional<Class<? extends SubTileEntity>>, ModelResourceLocation> models;
        private final VertexFormat vertexFormat;

        private IBakedModel baseModel;
        private ImmutableMap<Class<? extends SubTileEntity>, IBakedModel> bakedFlowerModels;
        private ImmutableMap<Optional<EnumFacing>, ImmutableList<BakedQuad>> quads;

        public SpecialFlowerBakedModel(ImmutableMap<Optional<Class<? extends SubTileEntity>>, ModelResourceLocation> models, VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> cameraTransforms) {
            this.models = models;
            this.vertexFormat = format;
            this.transforms = cameraTransforms;
        }

        private void refreshBakedModels() {
            if (baseModel == null) {
                ModelManager manager = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager();
                baseModel = getModel(manager, Optional.<Class<? extends SubTileEntity>>absent());

                ImmutableMap.Builder<Class<? extends SubTileEntity>, IBakedModel> builder = ImmutableMap.builder();
                for (Map.Entry<Optional<Class<? extends SubTileEntity>>, ModelResourceLocation> e : models.entrySet()) {
                    if (e.getKey().isPresent()) {
                        builder.put(e.getKey().get(), getModel(manager, e.getKey()));
                    }
                }

                bakedFlowerModels = builder.build();

                ImmutableMap.Builder<Optional<EnumFacing>, ImmutableList<BakedQuad>> quadBuilder = ImmutableMap.builder();
                quadBuilder.put(Optional.<EnumFacing>absent(), buildQuads(Optional.<EnumFacing>absent()));
                for (EnumFacing side : EnumFacing.values()) {
                    quadBuilder.put(Optional.of(side), buildQuads(Optional.of(side)));
                }
                quads = quadBuilder.build();
            }
        }

        private IBakedModel getModel(ModelManager manager, Optional<Class<? extends SubTileEntity>> layer) {
            ModelResourceLocation loc = models.get(layer);
            if (loc == null) {
                loc = new ModelResourceLocation("builtin/missing", "missing");
            }
            return manager.getModel(loc);
        }

        private ImmutableList<BakedQuad> buildQuads(Optional<EnumFacing> side) {
            ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
            for (IBakedModel model : bakedFlowerModels.values()) {
                if (side.isPresent()) {
                    builder.addAll(model.getFaceQuads(side.get()));
                } else {
                    builder.addAll(model.getGeneralQuads());
                }
            }
            return builder.build();
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing side) {
            refreshBakedModels();
            return quads.get(Optional.of(side));
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            refreshBakedModels();
            return quads.get(Optional.absent());
        }

        @Override
        public boolean isAmbientOcclusion() {
            refreshBakedModels();
            return baseModel.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            refreshBakedModels();
            return baseModel.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {
            refreshBakedModels();
            return baseModel.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            refreshBakedModels();
            return baseModel.getParticleTexture();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public IBakedModel handleBlockState(IBlockState state) {
            IExtendedBlockState extendedState = ((IExtendedBlockState) state);
            Class<? extends SubTileEntity> clazz = extendedState.getValue(BotaniaStateProps.SUBTILE_CLASS);
            return bakedFlowerModels.get(clazz);
        }

        @Override
        public VertexFormat getFormat() {
            return vertexFormat;
        }

        @Override
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, cameraTransformType);
        }
    }
}
