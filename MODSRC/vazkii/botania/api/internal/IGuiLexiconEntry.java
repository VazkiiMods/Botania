/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:48:41 PM (GMT)]
 */
package vazkii.botania.api.internal;

import vazkii.botania.api.lexicon.LexiconEntry;

/**
 * Internal interface for the Lexicon Entry GUI. This contains
 * everything that can be accessed from it. It's safe to cast
 * this type to GuiScreen.
 */
public interface IGuiLexiconEntry {

	/**
	 * Gets the entry currently portrayed in this gui.
	 */
	public LexiconEntry getEntry();

	/**
	 * Gets the current page the lexicon GUI is browsing.
	 */
	public int getPageOn();

	/**
	 * Gets the leftmost part of the GUI.
	 */
	public int getLeft();

	/**
	 * Gets the topmost part of the GUI.
	 */
	public int getTop();

	/**
	 * Gets the GUI's width.
	 */
	public int getWidth();

	/**
	 * Gets the GUI's height
	 */
	public int getHeight();

	/**
	 * Gets the GUI's Z level for rendering.
	 */
	public float getZLevel();
}
