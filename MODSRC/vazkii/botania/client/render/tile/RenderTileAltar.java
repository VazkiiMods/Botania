/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 21, 2014, 7:55:47 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelAltar;
import vazkii.botania.common.block.tile.TileAltar;

public class RenderTileAltar extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_ALTAR);
	ModelAltar model = new ModelAltar();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float ticks) {
		GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glTranslated(d0 + 0.5, d1 + 1.5, d2 + 0.5);
        GL11.glScalef(1F, -1F, -1F);
        model.render();
        GL11.glScalef(1F, -1F, -1F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        
        if(((TileAltar) tileentity).hasWater) {
        	Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        	GL11.glPushMatrix();
        	float f = 1F / 256F * 10F;
        	float p = -(1F / 16F * 6F);
        	GL11.glEnable(GL11.GL_BLEND);
        	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        	GL11.glColor4f(1F, 1F, 1F, 1F);
        	GL11.glTranslatef(p, -0.3F, p);
        	GL11.glRotatef(90F, 1F, 0F, 0F);
        	GL11.glScalef(f, f, f);
        	new RenderItem().renderIcon(0, 0, Block.waterStill.getIcon(0, 0), 16, 16);
        	GL11.glDisable(GL11.GL_BLEND);
        	GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
	}

}
