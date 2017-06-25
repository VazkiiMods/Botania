/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:48:41 PM (GMT)]
 */
package vazkii.botania.api.internal;

import net.minecraft.client.gui.GuiButton;
import vazkii.botania.api.lexicon.LexiconEntry;

import java.util.List;

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

	/**
	 * Gets the list of buttons in this gui.
	 */
	public List<GuiButton> getButtonList();

	/**
	 * Gets the total amount of ticks (+ partial ticks) the player
	 * has been in this gui.
	 */
	public float getElapsedTicks();

	/**
	 * Gets the current partial ticks.
	 */
	public float getPartialTicks();

	/**
	 * Gets the delta (1F = 1 tick) between this render call
	 * and the last one.
	 */
	public float getTickDelta();
}
