/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.google.common.base.Function;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
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
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.IFloatingFlower;
import vazkii.botania.common.item.block.ItemBlockFloatingSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.List;
import java.util.stream.Collectors;

public class FloatingFlowerModel implements ISmartItemModel, ISmartBlockModel, IResourceManagerReloadListener {

    public static final FloatingFlowerModel INSTANCE = new FloatingFlowerModel();

    private static Table<IFloatingFlower.IslandType, String, CompositeBakedModel> CACHE = HashBasedTable.create();

    private static final String MUNDANE_PREFIX = "botania:shimmeringFlower_";

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        CACHE.clear();
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        // Items always have GRASS island
        IFloatingFlower.IslandType islandType = IFloatingFlower.IslandType.GRASS;
        String identifier;

        if (Block.getBlockFromItem(stack.getItem()) == ModBlocks.floatingSpecialFlower) {
            // Magic flower
            identifier = ItemBlockFloatingSpecialFlower.getType(stack);
        } else {
            // Mundane flower
            identifier = MUNDANE_PREFIX + stack.getItemDamage();
        }

        return getModel(islandType, identifier);
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        IFloatingFlower.IslandType islandType = state.getValue(BlockFloatingFlower.ISLAND_TYPE);
        String identifier;

        if (state.getBlock() == ModBlocks.floatingSpecialFlower) {
            // Magic flower
            IExtendedBlockState realState = ((IExtendedBlockState) state);
            identifier = realState.getValue(BotaniaStateProps.SUBTILE_ID);
        } else {
            // Mundane flower
            identifier = MUNDANE_PREFIX + state.getValue(BotaniaStateProps.COLOR).getMetadata();
        }

        return getModel(islandType, identifier);
    }

    // Get the model for this islandtype + flower type combination. If it's not cached already, generate it.
    private CompositeBakedModel getModel(IFloatingFlower.IslandType islandType, String identifier) {
        ModelManager modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();

        if (CACHE.contains(islandType, identifier)) {
            return CACHE.get(islandType, identifier);
        } else {
            IBakedModel islandModel = modelManager.getModel(new ModelResourceLocation("botania:miniIsland", "variant=" + islandType.getName()));
            IBakedModel flowerModel;

            if (identifier.startsWith(MUNDANE_PREFIX)) {
                int meta = Integer.parseInt(identifier.substring(identifier.indexOf(MUNDANE_PREFIX) + MUNDANE_PREFIX.length()));
                flowerModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(ModBlocks.shinyFlower, 1, meta));
            } else {
                flowerModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(ItemBlockSpecialFlower.ofType(identifier));
            }

            // Enhance!
            CompositeBakedModel model = new CompositeBakedModel(flowerModel, islandModel);
            CACHE.put(islandType, identifier, model);
            return model;
        }
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
                genBuilder.addAll(flower.getFaceQuads(e).stream().map(input -> transform(input, transform)).collect(Collectors.toList()));
            }

            // Add island quads
            genBuilder.addAll(island.getGeneralQuads());
            for (EnumFacing e: EnumFacing.VALUES) {
                genBuilder.addAll(island.getFaceQuads(e));
            }

            genQuads = genBuilder.build();
        }

        // Forward all to flower model
        @Override public List<BakedQuad> getGeneralQuads() {
            return genQuads;
        }
        @Override public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) { return ImmutableList.of(); }
        @Override public boolean isAmbientOcclusion() {
            return base.isAmbientOcclusion();
        }
        @Override public boolean isGui3d() {
            return true;
        }
        @Override public boolean isBuiltInRenderer() {
            return base.isBuiltInRenderer();
        }
        @Override public TextureAtlasSprite getParticleTexture() {
            return base.getParticleTexture();
        }
        @Override public ItemCameraTransforms getItemCameraTransforms() {
            return base.getItemCameraTransforms();
        }
    }

    // Dummy results
    @Override public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) { return ImmutableList.of(); }
    @Override public List<BakedQuad> getGeneralQuads() { return ImmutableList.of(); }
    @Override public boolean isAmbientOcclusion() { return false; }
    @Override public boolean isGui3d() { return true; }
    @Override public boolean isBuiltInRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleTexture() { return null; }
    @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }

}
