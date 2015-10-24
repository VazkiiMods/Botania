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

import java.awt.Desktop;
import java.net.URI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageTutorial extends PageText {

	GuiButton buttonText, buttonVideo;

	public PageTutorial(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public void onOpened(IGuiLexiconEntry gui) {
		buttonText = new GuiButton(101, gui.getLeft() + 20, gui.getTop() + gui.getHeight() - 40, 50, 20, StatCollector.translateToLocal("botaniamisc.tutorialText"));
		buttonVideo = new GuiButton(101, gui.getLeft() + 75, gui.getTop() + gui.getHeight() - 40, 50, 20, StatCollector.translateToLocal("botaniamisc.tutorialVideo"));

		gui.getButtonList().add(buttonText);
		gui.getButtonList().add(buttonVideo);
	}

	@Override
	public void onClosed(IGuiLexiconEntry gui) {
		gui.getButtonList().remove(buttonText);
		gui.getButtonList().remove(buttonVideo);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onActionPerformed(IGuiLexiconEntry gui, GuiButton button) {
		if(button == buttonText) {
			GuiLexicon.startTutorial();
			Minecraft.getMinecraft().displayGuiScreen(new GuiLexicon());
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("botaniamisc.tutorialStarted").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
		} else if(button == buttonVideo && Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=rx0xyejC6fI"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
