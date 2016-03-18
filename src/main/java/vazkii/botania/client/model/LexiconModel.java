package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.vecmath.Matrix4f;
import java.util.List;

@SuppressWarnings("deprecation")
public class LexiconModel implements IPerspectiveAwareModel {

    private static final ModelResourceLocation path = new ModelResourceLocation("botania:lexicon_default", "inventory");

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        IBakedModel original = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(path);
        if(cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON && ConfigHandler.lexicon3dModel)
            return Pair.of(this, null);
        return ((IPerspectiveAwareModel) original).handlePerspective(cameraTransformType);
    }

    @Override public VertexFormat getFormat() { return Attributes.DEFAULT_BAKED_FORMAT; }
    @Override public List<BakedQuad> getFaceQuads(EnumFacing face) { return ImmutableList.of(); }
    @Override public List<BakedQuad> getGeneralQuads() { return ImmutableList.of(); }
    @Override public boolean isAmbientOcclusion() { return false; }
    @Override public boolean isGui3d() { return false; }
    @Override public boolean isBuiltInRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleTexture() { return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("botania:items/lexicon"); }
    @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
}
