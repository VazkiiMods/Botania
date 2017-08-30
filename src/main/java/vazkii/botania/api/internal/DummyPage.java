/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:41:23 PM (GMT)]
 */
package vazkii.botania.api.internal;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.LexiconPage;

/**
 * A dummy page. It does absolutely nothing and is only
 * existant to make sure everything goes right even if
 * Botania isn't loaded.
 */
public class DummyPage extends LexiconPage {

	public DummyPage(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int x, int y) {}

}
