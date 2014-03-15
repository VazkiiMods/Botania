/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 15, 2014, 5:04:42 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.block.BlockEnchanter;

public class RenderTileEnchanter extends TileEntitySpecialRenderer {

	RenderItem renderItem = new RenderItem();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		float alphaMod = 1F;
		
		GL11.glPushMatrix();
		GL11.glTranslated(d0, d1, d2);
		GL11.glRotated(90F, 1F, 0F, 0F);
		GL11.glTranslatef(-2F, -2F, -0.001F);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0.6F + (float) ((Math.cos((double) tileentity.worldObj.getTotalWorldTime() / 6D) + 1D) / 5D), 0.1F, 0.9F, (float) ((Math.sin((double) tileentity.worldObj.getTotalWorldTime() / 8D) + 1D) / 5D + 0.4D) * alphaMod);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		renderIcon(0, 0, BlockEnchanter.overlay, 5, 5, 240);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
	}
	
    public void renderIcon(int par1, int par2, Icon par3Icon, int par4, int par5, int brightness) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(brightness);
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par5), (double)0, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + par5), (double)0, (double)par3Icon.getMaxU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + 0), (double)0, (double)par3Icon.getMaxU(), (double)par3Icon.getMinV());
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)0, (double)par3Icon.getMinU(), (double)par3Icon.getMinV());
        tessellator.draw();
    }

}
