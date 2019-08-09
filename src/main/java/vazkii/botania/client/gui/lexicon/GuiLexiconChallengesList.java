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

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
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
	private Button backButton;

	public GuiLexiconChallengesList() {
		super(new TranslationTextComponent("botaniamisc.challenges"));
		parent = new GuiLexicon(getTitle());
	}

	@Override
	public void onInitGui() {
		super.onInitGui();

		addButton(backButton = new GuiButtonBack(left + guiWidth / 2 - 8, top + guiHeight + 2, b -> {
			mc.displayGuiScreen(parent);
			ClientTickHandler.notifyPageChange();
		}));

		int perline = 6;
		int i = 13;
		int y = top + 20;
		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants()) {
			int j = 0;
			for(Challenge c : ModChallenges.challenges.get(level)) {
				addButton(new GuiButtonChallengeIcon(left + 20 + j % perline * 18, y + j / perline * 17, c, this));
				i++;
				j++;
			}
			y += 44;
		}
	}

	@Override
	public void drawScreenAfterScale(int xCoord, int yCoord, float newPartialTicks) {
		super.drawScreenAfterScale(xCoord, yCoord, newPartialTicks);

		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants()) {
			List<Challenge> list = ModChallenges.challenges.get(level);
			int complete = 0;
			for(Challenge c : list)
				if(c.complete)
					complete++;

			font.drawString(TextFormatting.BOLD + I18n.format(level.getName()) + TextFormatting.RESET + " (" + complete + "/" + list.size() + ")", left + 20, top + 11 + level.ordinal() * 44, 0);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int mods) {
		if(keyCode == GLFW.GLFW_KEY_BACKSPACE && !notesEnabled) {
			back();
			return true;
		} else if(keyCode == GLFW.GLFW_KEY_HOME) {
			mc.displayGuiScreen(new GuiLexicon(getTitle()));
			ClientTickHandler.notifyPageChange();
			return true;
		}

		return super.keyPressed(keyCode, scanCode, mods);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mods) {
		if(mods == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			back();
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mods);
	}

	private void back() {
		if(backButton.active) {
			backButton.playDownSound(mc.getSoundHandler());
			backButton.onClick(backButton.x, backButton.y);
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
