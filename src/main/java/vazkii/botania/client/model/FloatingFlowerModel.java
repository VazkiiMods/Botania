package vazkii.botania.client.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.property.IExtendedBlockState;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.decor.IFloatingFlower;
import vazkii.botania.common.item.block.ItemBlockFloatingSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.List;

public class FloatingFlowerModel implements ISmartItemModel, ISmartBlockModel {
    public static final FloatingFlowerModel INSTANCE = new FloatingFlowerModel();

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        ModelManager modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();

        // Item form always has island type grass.
        IBakedModel islandType = modelManager.getModel(new ModelResourceLocation("botania:miniIsland", "variant=grass"));

        IBakedModel flowerModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(ItemBlockSpecialFlower.ofType(ItemBlockFloatingSpecialFlower.getType(stack)));

        return new CompositeBakedModel(flowerModel, islandType);
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        IExtendedBlockState realState = ((IExtendedBlockState) state);

        ModelManager modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();

        // Item form always has island type grass.
        IBakedModel islandType = modelManager.getModel(new ModelResourceLocation("botania:miniIsland", "variant=" + ((IFloatingFlower.IslandType) realState.getValue(BotaniaStateProps.ISLAND_TYPE)).getName()));

        IBakedModel flowerModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(ItemBlockSpecialFlower.ofType(realState.getValue(BotaniaStateProps.SUBTILE_ID)));

        return new CompositeBakedModel(flowerModel, islandType);
    }

    protected static BakedQuad transform(BakedQuad quad, final TRSRTransformation transform) {
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
        final IVertexConsumer consumer = new VertexTransformer(builder) {
            @Override
            public void put(int element, float... data) {
                VertexFormatElement formatElement = DefaultVertexFormats.ITEM.getElement(element);
                switch (formatElement.getUsage()) {
                    case POSITION: {
                        float[] newData = new float[4];
                        Vector4f vec = new Vector4f(data);
                        transform.getMatrix().transform(vec);
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

        private final IBakedModel base;
        private final List<BakedQuad> genQuads;

        public CompositeBakedModel(IBakedModel flower, IBakedModel island) {
            this.base = flower;

            ImmutableList.Builder<BakedQuad> genBuilder = ImmutableList.builder();
            final TRSRTransformation transform = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null));


            // Add flower quads, scaled and translated
            for (BakedQuad quad : flower.getGeneralQuads()) {
                genBuilder.add(transform(quad, transform));
            }

            for (EnumFacing e : EnumFacing.VALUES) {
                genBuilder.addAll(Iterables.transform(flower.getFaceQuads(e), new Function<BakedQuad, BakedQuad>() {
                    @Override public BakedQuad apply(BakedQuad input) { return transform(input, transform); }
                }));
            }

            // Add island quads
            genBuilder.addAll(island.getGeneralQuads());
            for (EnumFacing e: EnumFacing.VALUES) {
                genBuilder.addAll(island.getFaceQuads(e));
            }

            genQuads = genBuilder.build();
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            return genQuads;
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) { return ImmutableList.of(); }

        @Override
        public boolean isAmbientOcclusion() {
            return base.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return base.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return base.getParticleTexture();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return base.getItemCameraTransforms();
        }
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) { return ImmutableList.of(); }

    @Override
    public List<BakedQuad> getGeneralQuads() { return ImmutableList.of(); }

    @Override
    public boolean isAmbientOcclusion() { return false; }

    @Override
    public boolean isGui3d() { return true; }

    @Override
    public boolean isBuiltInRenderer() { return false; }

    @Override
    public TextureAtlasSprite getParticleTexture() { return null; }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
}
