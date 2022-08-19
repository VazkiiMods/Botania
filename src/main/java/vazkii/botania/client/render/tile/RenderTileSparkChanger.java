/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 28, 2015, 10:19:20 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.block.tile.TileSparkChanger;

public class RenderTileSparkChanger extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float pticks) {
		GL11.glPushMatrix();
		GL11.glTranslated(d0, d1, d2);
		GL11.glRotated(90F, 1F, 0F, 0F);
		GL11.glTranslatef(0.8F, 0.2F, -0.22F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		ItemStack stack = ((TileSparkChanger) tileentity).getStackInSlot(0);
		if(stack != null) {
			GL11.glPushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			float s = 0.6F;
			GL11.glScalef(s, s, s);
			GL11.glRotatef(180F, 0F, 1F, 0F);

			int renderPass = 0;
			do {
				IIcon icon = stack.getItem().getIcon(stack, renderPass);
				if(icon != null) {
					Color color = new Color(stack.getItem().getColorFromItemStack(stack, renderPass));
					GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
					float f = icon.getMinU();
					float f1 = icon.getMaxU();
					float f2 = icon.getMinV();
					float f3 = icon.getMaxV();
					ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
					GL11.glColor3f(1F, 1F, 1F);
				}
				renderPass++;
			} while(renderPass < stack.getItem().getRenderPasses(stack.getItemDamage()));
			GL11.glPopMatrix();
		}
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
	}

}
