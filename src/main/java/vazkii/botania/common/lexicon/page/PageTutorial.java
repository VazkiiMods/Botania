/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 16, 2015, 1:39:10 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

public class PageTutorial extends PageText {

	GuiButton button;

	public PageTutorial(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public void onOpened(IGuiLexiconEntry gui) {
		button = new GuiButton(101, gui.getLeft() + 30, gui.getTop() + gui.getHeight() - 50, gui.getWidth() - 60, 20, StatCollector.translateToLocal("botaniamisc.startTutorial"));
		gui.getButtonList().add(button);
	}

	@Override
	public void onClosed(IGuiLexiconEntry gui) {
		gui.getButtonList().remove(button);
	}

	@Override
	public void onActionPerformed(IGuiLexiconEntry gui, GuiButton button) {
		if(button == this.button) {
			GuiLexicon.startTutorial();
			Minecraft.getMinecraft().displayGuiScreen(new GuiLexicon());
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("botaniamisc.tutorialStarted").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
		}
	}

}
