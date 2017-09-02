/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 29, 2015, 4:24:07 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.challenge.Challenge;
import vazkii.botania.client.challenge.EnumChallengeLevel;
import vazkii.botania.client.challenge.ModChallenges;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBack;
import vazkii.botania.client.gui.lexicon.button.GuiButtonChallengeIcon;

import java.io.IOException;
import java.util.List;

public class GuiLexiconChallengesList extends GuiLexicon implements IParented {

	private GuiLexicon parent;
	private GuiButton backButton;

	public GuiLexiconChallengesList() {
		parent = new GuiLexicon();
		title = I18n.format("botaniamisc.challenges");
	}

	@Override
	public void onInitGui() {
		super.onInitGui();
		title = I18n.format("botaniamisc.challenges");

		buttonList.add(backButton = new GuiButtonBack(12, left + guiWidth / 2 - 8, top + guiHeight + 2));

		int perline = 6;
		int i = 13;
		int y = top + 20;
		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants()) {
			int j = 0;
			for(Challenge c : ModChallenges.challenges.get(level)) {
				buttonList.add(new GuiButtonChallengeIcon(i, left + 20 + j % perline * 18, y + j / perline * 17, c));
				i++;
				j++;
			}
			y += 44;
		}
	}

	@Override
	public void drawScreenAfterScale(int xCoord, int yCoord, float newPartialTicks) {
		super.drawScreenAfterScale(xCoord, yCoord, newPartialTicks);

		boolean unicode = fontRenderer.getUnicodeFlag();
		fontRenderer.setUnicodeFlag(true);
		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants()) {
			List<Challenge> list = ModChallenges.challenges.get(level);
			int complete = 0;
			for(Challenge c : list)
				if(c.complete)
					complete++;

			fontRenderer.drawString(TextFormatting.BOLD + I18n.format(level.getName()) + TextFormatting.RESET + " (" + complete + "/" + list.size() + ")", left + 20, top + 11 + level.ordinal() * 44, 0);
		}
		fontRenderer.setUnicodeFlag(unicode);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		if(par2 == 14 && !notesEnabled) // Backspace
			back();
		else if(par2 == 199) { // Home
			mc.displayGuiScreen(new GuiLexicon());
			ClientTickHandler.notifyPageChange();
		}

		super.keyTyped(par1, par2);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException {
		super.mouseClicked(par1, par2, par3);

		if(par3 == 1)
			back();
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id >= BOOKMARK_START)
			super.actionPerformed(par1GuiButton);
		else if(par1GuiButton.id == 12) {
			mc.displayGuiScreen(parent);
			ClientTickHandler.notifyPageChange();
		} else if(par1GuiButton instanceof GuiButtonChallengeIcon) {
			GuiButtonChallengeIcon cbutton = (GuiButtonChallengeIcon) par1GuiButton;
			mc.displayGuiScreen(new GuiLexiconChallenge(this, cbutton.challenge));
		} else if(par1GuiButton.id == NOTES_BUTTON_ID)
			notesEnabled = !notesEnabled;
	}

	private void back() {
		if(backButton.enabled) {
			actionPerformed(backButton);
			backButton.playPressSound(mc.getSoundHandler());
		}
	}

	@Override
	public void setParent(GuiLexicon gui) {
		parent = gui;
	}

	@Override
	boolean isMainPage() {
		return false;
	}

	@Override
	String getTitle() {
		return title;
	}

	@Override
	boolean isChallenge() {
		return true;
	}

	@Override
	boolean isCategoryIndex() {
		return false;
	}

	@Override
	public GuiLexicon copy() {
		return new GuiLexiconChallengesList();
	}

	@Override
	public String getNotesKey() {
		return "challengelist";
	}

}
