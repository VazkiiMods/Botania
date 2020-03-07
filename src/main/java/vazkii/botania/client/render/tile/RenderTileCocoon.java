/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 8, 2015, 4:44:23 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.lwjgl.opengl.GL11;
import vazkii.botania.common.block.tile.TileCocoon;

import javax.annotation.Nonnull;
import java.util.Random;

public class RenderTileCocoon extends TileEntityRenderer<TileCocoon> {

	public RenderTileCocoon(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileCocoon cocoon, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		float rot = 0F;
		float modval = 60F - (float) cocoon.timePassed / (float) TileCocoon.TOTAL_TIME * 30F;
		if(cocoon.timePassed % modval < 10) {
			float mod = (cocoon.timePassed + partialTicks) % modval;
			float v = mod / 5 * (float) Math.PI * 2;
			rot = (float) Math.sin(v) * (float) Math.log(cocoon.timePassed + partialTicks);
		}

		ms.push();
		ms.translate(0, 0, 1);
		ms.translate(0.5, 0, 0);
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(rot));
		ms.translate(-0.5, 0, 0);
		BlockState state = cocoon.getBlockState();
		IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(state);
		IVertexBuilder buffer = buffers.getBuffer(RenderTypeLookup.getBlockLayer(state));
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, state, model, 1, 1, 1, light, overlay);
		ms.pop();
	}
}
