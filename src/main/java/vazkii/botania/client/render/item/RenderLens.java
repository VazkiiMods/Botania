/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 31, 2014, 3:08:00 PM (GMT)]
 */
package vazkii.botania.client.render.item;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.common.item.lens.ItemLens;

public class RenderLens implements IItemRenderer {

	static RenderItem render = new RenderItem();
	ItemRenderer renderer  = new ItemRenderer(Minecraft.getMinecraft());

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type != ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch(type) {
		case ENTITY : {
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.5F, 0F, 0F);
			if(item.isOnItemFrame())
				GL11.glTranslatef(0F, -0.3F, 0.01F);
			render(item);
			GL11.glPopMatrix();
			break;
		}
		case EQUIPPED : {
			render(item);
			break;
		}
		case EQUIPPED_FIRST_PERSON : {
			render(item);
			break;
		}
		default : break;
		}
	}

	public static void render(ItemStack item) {
		Color color = new Color(((ILens) item.getItem()).getLensColor(item));
		render(item, color.getRGB());
	}

	public static void render(ItemStack item, int color_) {
		int dmg = item.getItemDamage();
		IIcon icon = item.getItem().getIconFromDamageForRenderPass(dmg, 1);
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		float scale = 1F / 16F;

		GL11.glColor4f(1F, 1F, 1F, 1F);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Color color = new Color(color_);
		GL11.glColor4ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) 255);

		boolean shiny = ItemLens.getStoredColor(item) != -1;
		icon = ItemLens.iconGlass;
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(0F, 0F, -0.5F);
		renderShinyLensIcon(icon, shiny);
		GL11.glRotatef(180F, 0F, 1F, 0F);
		GL11.glTranslatef(-16F, 0F, 0F);
		renderShinyLensIcon(icon, shiny);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();

		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	public static void renderShinyLensIcon(IIcon icon, boolean shiny) {
		float par1 = 0;
		float par2 = 0;
		float par4 = 16;
		float par5 = 16;
		float zLevel = 0F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		if(shiny)
			tessellator.setBrightness(240);
		tessellator.addVertexWithUV(par1 + 0, par2 + par5, zLevel, icon.getMinU(), icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + par5, zLevel, icon.getMaxU(), icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + 0, zLevel, icon.getMaxU(), icon.getMinV());
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, zLevel, icon.getMinU(), icon.getMinV());
		tessellator.draw();
	}

}