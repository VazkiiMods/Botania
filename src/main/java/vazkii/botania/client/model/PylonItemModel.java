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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.annotation.Immutable;
import org.apache.logging.log4j.Level;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.List;

public class PylonItemModel implements ISmartItemModel {

    private IBakedModel mana;
    private IBakedModel natura;
    private IBakedModel gaia;
    private boolean cached = false;

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        if (!cached) {
            cacheModels();
        }

        switch (stack.getItemDamage()) {
            case 2: return gaia;
            case 1: return natura;
            case 0: return mana;
            default: 
                return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel();
        }
    }

    private void cacheModels() {
        // Load the OBJ
        OBJModel model = null;
        try {
            model = ((OBJModel) OBJLoader.instance.loadModel(new ResourceLocation("botania:models/block/pylon.obj")));
        } catch (IOException e) {
            FMLLog.log(Level.ERROR, "[Botania]: Error loading pylon item model, substituting missing model");
            mana = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel();
            natura = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel();
            gaia = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel();
            cached = true;
            return;
        }

        // Apply the texture and flip the v's of the model
        IModel manaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon"))).process(ImmutableMap.of("flip-v", "true"));
        IModel naturaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon1"))).process(ImmutableMap.of("flip-v", "true"));
        IModel gaiaModel = ((OBJModel) model.retexture(ImmutableMap.of("#pylon", "botania:model/pylon2"))).process(ImmutableMap.of("flip-v", "true"));

        // All groups are off by default
        // OBJState constructor is derp, so the boolean condition is inverted - pass false to turn on a group

        final Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
            @Override
            public TextureAtlasSprite apply(ResourceLocation input) {
                return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
            }
        };

        ImmutableList<String> standardGroups = ImmutableList.of("Crystal", "Crystal_Ring",
                "Ring_Panel01", "Ring_Panel02", "Ring_Panel03", "Ring_Panel04",
                "Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04");

        mana = new PerspectiveWrapper(manaModel.bake(new OBJModel.OBJState(standardGroups, false), Attributes.DEFAULT_BAKED_FORMAT, textureGetter));
        // Natura model is just the crystal
        natura = new PerspectiveWrapper(naturaModel.bake(new OBJModel.OBJState(ImmutableList.of("Crystal"), false), Attributes.DEFAULT_BAKED_FORMAT, textureGetter));
        gaia = new PerspectiveWrapper(gaiaModel.bake(new OBJModel.OBJState(standardGroups, false), Attributes.DEFAULT_BAKED_FORMAT, textureGetter));
        cached = true;
    }

    @Override public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) { return ImmutableList.of(); }
    @Override public List<BakedQuad> getGeneralQuads() { return ImmutableList.of(); }
    @Override public boolean isAmbientOcclusion() { return false; }
    @Override public boolean isGui3d() { return true; }
    @Override public boolean isBuiltInRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleTexture() { return null; }
    @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }

    // Wrapper to transform the model before rendering it, so it looks right in the inventory
    private class PerspectiveWrapper implements IPerspectiveAwareModel {

        private final IFlexibleBakedModel parent;

        public PerspectiveWrapper(IBakedModel pylonModel) {
            this.parent = new IFlexibleBakedModel.Wrapper(pylonModel, Attributes.DEFAULT_BAKED_FORMAT);
        }

        @Override
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            switch(cameraTransformType) {
                case FIRST_PERSON: return ImmutablePair.of(parent, new TRSRTransformation(new Vector3f(0, 0, 0.4F), null, new Vector3f(0.75F, 0.75F, 0.75F), null).getMatrix());
                case GUI: return ImmutablePair.of(parent, TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(0.5F, 0.1F, 0), null, null, null)).getMatrix());
                case THIRD_PERSON: return ImmutablePair.of(parent, new TRSRTransformation(new Vector3f(0, -0.2F, 0.5F), null, null, null).getMatrix());
                default: return ImmutablePair.of(parent, TRSRTransformation.identity().getMatrix());
            }
        }

        @Override public VertexFormat getFormat() { return Attributes.DEFAULT_BAKED_FORMAT; }
        @Override public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) { return parent.getFaceQuads(p_177551_1_); }
        @Override public List<BakedQuad> getGeneralQuads() { return parent.getGeneralQuads(); }
        @Override public boolean isAmbientOcclusion() { return parent.isAmbientOcclusion(); }
        @Override public boolean isGui3d() { return parent.isGui3d(); }
        @Override public boolean isBuiltInRenderer() { return parent.isBuiltInRenderer(); }
        @Override public TextureAtlasSprite getParticleTexture() { return parent.getParticleTexture(); }
        @Override public ItemCameraTransforms getItemCameraTransforms() { return parent.getItemCameraTransforms(); }
    }
}
