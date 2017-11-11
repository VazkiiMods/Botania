/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 30, 2015, 10:54:30 PM (GMT)]
 */
package vazkii.botania.common.lexicon;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibLexicon;

import javax.annotation.Nonnull;

public class WelcomeLexiconEntry extends BasicLexiconEntry {

	private static final int PAGES = 7;

	public WelcomeLexiconEntry() {
		super(LibLexicon.BASICS_WELCOME, BotaniaAPI.categoryBasics);
		setPriority();
		setIcon(new ItemStack(ModItems.cosmetic, 1, 31));

		LexiconPage[] pages = new LexiconPage[PAGES];
		for(int i = 0; i < PAGES; i++)
			pages[i] = new PageText("" + i);
		setLexiconPages(pages);
	}

	@Override
	public int compareTo(@Nonnull LexiconEntry o) {
		return -1;
	}

}
