/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 9, 2014, 9:55:07 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.block.BlockAlfPortal;
import vazkii.botania.common.block.tile.TileAlfPortal;

public class RenderTileAlfPortal extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TileAlfPortal portal = (TileAlfPortal) tileentity;
		int meta = portal.getBlockMetadata();
		if(meta == 0)
			return;

		GL11.glPushMatrix();
		GL11.glTranslated(d0, d1, d2);
		GL11.glTranslatef(-1F, 1F, 0.25F);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		float alpha = (float) Math.min(1F, (Math.sin(portal.getWorldObj().getTotalWorldTime() / 8D) + 1D) / 7D + 0.6D) * (Math.min(60, portal.ticksOpen) / 60F);
		GL11.glColor4f(1F, 1F, 1F, alpha);

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

		if(meta == 2) {
			GL11.glTranslatef(1.25F, 0F, 1.75F);
			GL11.glRotatef(90F, 0F, 1F, 0F);
		}

		renderIcon(0, 0, BlockAlfPortal.portalTex, 3, 3, 240);
		if(meta == 2) {
			GL11.glTranslated(0F, 0F, 0.5F);
			renderIcon(0, 0, BlockAlfPortal.portalTex, 3, 3, 240);
			GL11.glTranslated(0F, 0F, -0.5F);
		}

		GL11.glRotatef(180F, 0F, 1F, 0F);
		GL11.glTranslated(-3F, 0F, -0.5F);
		renderIcon(0, 0, BlockAlfPortal.portalTex, 3, 3, 240);
		if(meta == 2) {
			GL11.glTranslated(0F, 0F, 0.5F);
			renderIcon(0, 0, BlockAlfPortal.portalTex, 3, 3, 240);
			GL11.glTranslated(0F, 0F, -0.5F);
		}

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
	}

	public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5, int brightness) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(brightness);
		tessellator.addVertexWithUV(par1 + 0, par2 + par5, 0, par3Icon.getMinU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + par5, 0, par3Icon.getMaxU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + 0, 0, par3Icon.getMaxU(), par3Icon.getMinV());
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0, par3Icon.getMinU(), par3Icon.getMinV());
		tessellator.draw();
	}

}
