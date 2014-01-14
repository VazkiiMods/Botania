/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:41:23 PM (GMT)]
 */
package vazkii.botania.api.page;

import net.minecraft.client.gui.GuiScreen;

/**
 * A dummy page. It does absolutely nothing and is only
 * existant to make sure everything goes right even if
 * Botania isn't loaded.
 */
public class PageDummy extends LexiconPage {

	public PageDummy(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public void renderScreen(GuiScreen gui, int x, int y) {
		// NO-OP
	}

}
