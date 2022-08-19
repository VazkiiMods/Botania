/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [16/12/2015, 20:38:33 (GMT)]
 */
package vazkii.botania.common.lexicon;

import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.client.core.handler.PersistentVariableHelper;

public class DLexiconEntry extends BLexiconEntry {

	public DLexiconEntry(String unlocalizedName, LexiconCategory category) {
		super(unlocalizedName, category);
	}
	
	@Override
	public boolean isVisible() {
		return !PersistentVariableHelper.dog;
	}

}
