/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
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
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RenderLens implements IItemRenderer {

	RenderItem render = new RenderItem();
	ItemRenderer renderer  = new ItemRenderer(Minecraft.getMinecraft());

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch(type) {
		case INVENTORY : {
			Icon icon = item.getItem().getIconFromDamageForRenderPass(0, 1);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			render.renderIcon(0, 0, icon, 16, 16);

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Color color = new Color(item.getItem().getColorFromItemStack(item, 0));
			GL11.glColor4ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) 255);
			icon = item.getItem().getIconFromDamageForRenderPass(0, 0);
			render.renderIcon(0, 0, icon, 16, 16);
			GL11.glDisable(GL11.GL_BLEND);
			break;
		}
		case ENTITY : {
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.5F, 0F, 0F);
			render(item, false);
			GL11.glPopMatrix();
			break;
		}
		case EQUIPPED : {
			render(item, false);
			break;
		}
		case EQUIPPED_FIRST_PERSON : {
			render(item, false);
			break;
		}
		default : break;
		}
	}
	
	public void render(ItemStack item, boolean inventory) {
		Icon icon = item.getItem().getIconFromDamageForRenderPass(0, 1);
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / (inventory ? 1F : 16F));
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		icon = item.getItem().getIconFromDamageForRenderPass(0, 0);
		float scale = 1F / 16F;
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(0F, 0F, -0.5F);
		Color color = new Color(item.getItem().getColorFromItemStack(item, 0));
		GL11.glColor4ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) 255);
		render.renderIcon(0, 0, icon, 16, 16);
		GL11.glRotatef(180F, 0F, 1F, 0F);
		GL11.glTranslatef(-16F, 0F, 0F);
		render.renderIcon(0, 0, icon, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
	}

}