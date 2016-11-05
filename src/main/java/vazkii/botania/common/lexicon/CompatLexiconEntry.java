/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 24, 2015, 5:43:11 PM (GMT)]
 */
package vazkii.botania.common.lexicon;

import vazkii.botania.api.lexicon.IAddonEntry;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.common.lib.LibMisc;

public class CompatLexiconEntry extends BasicLexiconEntry implements IAddonEntry {

	final String mod;

	public CompatLexiconEntry(String unlocalizedName, LexiconCategory category, String mod) {
		super(unlocalizedName, category);
		this.mod = mod;
	}

	@Override
	public String getSubtitle() {
		return "[" + LibMisc.MOD_NAME + " x " + mod + "]";
	}

}
