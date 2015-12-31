/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 6, 2014, 10:04:57 PM (GMT)]
 *//*

package vazkii.botania.client.render.item;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RenderTransparentItem implements IItemRenderer {

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
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.5F, 0F, 0F);
			if(item.isOnItemFrame())
				GlStateManager.translate(0F, -0.3F, 0.01F);
			render(item);
			GlStateManager.popMatrix();
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

	public void render(ItemStack item) {
		int dmg = item.getItemDamage();
		IIcon icon = item.getItem().getIconFromDamageForRenderPass(dmg, 0);
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		float scale = 1F / 16F;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);

		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

		GlStateManager.color(1F, 1F, 1F, 1F);
	}


}
*/
