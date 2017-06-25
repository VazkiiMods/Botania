/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 6, 2014, 6:35:32 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;

public class GuiButtonBackWithShift extends GuiButtonBack {

	public GuiButtonBackWithShift(int par1, int par2, int par3) {
		super(par1, par2, par3);
	}

	@Override
	public List<String> getTooltip() {
		return Arrays.asList(I18n.format("botaniamisc.back"), TextFormatting.GRAY + I18n.format("botaniamisc.clickToIndex"));
	}

}
