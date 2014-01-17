/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:17:06 PM (GMT)]
 */
package vazkii.botania.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.botania.api.page.LexiconPage;

public class LexiconEntry {

	public final String unlocalizedName;
	public List<LexiconPage> pages = new ArrayList<LexiconPage>();

	/**
	 * @param unlocalizedName The unlocalized name of this entry. This will be localized by the client display.
	 */
	public LexiconEntry(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	/**
	 * Sets what pages you want this entry to have.
	 */
	public void setLexiconPages(LexiconPage... pages) {
		this.pages.addAll(Arrays.asList(pages));
	}

	/**
	 * Adds a page to the list of pages.
	 */
	public void addPage(LexiconPage page) {
		pages.add(page);
	}

	/**
	 * Should this entry be displayed in the list of entries?
	 */
	public boolean shouldBeDisplayed(GuiScreen screen, EntityPlayer player) {
		return true;
	}

}
