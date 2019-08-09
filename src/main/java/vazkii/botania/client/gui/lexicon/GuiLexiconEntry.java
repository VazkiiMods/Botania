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
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
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
import java.net.URI;
import java.util.List;

public class GuiLexiconEntry extends GuiLexicon implements IGuiLexiconEntry, IParented {

	private static final String TAG_ENTRY = "entry";
	private static final String TAG_PAGE = "page";

	public int page = 0;
	private boolean firstEntry = false;
	LexiconEntry entry;
	private Screen parent;
	private String title;
	private String subtitle;

	private Button leftButton, rightButton, backButton;

	public GuiLexiconEntry() {
		parent = new GuiLexicon();
		setTitle();
	}

	public GuiLexiconEntry(LexiconEntry entry, Screen parent) {
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

		addButton(backButton = new GuiButtonBackWithShift(left + guiWidth / 2 - 8, top + guiHeight + 2, b -> {
			entry.pages.get(page).onClosed(GuiLexiconEntry.this);
			mc.displayGuiScreen(Screen.hasShiftDown() ? new GuiLexicon() : parent);
			ClientTickHandler.notifyPageChange();
		}));
		addButton(leftButton = new GuiButtonPage(left, top + guiHeight - 10, false, b -> {
			entry.pages.get(page).onClosed(GuiLexiconEntry.this);
			page--;
			entry.pages.get(page).onOpened(GuiLexiconEntry.this);

			ClientTickHandler.notifyPageChange();
			updatePageButtons();
		}));
		addButton(rightButton = new GuiButtonPage(left + guiWidth - 18, top + guiHeight - 10, true, b -> {
			entry.pages.get(page).onClosed(GuiLexiconEntry.this);
			page++;
			entry.pages.get(page).onOpened(GuiLexiconEntry.this);

			ClientTickHandler.notifyPageChange();
			updatePageButtons();
		}));
		addButton(new GuiButtonShare(left + guiWidth - 6, top - 2, b -> {
			Minecraft mc = Minecraft.getInstance();
			String cmd = "/botania-share " + entry.getUnlocalizedName();

			mc.ingameGUI.getChatGUI().addToSentMessages(cmd);
			mc.player.sendChatMessage(cmd);
		}));
		if(entry.getWebLink() != null)
			addButton(new GuiButtonViewOnline(left - 8, top + 12, b -> {
				try {
					if(Desktop.isDesktopSupported())
						Desktop.getDesktop().browse(new URI(entry.getWebLink()));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}));

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
	public ITextComponent getTitle() {
		return new StringTextComponent(String.format("%s " + TextFormatting.ITALIC + "(%s/%s)", title, page + 1, entry.pages.size()));
	}

	@Override
	String getSubtitle() {
		return subtitle;
	}

	@Override
	boolean isCategoryIndex() {
		return false;
	}

	public GuiLexiconEntry setFirstEntry() {
		firstEntry = true;
		return this;
	}

	private void updatePageButtons() {
		leftButton.active = page != 0;
		rightButton.active = page + 1 < entry.pages.size();
		if(firstEntry)
			backButton.active = !rightButton.active;
	}

	@Override
	public void drawScreenAfterScale(int xCoord, int yCoord, float newPartialTicks) {
		super.drawScreenAfterScale(xCoord, yCoord, newPartialTicks);

		LexiconPage page = entry.pages.get(this.page);
		page.renderScreen(this, xCoord, yCoord);
	}

	@Override
	public void tick() {
		super.tick();

		LexiconPage page = entry.pages.get(this.page);
		page.updateScreen(this);

		if(this.page == entry.pages.size() - 1) {
			LexiconEntry entry = tutorial.peek();
			if(entry == this.entry) {
				tutorial.poll();
				positionTutorialArrow();
				if(tutorial.isEmpty()) {
					mc.player.sendMessage(new TranslationTextComponent("botaniamisc.tutorialEnded").setStyle(new Style().setColor(TextFormatting.RED)));
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

		if(rightButton.active && rightButton.visible)
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
		return blitOffset;
	}

	@Override
	public void setParent(GuiLexicon gui) {
		parent = gui;
	}

	private boolean swiped = false;

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && dragX > 100 && mc.gameSettings.touchscreen && !swiped) {
			// todo 1.13 correct these constants
			if(dragX < 0.5) {
				nextPage();
				swiped = true;
			} else if(dragX > 0.5) {
				prevPage();
				swiped = true;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			swiped = false;
		}

		switch(button) {
		case 1:
			back();
			return true;
		case 3:
			nextPage();
			return true;
		case 4:
			prevPage();
			return true;
		default: return super.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double w) {
		if(w < 0)
			nextPage();
		else if(w > 0)
			prevPage();
		return true;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int mods) {
		LexiconPage page = entry.pages.get(this.page);
		if (page.keyPressed(keyCode, scanCode, mods)) {
			return true;
		}

		if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
			mc.displayGuiScreen(null);
			mc.mouseHelper.grabMouse();
		} else if(keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_PAGE_UP)
			prevPage();
		else if(keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_PAGE_DOWN)
			nextPage();
		if(keyCode == GLFW.GLFW_KEY_BACKSPACE && !notesEnabled)
			back();
		else if(keyCode == GLFW.GLFW_KEY_HOME) { // Home
			mc.displayGuiScreen(new GuiLexicon());
			ClientTickHandler.notifyPageChange();
		}

		return super.keyPressed(keyCode, scanCode, mods);
	}

	private void back() {
		if(backButton.active) {
			backButton.playDownSound(mc.getSoundHandler());
			backButton.onClick(backButton.x, backButton.y);
		}
	}

	private void nextPage() {
		if(rightButton.active) {
			rightButton.playDownSound(mc.getSoundHandler());
			rightButton.onClick(rightButton.x, rightButton.y);
			updateNote();
		}
	}

	private void prevPage() {
		if(leftButton.active) {
			leftButton.playDownSound(mc.getSoundHandler());
			leftButton.onClick(leftButton.x, leftButton.y);
			updateNote();
		}
	}

	private void updateNote() {
		String key = getNotesKey();
		note = notes.getOrDefault(key, "");
	}

	@Override
	public List<Widget> getButtonList() {
		return buttons;
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
	public void serialize(CompoundNBT cmp) {
		super.serialize(cmp);
		cmp.putString(TAG_ENTRY, entry.getUnlocalizedName());
		cmp.putInt(TAG_PAGE, page);
	}

	@Override
	public void load(CompoundNBT cmp) {
		super.load(cmp);

		String entryStr = cmp.getString(TAG_ENTRY);
		for(LexiconEntry entry : BotaniaAPI.getAllEntries())
			if(entry.getUnlocalizedName().equals(entryStr)) {
				this.entry = entry;
				break;
			}

		page = cmp.getInt(TAG_PAGE);

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
