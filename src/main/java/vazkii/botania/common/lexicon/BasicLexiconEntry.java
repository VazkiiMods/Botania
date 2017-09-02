/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 9:47:21 PM (GMT)]
 */
package vazkii.botania.common.lexicon;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ITwoNamedPage;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;

import javax.annotation.Nonnull;

public class BasicLexiconEntry extends LexiconEntry {

	public BasicLexiconEntry(String unlocalizedName, LexiconCategory category) {
		super(unlocalizedName, category);
		BotaniaAPI.addEntry(this, category);
	}

	@Override
	public LexiconEntry setLexiconPages(LexiconPage... pages) {
		for(LexiconPage page : pages) {
			page.unlocalizedName = "botania.page." + getLazyUnlocalizedName() + page.unlocalizedName;
			if(page instanceof ITwoNamedPage) {
				ITwoNamedPage dou = (ITwoNamedPage) page;
				dou.setSecondUnlocalizedName("botania.page." + getLazyUnlocalizedName() + dou.getSecondUnlocalizedName());
			}
		}

		return super.setLexiconPages(pages);
	}

	@Override
	public String getUnlocalizedName() {
		return "botania.entry." + super.getUnlocalizedName();
	}

	@Override
	public String getTagline() {
		return "botania.tagline." + super.getUnlocalizedName();
	}

	public String getLazyUnlocalizedName() {
		return super.getUnlocalizedName();
	}

	@Override
	public String getWebLink() {
		return "http://botaniamod.net/lexicon.php#" + unlocalizedName;
	}

	@Override
	public int compareTo(@Nonnull LexiconEntry o) {
		return o instanceof WelcomeLexiconEntry ? 1 : super.compareTo(o);
	}

}
