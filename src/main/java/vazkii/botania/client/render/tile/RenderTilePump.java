package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.mana.TilePump;

public class RenderTilePump extends TileEntityRenderer<TilePump> {
    public static IBakedModel headModel = null;

    public RenderTilePump(TileEntityRendererDispatcher manager) {
        super(manager);
    }

    @Override
    public void render(TilePump pump, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
        ms.push();
        ms.translate(0.5, 0, 0.5);
        float angle = 0;
        switch (pump.getBlockState().get(BotaniaStateProps.CARDINALS)) {
            default:
            case NORTH: break;
            case SOUTH:
                angle = 180;
                break;
            case EAST:
                angle = -90;
                break;
            case WEST:
                angle = 90;
                break;
        }
        ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(angle));
        ms.translate(-0.5, 0, -0.5);
        double diff = Math.max(0F, Math.min(8F, pump.innerRingPos + pump.moving * partialTicks));
        ms.translate(0, 0, diff / 14);
        IVertexBuilder buffer = buffers.getBuffer(RenderType.getSolid());
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, null, headModel, 1, 1, 1, light, overlay);
        ms.pop();
    }
}
