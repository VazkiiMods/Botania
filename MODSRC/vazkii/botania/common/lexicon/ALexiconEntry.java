package vazkii.botania.common.lexicon;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;

public class ALexiconEntry extends BLexiconEntry {

	public ALexiconEntry(String unlocalizedName, LexiconCategory category) {
		super(unlocalizedName, category);
		setKnowledgeType(BotaniaAPI.elvenKnowledge);
	}

}
