package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;
import vazkii.botania.common.item.block.ItemBlockFloatingSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import java.util.List;

public class FloatingFlowerModel implements ISmartItemModel {
    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        ModelManager modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();

        // Item form always has island type grass.
        IBakedModel islandType = modelManager.getModel(new ModelResourceLocation("botania:miniIsland", "variant=grass"));

        IBakedModel flowerModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(ItemBlockSpecialFlower.ofType(ItemBlockFloatingSpecialFlower.getType(stack)));


        return new CompositeBakedModel(flowerModel, islandType);
    }

    private class CompositeBakedModel implements IBakedModel {

        private final IBakedModel base;
        private final List<IBakedModel> compose;

        public CompositeBakedModel(IBakedModel base, IBakedModel... compose) {
            this.base = base;
            this.compose = ImmutableList.copyOf(compose);
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) {
            ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
            builder.addAll(base.getFaceQuads(p_177551_1_));
            for (IBakedModel model : compose) {
                builder.addAll(model.getFaceQuads(p_177551_1_));
            }
            return builder.build();
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
            builder.addAll(base.getGeneralQuads());
            for (IBakedModel model : compose) {
                builder.addAll(model.getGeneralQuads());
            }
            return builder.build();

        }

        @Override
        public boolean isAmbientOcclusion() {
            return base.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return base.isGui3d();
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
