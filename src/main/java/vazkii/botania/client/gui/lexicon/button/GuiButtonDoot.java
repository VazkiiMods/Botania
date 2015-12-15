/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 11, 2015, 7:04:46 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.item.ModItems;

// Ssssshhhhhhh
// I should've done this last year
public class GuiButtonDoot extends GuiButtonLexicon {

	public GuiButtonDoot(int id, int x, int y) {
		super(id, x, y, 16, 16, "");
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		field_146123_n = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
		int k = getHoverState(field_146123_n);

		par1Minecraft.renderEngine.bindTexture(TextureMap.locationItemsTexture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		RenderItem.getInstance().renderItemIntoGUI(par1Minecraft.fontRenderer, par1Minecraft.renderEngine, new ItemStack(ModItems.cacophonium), xPosition, yPosition);
		RenderItem.getInstance().renderItemIntoGUI(par1Minecraft.fontRenderer, par1Minecraft.renderEngine, new ItemStack(Items.fireworks), xPosition + 8, yPosition + 2);

		GL11.glDisable(GL11.GL_LIGHTING);


		List<String> tooltip = new ArrayList();
		tooltip.add(EnumChatFormatting.LIGHT_PURPLE + "Happy Birthday Vazkii!");
		tooltip.add(EnumChatFormatting.GRAY + "doot doot");

		if(k == 2)
			RenderHelper.renderTooltip(xPosition - 100, yPosition + 36, tooltip);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

}

