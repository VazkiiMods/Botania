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
import net.minecraft.util.StatCollector;
import vazkii.botania.client.challenge.Challenge;
import vazkii.botania.client.challenge.EnumChallengeLevel;
import vazkii.botania.client.challenge.ModChallenges;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBack;
import vazkii.botania.client.gui.lexicon.button.GuiButtonChallengeIcon;

public class GuiLexiconChallengesList extends GuiLexicon implements IParented {

	GuiLexicon parent;
	GuiButton backButton;
	
	public GuiLexiconChallengesList() {
		parent = new GuiLexicon();
		title = StatCollector.translateToLocal("botaniamisc.challenges");
	}
	
	@Override
	public void onInitGui() {
		super.onInitGui();

		buttonList.add(backButton = new GuiButtonBack(12, left + guiWidth / 2 - 8, top + guiHeight + 2));
		
		int i = 13;
		int y = top + 21;
		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants()) {
			int j = 0;
			for(Challenge c : ModChallenges.challenges.get(level)) {
				buttonList.add(new GuiButtonChallengeIcon(i, left + 20 + (j % 6) * 18, y + (j / 6) * 17, c));
				i++;
				j++;
			}
			y += 44;
		}
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		
		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants())
			fontRendererObj.drawString(StatCollector.translateToLocal(level.getName()), left + 20, top + 12 + level.ordinal() * 44, 0);
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		if(par2 == 14) // Backspace
			back();
		else if(par2 == 199) { // Home
			mc.displayGuiScreen(new GuiLexicon());
			ClientTickHandler.notifyPageChange();
		}

		super.keyTyped(par1, par2);
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);

		if(par3 == 1)
			back();
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id >= BOOKMARK_START)
			handleBookmark(par1GuiButton);
		else
			switch(par1GuiButton.id) {
			case 12 :
				mc.displayGuiScreen(parent);
				ClientTickHandler.notifyPageChange();
				break;
			default :
				//int index = par1GuiButton.id + page * 12;
				//openEntry(index);
			}
	}
	
	void back() {
		if(backButton.enabled) {
			actionPerformed(backButton);
			backButton.func_146113_a(mc.getSoundHandler());
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
	
}
