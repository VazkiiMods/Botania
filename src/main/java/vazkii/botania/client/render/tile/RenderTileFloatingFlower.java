/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 8, 2014, 10:58:46 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.*;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.ModelMiniIsland;
import vazkii.botania.common.block.decor.IFloatingFlower;

public class RenderTileFloatingFlower extends TileEntitySpecialRenderer {

	private static final ModelMiniIsland model = new ModelMiniIsland();

	@Override
	public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float t) {
		IFloatingFlower flower = (IFloatingFlower) tile;
		GL11.glPushMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		double worldTime = tile.getWorldObj() == null ? 0 : (double) (ClientTickHandler.ticksInGame + t);
		if(tile.getWorldObj() != null)
			worldTime += new Random(tile.xCoord ^ tile.yCoord ^ tile.zCoord).nextInt(1000);

		GL11.glTranslatef(0.5F, 0F, 0.5F);
		GL11.glRotatef(-((float) worldTime * 0.5F), 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, 0F, -0.5F);

		if(tile.getWorldObj() != null) {
			GL11.glTranslatef(0F, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0F);
			GL11.glRotatef(4F * (float) Math.sin(worldTime * 0.04F), 1F, 0F, 0F);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(flower.getIslandType().getResource());
		Color color = new Color(flower.getIslandType().getColor());
		GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 1.4F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		model.render();
		GL11.glPopMatrix();
		GL11.glColor3f(1f, 1f, 1f);

		ItemStack stack = flower.getDisplayStack();
		IIcon icon = stack.getIconIndex();

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		GL11.glTranslatef(0.25F, 0.4F, 0.5F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glPopMatrix();
	}

}
