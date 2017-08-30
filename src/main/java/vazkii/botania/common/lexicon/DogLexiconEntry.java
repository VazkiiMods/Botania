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

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.client.core.handler.PersistentVariableHelper;

public class DogLexiconEntry extends BasicLexiconEntry {

	public DogLexiconEntry(String unlocalizedName, LexiconCategory category) {
		super(unlocalizedName, category);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isVisible() {
		return !PersistentVariableHelper.dog;
	}

}
