package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.vecmath.Matrix4f;
import java.util.List;

public class LexiconModel implements IPerspectiveAwareModel {
    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        ModelResourceLocation path = new ModelResourceLocation("botania:lexicon_default", "inventory");
        IBakedModel original = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(path);
        if(cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON && ConfigHandler.lexicon3dModel) {
            return Pair.of(this, null);
        }

        return Pair.of(new IFlexibleBakedModel.Wrapper(original, Attributes.DEFAULT_BAKED_FORMAT), new TRSRTransformation(original.getItemCameraTransforms().getTransform(cameraTransformType)).getMatrix());
    }

    @Override public VertexFormat getFormat() { return DefaultVertexFormats.ITEM; }
    @Override public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) { return ImmutableList.of(); }
    @Override public List<BakedQuad> getGeneralQuads() { return ImmutableList.of(); }
    @Override public boolean isAmbientOcclusion() { return false; }
    @Override public boolean isGui3d() { return false; }
    @Override public boolean isBuiltInRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleTexture() { return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("botania:items/lexicon"); }
    @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
}
