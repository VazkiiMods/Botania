/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 2, 2015, 6:27:58 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.LexiconEntry;

import java.util.ArrayList;
import java.util.List;

public class GuiLexiconHistory extends GuiLexiconIndex {

	public static final List<LexiconEntry> history = new ArrayList<>();

	public GuiLexiconHistory() {
		super(null);
		title = I18n.format("botaniamisc.historyLong");
	}

	@Override
	void buildEntries() {
		entriesToDisplay.clear();
		ILexicon lex = (ILexicon) stackUsed.getItem();
		for(int i = history.size() - 1; i >= 0; i--) {
			LexiconEntry entry = history.get(i);
			if(lex.isKnowledgeUnlocked(stackUsed, entry.getKnowledgeType()) && I18n.format(entry.getUnlocalizedName()).toLowerCase().contains(searchField.getText().toLowerCase().trim()))
				entriesToDisplay.add(entry);
		}
	}

	public static void visit(LexiconEntry entry) {
		history.remove(entry);
		history.add(entry);
	}

	@Override
	public GuiLexicon copy() {
		return new GuiLexiconHistory();
	}

	@Override
	public void load(NBTTagCompound cmp) {}

	@Override
	public String getNotesKey() {
		return "history";
	}

}
