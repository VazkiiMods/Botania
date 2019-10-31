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

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import org.lwjgl.opengl.GL11;
import vazkii.botania.common.block.tile.TileCocoon;

import javax.annotation.Nonnull;

public class RenderTileCocoon extends TileEntityRenderer<TileCocoon> {

	@Override
	public void render(@Nonnull TileCocoon cocoon, double d0, double d1, double d2, float f, int digProgress) {
		float rot = 0F;
		float modval = 60F - (float) cocoon.timePassed / (float) TileCocoon.TOTAL_TIME * 30F;
		if(cocoon.timePassed % modval < 10) {
			float mod = (cocoon.timePassed + f) % modval;
			float v = mod / 5 * (float) Math.PI * 2;
			rot = (float) Math.sin(v) * (float) Math.log(cocoon.timePassed + f);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.translated(d0, d1, d2 + 1);
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.translatef(0.5F, 0, 0F);
		GlStateManager.rotatef(rot, 1F, 0F, 0F);
		GlStateManager.translatef(-0.5F, 0, 0F);
		BlockState state = cocoon.getWorld().getBlockState(cocoon.getPos());
		IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(state);
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0F, false);
		GlStateManager.color3f(1F, 1F, 1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}
}
