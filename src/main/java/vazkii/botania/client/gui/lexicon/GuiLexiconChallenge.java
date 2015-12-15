/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 29, 2015, 5:25:06 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.challenge.Challenge;
import vazkii.botania.client.challenge.ModChallenges;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBack;
import vazkii.botania.common.lexicon.page.PageText;

public class GuiLexiconChallenge extends GuiLexicon implements IParented {

	private static final String TAG_CHALLENGE = "challenge";

	Challenge challenge;
	GuiLexicon parent;
	GuiButton backButton, completeButton;

	public GuiLexiconChallenge() {
		parent = new GuiLexiconChallengesList();
	}

	public GuiLexiconChallenge(GuiLexicon parent, Challenge challenge) {
		this.parent = parent;
		this.challenge = challenge;
		setTitle();
	}

	public void setTitle() {
		title = challenge == null ? "(null)" : StatCollector.translateToLocal(challenge.unlocalizedName);
	}

	@Override
	public void onInitGui() {
		super.onInitGui();
		setTitle();

		buttonList.add(backButton = new GuiButtonBack(12, left + guiWidth / 2 - 8, top + guiHeight + 2));
		buttonList.add(completeButton = new GuiButton(13, left + 20, top + guiHeight - 35, guiWidth - 40, 20, ""));
		setCompleteButtonTitle();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);

		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderItem.getInstance().renderItemIntoGUI(fontRendererObj, mc.renderEngine, challenge.icon, left + 18, top + 15);
		RenderHelper.disableStandardItemLighting();
		GL11.glEnable(GL11.GL_BLEND);

		boolean unicode = fontRendererObj.getUnicodeFlag();
		fontRendererObj.setUnicodeFlag(true);
		fontRendererObj.drawString(EnumChatFormatting.BOLD + StatCollector.translateToLocal(challenge.unlocalizedName), left + 38, top + 13, 0);
		fontRendererObj.drawString(StatCollector.translateToLocal(challenge.level.getName()) + " / " + (challenge.complete ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED) + StatCollector.translateToLocal(challenge.complete ? "botaniamisc.completed" : "botaniamisc.notCompleted"), left + 38, top + 23, 0);

		int width = guiWidth - 30;
		int x = left + 16;
		int y = top + 28;

		PageText.renderText(x, y, width, guiHeight, challenge.unlocalizedName + ".desc");
		fontRendererObj.setUnicodeFlag(unicode);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if(par2 == 14 && !notesEnabled) // Backspace
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
			super.actionPerformed(par1GuiButton);
		else if(par1GuiButton.id == 12) {
			mc.displayGuiScreen(parent);
			ClientTickHandler.notifyPageChange();
		} else if(par1GuiButton.id == 13) {
			challenge.complete = !challenge.complete;
			setCompleteButtonTitle();
			PersistentVariableHelper.saveSafe();
		} else if(par1GuiButton.id == NOTES_BUTTON_ID)
			notesEnabled = !notesEnabled;
	}

	void setCompleteButtonTitle() {
		completeButton.displayString = StatCollector.translateToLocal(challenge.complete ? "botaniamisc.markNotCompleted" : "botaniamisc.markCompleted");
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
		return new GuiLexiconChallenge(parent, challenge);
	}

	@Override
	public void serialize(NBTTagCompound cmp) {
		super.serialize(cmp);
		cmp.setString(TAG_CHALLENGE, challenge.unlocalizedName);
	}

	@Override
	public void load(NBTTagCompound cmp) {
		super.load(cmp);
		String challengeName = cmp.getString(TAG_CHALLENGE);
		Challenge c = ModChallenges.challengeLookup.get(challengeName);
		challenge = c;
		setTitle();
	}

	@Override
	public String getNotesKey() {
		return "challenge_" + challenge.unlocalizedName;
	}

}
