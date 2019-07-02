/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:46:59 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBack;
import vazkii.botania.client.gui.lexicon.button.GuiButtonIndexEntry;
import vazkii.botania.client.gui.lexicon.button.GuiButtonPage;
import vazkii.botania.common.lexicon.DogLexiconEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiLexiconIndex extends GuiLexicon implements IParented {

	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PAGE = "page";

	LexiconCategory category;
	String title;
	public int page = 0;

	private int tutPage = -1;

	private Button leftButton, rightButton, backButton;
	private GuiLexicon parent;
	TextFieldWidget searchField;

	private Button currentButton;
	private LexiconEntry currentEntry;
	private float infoTime;

	final List<LexiconEntry> entriesToDisplay = new ArrayList<>();

	public GuiLexiconIndex(LexiconCategory category) {
		super(new TranslationTextComponent("botaniamisc.lexiconIndex"));
		this.category = category;
		this.parent = new GuiLexicon();
	}

	@Override
	boolean isMainPage() {
		return false;
	}

	@Override
	public ITextComponent getTitle() {
		return category == null ? super.getTitle() : new TranslationTextComponent(category.getUnlocalizedName());
	}

	@Override
	boolean isIndex() {
		return true;
	}

	@Override
	boolean isCategoryIndex() {
		return false;
	}

	@Override
	public void onInitGui() {
		super.onInitGui();

		if(!GuiLexicon.isValidLexiconGui(this))	{
			currentOpenLexicon = new GuiLexicon();
			mc.displayGuiScreen(currentOpenLexicon);
			ClientTickHandler.notifyPageChange();
			return;
		}

		buttons.add(backButton = new GuiButtonBack(left + guiWidth / 2 - 8, top + guiHeight + 2, b -> {
			mc.displayGuiScreen(parent);
			ClientTickHandler.notifyPageChange();
		}));
		buttons.add(leftButton = new GuiButtonPage(left, top + guiHeight - 10, false, b -> {
			page--;
			updatePageButtons();
			populateIndex();
			ClientTickHandler.notifyPageChange();
		}));
		buttons.add(rightButton = new GuiButtonPage(left + guiWidth - 18, top + guiHeight - 10, true, b -> {
			page++;
			updatePageButtons();
			populateIndex();
			ClientTickHandler.notifyPageChange();
		}));

		searchField = new TextFieldWidget(font, left + guiWidth / 2 + 28, top + guiHeight + 6, 200, 10, "");
		searchField.setCanLoseFocus(false);
		searchField.changeFocus(true);
		searchField.setEnableBackgroundDrawing(false);

		updateAll();
	}

	private void updateAll() {
		buildEntries();
		updatePageButtons();
		populateIndex();
	}

	void buildEntries() {
		entriesToDisplay.clear();
		ILexicon lex = (ILexicon) stackUsed.getItem();
		for(LexiconEntry entry : category == null ? BotaniaAPI.getAllEntries() : category.entries) {
			if(entry.isVisible() && lex.isKnowledgeUnlocked(stackUsed, entry.getKnowledgeType()) && matchesSearch(entry))
				entriesToDisplay.add(entry);
		}
		Collections.sort(entriesToDisplay);
	}

	private boolean matchesSearch(LexiconEntry e) {
		String search = searchField.getText().trim();
		if(search.isEmpty())
			return true;

		search = search.toLowerCase();
		if(I18n.format(e.getUnlocalizedName()).toLowerCase().contains(search))
			return true;

		for(ItemStack stack : e.getDisplayedRecipes()) {
			String stackName = stack.getDisplayName().getString().trim();
			if(stackName.contains(search))
				return true;
		}

		return false;
	}

	@Override
	void populateIndex() {
		LexiconEntry tutEntry = !tutorial.isEmpty() ? tutorial.peek() : null;

		for(int i = page * 12; i < (page + 1) * 12; i++) {
			GuiButtonIndexEntry button = (GuiButtonIndexEntry) buttons.get(i - page * 12);
			LexiconEntry entry = i >= entriesToDisplay.size() ? null : entriesToDisplay.get(i);
			if(entry != null) {
				button.setMessage(entry.getKnowledgeType().color + "" + (entry.isPriority() ? TextFormatting.ITALIC : "") + I18n.format(entry.getUnlocalizedName()));
				button.displayStack = entry.getIcon();
				if(entry == tutEntry)
					tutPage = page;

				if(entry instanceof DogLexiconEntry)
					button.dog = true;
			} else button.setMessage("");
		}
	}

	public void setHoveredButton(GuiButtonIndexEntry b) {
		if(b == null)
			currentEntry = null;
		else currentEntry = entriesToDisplay.get(b.ordinal + page * 12);
		currentButton = b;
	}

	@Override
	public void drawScreenAfterScale(int xCoord, int yCoord, float newPartialTicks) {
		super.drawScreenAfterScale(xCoord, yCoord, newPartialTicks);

		if(!searchField.getText().isEmpty()) {
			drawBookmark(left + 138, top + guiHeight - 24, "  " + searchField.getText(), false);
			mc.textureManager.bindTexture(texture);
			GlStateManager.color4f(1F, 1F, 1F, 1F);
			blit(left + 134, top + guiHeight - 26, 86, 180, 12, 12);

			if(entriesToDisplay.size() == 1) {
				String s = I18n.format("botaniamisc.enterToView");
				mc.fontRenderer.drawString(s, left + guiWidth / 2 - mc.fontRenderer.getStringWidth(s) / 2, top + 30, 0x666666);
			}
		} else {
			String s = I18n.format("botaniamisc.typeToSearch");
			mc.fontRenderer.drawString(s, left + 120 - mc.fontRenderer.getStringWidth(s), top + guiHeight - 18, 0x666666);
		}

		float animationTime = 4F;
		if(hasShiftDown()) {
			if(currentButton != null)
				infoTime = Math.min(animationTime, infoTime + timeDelta);
		} else {
			infoTime = Math.max(0, infoTime - timeDelta);

			if(currentButton != null && infoTime == 0) {
				int x;
				int y;

				x = currentButton.x - 20;
				y = currentButton.y;

				mc.fontRenderer.drawStringWithShadow("?", x, y, 0xFFFFFF);
				GlStateManager.scalef(0.5F, 0.5F, 1F);
				mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "Shift", x * 2 - 6, y * 2 + 20, 0xFFFFFF);
				GlStateManager.scalef(2F, 2F, 1F);
			}
		}

		if(currentButton != null && infoTime > 0) {
			float fract = infoTime / animationTime;

			int x = currentButton.x;
			int y = currentButton.y;
			String s = I18n.format(currentEntry.getTagline());
			int width = mc.fontRenderer.getStringWidth(s);

			GlStateManager.pushMatrix();
			GlStateManager.translatef(x, y, 0);
			GlStateManager.scalef(fract, 1F, 1F);
			AbstractGui.fill(12, -30, width + 20, -2, 0x44000000);
			AbstractGui.fill(10, -32, width + 22, -2, 0x44000000);

			drawBookmark(width / 2 + 16, -8, s, true, 0xFFFFFF, 180);

			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			ItemStack paper = new ItemStack(Items.PAPER, currentEntry.pages.size());

			mc.getItemRenderer().renderItemAndEffectIntoGUI(paper, 14, -28);
			mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, paper, 14, -28);
			List<ItemStack> stacks = currentEntry.getDisplayedRecipes();

			if(stacks.size() > 0) {
				int spaceForEach = Math.min(18, (width - 30) / stacks.size());
				for(int i = 0; i < stacks.size(); i++) {
					ItemStack stack = stacks.get(i);
					mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 38 + spaceForEach * i, -28);
				}
			}

			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

			GlStateManager.popMatrix();
		}

		setHoveredButton(null);
	}

	@Override
	public void positionTutorialArrow() {
		LexiconEntry entry = tutorial.peek();
		LexiconCategory category = entry.category;
		if(category != this.category) {
			orientTutorialArrowWithButton(backButton);
			return;
		}

		if(tutPage != -1 && tutPage != page) {
			orientTutorialArrowWithButton(tutPage < page ? leftButton : rightButton);
			return;
		}

		for(Widget widget : buttons) {
			if(widget instanceof GuiButtonIndexEntry) {
				GuiButtonIndexEntry button = (GuiButtonIndexEntry) widget;
				int id = button.ordinal;
				int index = id + page * 12;
				if(index >= entriesToDisplay.size())
					continue;

				if(entry == entriesToDisplay.get(index)) {
					orientTutorialArrowWithButton(id >= 12 ? rightButton : button);
					break;
				}
			}
		}
	}

	public void openEntry(int index) {
		if(index >= entriesToDisplay.size())
			return;

		LexiconEntry entry = entriesToDisplay.get(index);
		mc.displayGuiScreen(new GuiLexiconEntry(entry, this));
		ClientTickHandler.notifyPageChange();
	}

	private void updatePageButtons() {
		leftButton.active = page != 0;
		rightButton.active = page < (entriesToDisplay.size() - 1) / 12;
		putTutorialArrow();
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
			} else if(dragY > 0.5) {
				prevPage();
				swiped = true;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
			swiped = false;

		// todo 1.13 might not be needed with focus changes in super?
		if (searchField.mouseClicked(mouseX, mouseY, button))
			return true;

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
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double x, double y, double w) {
		if(w < 0)
			nextPage();
		else if(w > 0)
			prevPage();
		return true;
	}

	@Override
	boolean closeScreenOnInvKey() {
		return false;
	}

	@Override
	public boolean charTyped(char codePoint, int mods) {
		if(!notesEnabled) {
			// todo 1.13 use focus system?
			String search = searchField.getText();
			searchField.charTyped(codePoint, mods);
			if(!searchField.getText().equalsIgnoreCase(search))
				updateAll();
			return true;
		}
		return super.charTyped(codePoint, mods);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int mods) {
		if(keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_PAGE_UP) {
			prevPage();
			return true;
		} else if(keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
			nextPage();
			return true;
		} else if(keyCode == GLFW.GLFW_KEY_BACKSPACE && !notesEnabled && searchField.getText().isEmpty()) {
			back();
			return true;
		} else if(keyCode == GLFW.GLFW_KEY_HOME) {
			mc.displayGuiScreen(new GuiLexicon());
			ClientTickHandler.notifyPageChange();
			return true;
		} else if(keyCode == GLFW.GLFW_KEY_ENTER && entriesToDisplay.size() == 1) {
			openEntry(0);
			return true;
		} else {
			return super.keyPressed(keyCode, scanCode, mods);
		}
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
		}
	}

	private void prevPage() {
		if(leftButton.active) {
			leftButton.playDownSound(mc.getSoundHandler());
			leftButton.onClick(leftButton.x, leftButton.y);
		}
	}

	@Override
	public void serialize(CompoundNBT cmp) {
		super.serialize(cmp);
		cmp.putString(TAG_CATEGORY, category == null ? "" : category.getUnlocalizedName());
		cmp.putInt(TAG_PAGE, page);
	}

	@Override
	public void load(CompoundNBT cmp) {
		super.load(cmp);
		String categoryStr = cmp.getString(TAG_CATEGORY);
		if(categoryStr.isEmpty())
			category = null;
		else for(LexiconCategory cat : BotaniaAPI.getAllCategories())
			if(cat.getUnlocalizedName().equals(categoryStr)) {
				category = cat;
				break;
			}
		page = cmp.getInt(TAG_PAGE);
	}

	@Override
	public GuiLexicon copy() {
		GuiLexiconIndex gui = new GuiLexiconIndex(category);
		gui.page = page;
		return gui;
	}

	@Override
	public String getNotesKey() {
		return "category_" + (category == null ? "lexindex" : category.unlocalizedName);
	}
}

