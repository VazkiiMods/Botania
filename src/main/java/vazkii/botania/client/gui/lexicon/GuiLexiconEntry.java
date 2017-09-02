/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:47:06 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.IAddonEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBackWithShift;
import vazkii.botania.client.gui.lexicon.button.GuiButtonPage;
import vazkii.botania.client.gui.lexicon.button.GuiButtonShare;
import vazkii.botania.client.gui.lexicon.button.GuiButtonViewOnline;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class GuiLexiconEntry extends GuiLexicon implements IGuiLexiconEntry, IParented {

	private static final String TAG_ENTRY = "entry";
	private static final String TAG_PAGE = "page";

	public int page = 0;
	private boolean firstEntry = false;
	LexiconEntry entry;
	private GuiScreen parent;
	private String title;
	private String subtitle;

	private GuiButton leftButton, rightButton, backButton;

	public GuiLexiconEntry() {
		parent = new GuiLexicon();
		setTitle();
	}

	public GuiLexiconEntry(LexiconEntry entry, GuiScreen parent) {
		this.entry = entry;
		this.parent = parent;
		setTitle();
	}

	private void setTitle() {
		if(entry == null) {
			title = "(null)";
			return;
		}

		title = I18n.format(entry.getUnlocalizedName());
		if(entry instanceof IAddonEntry)
			subtitle = I18n.format(((IAddonEntry) entry).getSubtitle());
		else subtitle = null;
	}

	@Override
	public void onInitGui() {
		super.onInitGui();

		buttonList.add(backButton = new GuiButtonBackWithShift(0, left + guiWidth / 2 - 8, top + guiHeight + 2));
		buttonList.add(leftButton = new GuiButtonPage(1, left, top + guiHeight - 10, false));
		buttonList.add(rightButton = new GuiButtonPage(2, left + guiWidth - 18, top + guiHeight - 10, true));
		buttonList.add(new GuiButtonShare(3, left + guiWidth - 6, top - 2));
		if(entry.getWebLink() != null)
			buttonList.add(new GuiButtonViewOnline(4, left - 8, top + 12));

		if(!GuiLexicon.isValidLexiconGui(this))	{
			currentOpenLexicon = new GuiLexicon();
			mc.displayGuiScreen(currentOpenLexicon);
			ClientTickHandler.notifyPageChange();
			return;
		}

		LexiconPage page = entry.pages.get(this.page);

		page.onOpened(this);
		updatePageButtons();
		GuiLexiconHistory.visit(entry);
	}

	@Override
	public LexiconEntry getEntry() {
		return entry;
	}

	@Override
	public int getPageOn() {
		return page;
	}

	@Override
	boolean isMainPage() {
		return false;
	}

	@Override
	String getTitle() {
		return String.format("%s " + TextFormatting.ITALIC + "(%s/%s)", title, page + 1, entry.pages.size());
	}

	@Override
	String getSubtitle() {
		return subtitle;
	}

	@Override
	boolean isCategoryIndex() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		LexiconPage currentPage = entry.pages.get(page);
		LexiconPage newPage;

		if(par1GuiButton.id >= BOOKMARK_START)
			handleBookmark(par1GuiButton);
		else if(par1GuiButton.id == NOTES_BUTTON_ID)
			notesEnabled = !notesEnabled;
		else
			switch(par1GuiButton.id) {
			case 0 :
				currentPage.onClosed(this);
				mc.displayGuiScreen(GuiScreen.isShiftKeyDown() ? new GuiLexicon() : parent);
				ClientTickHandler.notifyPageChange();
				break;
			case 1 :
				currentPage.onClosed(this);
				page--;
				newPage = entry.pages.get(page);
				newPage.onOpened(this);

				ClientTickHandler.notifyPageChange();
				break;
			case 2 :
				currentPage.onClosed(this);
				page++;
				newPage = entry.pages.get(page);
				newPage.onOpened(this);

				ClientTickHandler.notifyPageChange();
				break;
			case 3 :
				Minecraft mc = Minecraft.getMinecraft();
				String cmd = "/botania-share " + entry.getUnlocalizedName();

				mc.ingameGUI.getChatGUI().addToSentMessages(cmd);
				mc.player.sendChatMessage(cmd);
				break;
			case 4 :
				try {
					if(Desktop.isDesktopSupported())
						Desktop.getDesktop().browse(new URI(entry.getWebLink()));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}

		updatePageButtons();
		currentPage.onActionPerformed(this, par1GuiButton);
	}

	public GuiLexiconEntry setFirstEntry() {
		firstEntry = true;
		return this;
	}

	public void updatePageButtons() {
		leftButton.enabled = page != 0;
		rightButton.enabled = page + 1 < entry.pages.size();
		if(firstEntry)
			backButton.enabled = !rightButton.enabled;
	}

	@Override
	public void drawScreenAfterScale(int xCoord, int yCoord, float newPartialTicks) {
		super.drawScreenAfterScale(xCoord, yCoord, newPartialTicks);

		LexiconPage page = entry.pages.get(this.page);
		page.renderScreen(this, xCoord, yCoord);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		LexiconPage page = entry.pages.get(this.page);
		page.updateScreen(this);

		if(this.page == entry.pages.size() - 1) {
			LexiconEntry entry = tutorial.peek();
			if(entry == this.entry) {
				tutorial.poll();
				positionTutorialArrow();
				if(tutorial.isEmpty()) {
					mc.player.sendMessage(new TextComponentTranslation("botaniamisc.tutorialEnded").setStyle(new Style().setColor(TextFormatting.RED)));
					hasTutorialArrow = false;
				}
			}
		}
	}

	@Override
	public void positionTutorialArrow() {
		LexiconEntry entry = tutorial.peek();
		if(entry != this.entry) {
			orientTutorialArrowWithButton(backButton);
			return;
		}

		if(rightButton.enabled && rightButton.visible)
			orientTutorialArrowWithButton(rightButton);
	}

	@Override
	public int getLeft() {
		return left;
	}

	@Override
	public int getTop() {
		return top;
	}

	@Override
	public int getWidth() {
		return guiWidth;
	}

	@Override
	public int getHeight() {
		return guiHeight;
	}

	@Override
	public float getZLevel() {
		return zLevel;
	}

	@Override
	public void setParent(GuiLexicon gui) {
		parent = gui;
	}

	private int fx = 0;
	private boolean swiped = false;

	@Override
	protected void mouseClickMove(int x, int y, int button, long time) {
		if(button == 0 && Math.abs(x - fx) > 100 && mc.gameSettings.touchscreen && !swiped) {
			double swipe = (x - fx) / Math.max(1, (double) time);
			if(swipe < 0.5) {
				nextPage();
				swiped = true;
			} else if(swipe > 0.5) {
				prevPage();
				swiped = true;
			}
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException {
		super.mouseClicked(par1, par2, par3);

		fx = par1;
		switch(par3) {
		case 1:
			back();
			break;
		case 3:
			nextPage();
			break;
		case 4:
			prevPage();
			break;
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		if(Mouse.getEventButton() == 0)
			swiped = false;

		int w = Mouse.getEventDWheel();
		if(w < 0)
			nextPage();
		else if(w > 0)
			prevPage();
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		handleNoteKey(par1, par2);

		LexiconPage page = entry.pages.get(this.page);
		page.onKeyPressed(par1, par2);

		if(par2 == 1) {
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		} else if(par2 == 203 || par2 == 200 || par2 == 201) // Left, Up, Page Up
			prevPage();
		else if(par2 == 205 || par2 == 208 || par2 == 209) // Right, Down Page Down
			nextPage();
		if(par2 == 14 && !notesEnabled) // Backspace
			back();
		else if(par2 == 199) { // Home
			mc.displayGuiScreen(new GuiLexicon());
			ClientTickHandler.notifyPageChange();
		}
	}

	private void back() {
		if(backButton.enabled) {
			actionPerformed(backButton);
			backButton.playPressSound(mc.getSoundHandler());
		}
	}

	private void nextPage() {
		if(rightButton.enabled) {
			actionPerformed(rightButton);
			rightButton.playPressSound(mc.getSoundHandler());
			updateNote();
		}
	}

	private void prevPage() {
		if(leftButton.enabled) {
			actionPerformed(leftButton);
			leftButton.playPressSound(mc.getSoundHandler());
			updateNote();
		}
	}

	private void updateNote() {
		String key = getNotesKey();
		note = notes.getOrDefault(key, "");
	}

	@Override
	public List<GuiButton> getButtonList() {
		return buttonList;
	}

	@Override
	public float getElapsedTicks() {
		return lastTime;
	}

	@Override
	public float getPartialTicks() {
		return partialTicks;
	}

	@Override
	public float getTickDelta() {
		return timeDelta;
	}

	@Override
	public void serialize(NBTTagCompound cmp) {
		super.serialize(cmp);
		cmp.setString(TAG_ENTRY, entry.getUnlocalizedName());
		cmp.setInteger(TAG_PAGE, page);
	}

	@Override
	public void load(NBTTagCompound cmp) {
		super.load(cmp);

		String entryStr = cmp.getString(TAG_ENTRY);
		for(LexiconEntry entry : BotaniaAPI.getAllEntries())
			if(entry.getUnlocalizedName().equals(entryStr)) {
				this.entry = entry;
				break;
			}

		page = cmp.getInteger(TAG_PAGE);

		setTitle();
	}

	@Override
	public GuiLexicon copy() {
		GuiLexiconEntry gui = new GuiLexiconEntry(entry, parent);
		gui.page = page;
		gui.setTitle();
		return gui;
	}

	@Override
	public String getNotesKey() {
		return "entry_" + entry.unlocalizedName + "_" + page;
	}
}
