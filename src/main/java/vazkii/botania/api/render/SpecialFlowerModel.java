package vazkii.botania.api.render;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelCustomData;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.block.decor.IFloatingFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.vecmath.Matrix4f;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SpecialFlowerModel implements IModelCustomData {

    /**
     * Register your model for the given subtile class here.
     * Call this DURING PREINIT. Calling it anytime after blockModels have already baked does not guarantee that your model will work.
     * Your model json must specify key "tintindex" in all the faces it wants tint applied.
     * Tint is applied whenever a player recolors the flower using floral dye
     * todo 1.8.8 move this elsewhere or otherwise make it fit better with the rest of the subtile registration process
     *
     * @param subTileName The String ID of the subtile
     * @param model A path to a blockstate json and variant to be used for this subtile
     * @param itemModel A path to a blockstate json and variant to be used for this subtile's item form
     */
    public static void register(String subTileName, ModelResourceLocation model, ModelResourceLocation itemModel) {
        queuedBlockModels.put(subTileName, model);
        queuedItemModels.put(subTileName, itemModel);
    }

    /**
     * Register your model for the given subtile class here.
     * Call this DURING PREINIT. Calling it anytime after blockModels have already baked does not guarantee that your model will work.
     * Your model json must specify key "tintindex" in all the faces it wants tint applied.
     * Tint is applied whenever a player recolors the flower using floral dye
     * todo 1.8.8 move this elsewhere or otherwise make it fit better with the rest of the subtile registration process
     *
     * @param subTileName The String ID of the subtile
     * @param model A path to a blockstate json and variant to be used the block. The item model will be drawn from the same blockstate json, from variant "inventory"
     */
    public static void register(String subTileName, ModelResourceLocation model) {
        register(subTileName, model, new ModelResourceLocation(model.getResourceDomain() + ":" + model.getResourcePath(), "inventory"));
    }

    public static void register(Class<? extends SubTileEntity> clazz, ModelResourceLocation model) {
        register(BotaniaAPI.getSubTileStringMapping(clazz), model);
    }

    public static void register(Class<? extends SubTileEntity> clazz, ModelResourceLocation model, ModelResourceLocation itemModel) {
        register(BotaniaAPI.getSubTileStringMapping(clazz), model, itemModel);
    }

    /**
     * Internal implementation
     **/
    // SpecialFlowerModel for when there are no blockModels registered for a subtile
    public static final SpecialFlowerModel INSTANCE = new SpecialFlowerModel(ImmutableMap.<Optional<String>, ModelResourceLocation>of(), ImmutableMap.<Optional<String>, ModelResourceLocation>of());
    // Models registered from the externel API thus far
    private static final Map<String, ModelResourceLocation> queuedBlockModels = Maps.newHashMap();
    private static final Map<String, ModelResourceLocation> queuedItemModels = Maps.newHashMap();


    private final ImmutableMap<Optional<String>, ModelResourceLocation> blockModels;
    private final ImmutableMap<Optional<String>, ModelResourceLocation> itemModels;

    public SpecialFlowerModel(ImmutableMap<Optional<String>, ModelResourceLocation> blockModels,
                              ImmutableMap<Optional<String>, ModelResourceLocation> itemModels) {
        this.blockModels = blockModels;
        this.itemModels = itemModels;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.addAll(blockModels.values());
        builder.addAll(itemModels.values());

        // Force mini island model to be loaded and baked, for use elsewhere. See <TODO>
        for (IFloatingFlower.IslandType i : IFloatingFlower.IslandType.values()) {
            builder.add(new ModelResourceLocation("botania:miniIsland", "variant=" + i.name().toLowerCase(Locale.ROOT)));
        }

        return builder.build();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableList.of();
    }

    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        // "Bake" the SpecialFlowerModel (in reality, just create another wrapper for all the delegate blockModels)
        return new SpecialFlowerBakedModel(blockModels, itemModels, format, IPerspectiveAwareModel.MapWrapper.getTransforms(state));
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        // Load the base variant from blockstate json, and also add all the model paths we received from external API
        ImmutableMap.Builder<Optional<String>, ModelResourceLocation> blockBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<Optional<String>, ModelResourceLocation> itemBuilder = ImmutableMap.builder();

        for (String key : customData.keySet()) {
            if ("base".equals(key)) {
                blockBuilder.put(Optional.<String>absent(), getLocation(customData.get(key)));
            }
        }

        for (Map.Entry<String, ModelResourceLocation> e : queuedBlockModels.entrySet()) {
            blockBuilder.put(Optional.of(e.getKey()), e.getValue());
        }
        for (Map.Entry<String, ModelResourceLocation> e : queuedItemModels.entrySet()) {
            itemBuilder.put(Optional.of(e.getKey()), e.getValue());
        }

        ImmutableMap<Optional<String>, ModelResourceLocation> blockModels = blockBuilder.build();
        ImmutableMap<Optional<String>, ModelResourceLocation> itemModels = itemBuilder.build();
        if (blockModels.isEmpty() && itemModels.isEmpty()) return INSTANCE;
        return new SpecialFlowerModel(blockModels, itemModels);
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
                return modelLocation.getResourceDomain().equals("botania_special") && (
                        modelLocation.getResourcePath().equals("specialFlower") ||
                                modelLocation.getResourcePath().equals("models/block/specialFlower") ||
                                modelLocation.getResourcePath().equals("models/item/specialFlower"));
            }

            @Override
            public IModel loadModel(ResourceLocation modelLocation) throws IOException {
                // Load a dummy model for now, all actual blockModels added in process().
                return SpecialFlowerModel.INSTANCE;
            }
        }
    }

    public static class SpecialFlowerBakedModel implements IPerspectiveAwareModel, ISmartBlockModel, ISmartItemModel {

        private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        private final ImmutableMap<Optional<String>, ModelResourceLocation> blockModels;
        private final ImmutableMap<Optional<String>, ModelResourceLocation> itemModels;
        private final VertexFormat vertexFormat;

        private IBakedModel baseModel;
        private ImmutableMap<String, IBakedModel> bakedBlockModels;
        private ImmutableMap<String, IBakedModel> bakedItemModels;
        private ImmutableMap<Optional<EnumFacing>, ImmutableList<BakedQuad>> quads;

        public SpecialFlowerBakedModel(ImmutableMap<Optional<String>, ModelResourceLocation> blockModels,
                                       ImmutableMap<Optional<String>, ModelResourceLocation> itemModels,
                                       VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> cameraTransforms) {
            this.blockModels = blockModels;
            this.itemModels = itemModels;
            this.vertexFormat = format;
            this.transforms = cameraTransforms;
        }

        private void refreshBakedModels() {
            if (baseModel == null) {
                ModelManager manager = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager();
                baseModel = getBlockModel(manager, Optional.<String>absent());

                ImmutableMap.Builder<String, IBakedModel> builder = ImmutableMap.builder();
                for (Map.Entry<Optional<String>, ModelResourceLocation> e : blockModels.entrySet()) {
                    if (e.getKey().isPresent()) {
                        builder.put(e.getKey().get(), getBlockModel(manager, e.getKey()));
                    }
                }

                bakedBlockModels = builder.build();

                ImmutableMap.Builder<String, IBakedModel> builder2 = ImmutableMap.builder();
                for (Map.Entry<Optional<String>, ModelResourceLocation> e : itemModels.entrySet()) {
                    if (e.getKey().isPresent()) {
                        builder2.put(e.getKey().get(), getItemModel(manager, e.getKey()));
                    }
                }

                bakedItemModels = builder2.build();

                ImmutableMap.Builder<Optional<EnumFacing>, ImmutableList<BakedQuad>> quadBuilder = ImmutableMap.builder();
                quadBuilder.put(Optional.<EnumFacing>absent(), buildQuads(Optional.<EnumFacing>absent()));
                for (EnumFacing side : EnumFacing.values()) {
                    quadBuilder.put(Optional.of(side), buildQuads(Optional.of(side)));
                }
                quads = quadBuilder.build();
            }
        }

        private IBakedModel getBlockModel(ModelManager manager, Optional<String> opt) {
            ModelResourceLocation loc = blockModels.get(opt);
            if (loc == null) {
                loc = new ModelResourceLocation("builtin/missing", "missing");
            }
            return manager.getModel(loc);
        }

        private IBakedModel getItemModel(ModelManager manager, Optional<String> opt) {
            ModelResourceLocation loc = itemModels.get(opt);
            if (loc == null) {
                loc = new ModelResourceLocation("builtin/missing", "missing");
            }
            return manager.getModel(loc);
        }

        private ImmutableList<BakedQuad> buildQuads(Optional<EnumFacing> side) {
            ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
            for (IBakedModel model : bakedBlockModels.values()) {
                if (side.isPresent()) {
                    builder.addAll(model.getFaceQuads(side.get()));
                } else {
                    builder.addAll(model.getGeneralQuads());
                }
            }

            for (IBakedModel model : bakedItemModels.values()) {
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
            return quads.get(Optional.<EnumFacing>absent());
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
            refreshBakedModels();
            IExtendedBlockState extendedState = ((IExtendedBlockState) state);
            IBakedModel ret = bakedBlockModels.get(extendedState.getValue(BotaniaStateProps.SUBTILE_ID));
            if (ret == null) {
                System.out.println("Warning: no flower model for subtile: " + extendedState.getValue(BotaniaStateProps.SUBTILE_ID));
            }
            return ret == null ? Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel() : ret;
        }

        @Override
        public VertexFormat getFormat() {
            return vertexFormat;
        }

        @Override
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, cameraTransformType);
        }

        @Override
        public IBakedModel handleItemState(ItemStack stack) {
            refreshBakedModels();
            //System.out.println("calling smart item");
            IBakedModel item = bakedItemModels.get(ItemBlockSpecialFlower.getType(stack));
            if (item == null) {
                System.out.println("Missing item model, selecting block model");
                item = bakedBlockModels.get(ItemBlockSpecialFlower.getType(stack));
            }

            return item == null ? Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel() : item;
        }
    }
}
