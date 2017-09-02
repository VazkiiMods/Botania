/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 7, 2014, 5:53:16 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import vazkii.botania.common.core.handler.ModSounds;

public class GuiButtonLexicon extends GuiButton {

	public GuiButtonLexicon(int id, int x, int y, int width, int height, String text) {
		super(id, x, y, width, height, text);
	}

	@Override
	public void playPressSound(SoundHandler handler)  {
		handler.playSound(PositionedSoundRecord.getMasterRecord(ModSounds.lexiconPage, 1.0F));
	}

}
