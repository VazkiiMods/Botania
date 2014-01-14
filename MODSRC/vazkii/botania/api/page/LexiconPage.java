/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:17:24 PM (GMT)]
 */
package vazkii.botania.api.page;

import net.minecraft.client.gui.GuiScreen;

public abstract class LexiconPage {
	
	private final String unlocalizedName;
	
	public LexiconPage(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}
	
	public abstract void renderScreen(GuiScreen gui, int x, int y);
	
	public String getUnlocalizedName() {
		return unlocalizedName;
	}
}
