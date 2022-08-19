/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 16, 2015, 5:03:57 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.block.BlockLightRelay;

public class RenderTileLightRelay extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pticks) {
		Minecraft mc = Minecraft.getMinecraft();
		IIcon iicon = tile.getBlockMetadata() > 0 ? BlockLightRelay.worldIconRed : BlockLightRelay.worldIcon;

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.3, z + 0.5);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.05F);

		double time = ClientTickHandler.ticksInGame + pticks;
		GL11.glColor4f(1F, 1F, 1F, 1F);

		float scale = 0.75F;
		GL11.glScalef(scale, scale, scale);
		Tessellator tessellator = Tessellator.instance;

		GL11.glPushMatrix();
		float r = 180.0F - RenderManager.instance.playerViewY;
		GL11.glRotatef(r, 0F, 1F, 0F);
		GL11.glRotatef(-RenderManager.instance.playerViewX, 1F, 0F, 0F);

		float off = 0.25F;
		GL11.glTranslatef(0F, off, 0F);
		GL11.glRotated(time, 0F, 0F, 1F);
		GL11.glTranslatef(0F, -off, 0F);

		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		ShaderHelper.useShader(ShaderHelper.halo);
		func_77026_a(tessellator, iicon);
		ShaderHelper.releaseShader();
		
		GL11.glPopMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	private void func_77026_a(Tessellator p_77026_1_, IIcon p_77026_2_) {
		float f = p_77026_2_.getMinU();
		float f1 = p_77026_2_.getMaxU();
		float f2 = p_77026_2_.getMinV();
		float f3 = p_77026_2_.getMaxV();
		float size = f1 - f;
		float pad = size / 8F;
		f += pad;
		f1 -= pad;
		f2 += pad;
		f3 -= pad;
		
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		p_77026_1_.startDrawingQuads();
		p_77026_1_.setNormal(0.0F, 1.0F, 0.0F);
		p_77026_1_.setBrightness(240);
		p_77026_1_.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
		p_77026_1_.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
		p_77026_1_.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, f1, f2);
		p_77026_1_.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, f, f2);
		p_77026_1_.draw();

	}

}
