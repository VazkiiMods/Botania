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

import vazkii.botania.api.internal.IGuiLexiconEntry;

public abstract class LexiconPage {
	
	private final String unlocalizedName;
	
	public LexiconPage(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}
	
	/**
	 * Does the rendering for this page.
	 * @param gui The active GuiScreen
	 * @param x The leftmost part of the gui.
	 * @param y The top part of the gui.
	 */
	public abstract void renderScreen(IGuiLexiconEntry gui, int x, int y);
	
	public String getUnlocalizedName() {
		return unlocalizedName;
	}
}
