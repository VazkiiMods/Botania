package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.mana.TilePump;

public class RenderTilePump extends TileEntityRenderer<TilePump> {
    public static IBakedModel headModel = null;

    @Override
    public void render(TilePump pump, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        GlStateManager.translated(0.5, 0, 0.5);
        switch (pump.getBlockState().get(BotaniaStateProps.CARDINALS)) {
            default:
            case NORTH: break;
            case SOUTH:
                GlStateManager.rotatef(180, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotatef(-90, 0, 1, 0);
                break;
            case WEST:
                GlStateManager.rotatef(90, 0, 1, 0);
                break;
        }
        GlStateManager.translated(-0.5, 0, -0.5);
        GlStateManager.color4f(1, 1, 1, 1);
        Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(headModel, 1, 1, 1, 1);
        GlStateManager.popMatrix();
    }
}
