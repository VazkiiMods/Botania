/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Oct 31, 2014, 4:53:15 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelBrewery;
import vazkii.botania.client.render.item.RenderLens;
import vazkii.botania.common.block.tile.TileBrewery;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileBrewery extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_BREWERY);
	ModelBrewery model = new ModelBrewery();
	public static boolean rotate = true;
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TileBrewery brewery = (TileBrewery) tileentity;
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glTranslatef(0.5F, -1.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + f;
		if(!rotate)
			time = -1;
		
		model.render(time);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

}
