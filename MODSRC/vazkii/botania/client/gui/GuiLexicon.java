/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:48:05 PM (GMT)]
 */
package vazkii.botania.client.gui;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiLexicon extends GuiScreen {

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_LEXICON);
	
	int guiWidth = 146;
	int guiHeight = 180;
	int left, top;
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		left = width / 2 - guiWidth / 2;
		top = height / 2 - guiHeight / 2;
		
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);
		drawCenteredString(fontRenderer, ModItems.lexicon.getItemDisplayName(null), left + guiWidth / 2, top - 12, 0x00FF00);
	}
	
}
