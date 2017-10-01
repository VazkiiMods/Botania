/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:17:24 PM (GMT)]
 */
package vazkii.botania.api.lexicon;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.IGuiLexiconEntry;

import java.util.List;

public abstract class LexiconPage {

	public String unlocalizedName;
	public boolean skipRegistry;

	public LexiconPage(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	/**
	 * Does the rendering for this page.
	 * @param gui The active GuiScreen
	 * @param mx The mouse's relative X position.
	 * @param my The mouse's relative Y position.
	 */
	@SideOnly(Side.CLIENT)
	public abstract void renderScreen(IGuiLexiconEntry gui, int mx, int my);

	/**
	 * Called per update tick. Non gui-sensitive version, kept for backwards compatibility only.
	 */
	@SideOnly(Side.CLIENT)
	public void updateScreen() {}

	/**
	 * Called per update tick. Feel free to override fully, the
	 * call to updateScreen() is for backwards compatibility.
	 */
	@SideOnly(Side.CLIENT)
	public void updateScreen(IGuiLexiconEntry gui) {
		updateScreen();
	}

	/**
	 * Called when this page is opened, be it via initGui() or when the player changes page.
	 * You can add buttons and whatever you'd do on initGui() here.
	 */
	@SideOnly(Side.CLIENT)
	public void onOpened(IGuiLexiconEntry gui) {}

	/**
	 * Called when this page is opened, be it via closing the gui or when the player changes page.
	 * Make sure to dispose of anything you don't use any more, such as buttons in the gui's buttonList.
	 */
	@SideOnly(Side.CLIENT)
	public void onClosed(IGuiLexiconEntry gui) {}

	/**
	 * Called when a button is pressed, equivalent to GuiScreen.actionPerformed.
	 */
	@SideOnly(Side.CLIENT)
	public void onActionPerformed(IGuiLexiconEntry gui, GuiButton button) {}

	/**
	 * Called when a key is pressed.
	 */
	@SideOnly(Side.CLIENT)
	public void onKeyPressed(char c, int key) {}

	/**
	 * Called when {@link LexiconEntry#setLexiconPages(LexiconPage...)} is called.
	 */
	public void onPageAdded(LexiconEntry entry, int index) {}

	/**
	 * Shows the list of recipes present in this page for display in the category
	 * page.
	 */
	public List<ItemStack> getDisplayedRecipes() {
		return ImmutableList.of();
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public LexiconPage setSkipRegistry() {
		skipRegistry = true;
		return this;
	}
}
