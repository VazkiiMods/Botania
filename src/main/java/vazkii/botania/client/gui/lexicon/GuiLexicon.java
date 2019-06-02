/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:48:05 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.BotaniaTutorialStartEvent;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.gui.GuiBotaniaConfig;
import vazkii.botania.client.gui.lexicon.button.GuiButtonAchievement;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBookmark;
import vazkii.botania.client.gui.lexicon.button.GuiButtonCategory;
import vazkii.botania.client.gui.lexicon.button.GuiButtonChallengeInfo;
import vazkii.botania.client.gui.lexicon.button.GuiButtonChallenges;
import vazkii.botania.client.gui.lexicon.button.GuiButtonDoot;
import vazkii.botania.client.gui.lexicon.button.GuiButtonHistory;
import vazkii.botania.client.gui.lexicon.button.GuiButtonInvisible;
import vazkii.botania.client.gui.lexicon.button.GuiButtonNotes;
import vazkii.botania.client.gui.lexicon.button.GuiButtonOptions;
import vazkii.botania.client.gui.lexicon.button.GuiButtonScaleChange;
import vazkii.botania.client.gui.lexicon.button.GuiButtonUpdateWarning;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibMisc;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GuiLexicon extends GuiScreen {

	public static GuiLexicon currentOpenLexicon = new GuiLexicon();
	public static ItemStack stackUsed = ItemStack.EMPTY;

	public static final Map<String, String> notes = new HashMap<>();

	private static final String TAG_TYPE = "type";

	private static final int[] KONAMI_CODE = { 200, 200, 208, 208, 203, 205, 203, 205, 48, 30 };

	public static final int BOOKMARK_START = 1337;
	public static final int NOTES_BUTTON_ID = 1336; // random button tho
	public static final int MAX_BOOKMARK_COUNT = 8;
	public static final List<GuiLexicon> bookmarks = new ArrayList<>();
	public static final List<String> bookmarkKeys = new ArrayList<>();
	private boolean bookmarksNeedPopulation = false;

	public static final Queue<LexiconEntry> tutorial = new ArrayDeque<>();

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_LEXICON);
	public static final ResourceLocation textureToff = new ResourceLocation(LibResources.GUI_TOFF);

	public float lastTime = 0F;
	public float partialTicks = 0F;
	public float timeDelta = 0F;

	private static final int TUTORIAL_ARROW_WIDTH = 10;
	private static final int TUTORIAL_ARROW_HEIGHT = 12;
	boolean hasTutorialArrow;
	int tutorialArrowX, tutorialArrowY;
	int konamiIndex;
	int konamiTime;

	private static final int NOTE_TWEEN_TIME = 5;
	public static boolean notesEnabled;
	static int notesMoveTime;
	public String note = "";
	public String categoryHighlight = "";

	String title;
	final int guiWidth = 146;
	final int guiHeight = 180;
	int left, top;

	@Override
	public final void initGui() {
		super.initGui();

		if(PersistentVariableHelper.firstLoad) {
			PersistentVariableHelper.firstLoad = false;
			PersistentVariableHelper.saveSafe();
		}

		String key = getNotesKey();
		if(notes.containsKey(key))
			note = notes.get(key);

		onInitGui();

		putTutorialArrow();
	}

	public void onInitGui() {
		int guiScale = mc.gameSettings.guiScale;
		int persistentScale = Math.min(PersistentVariableHelper.lexiconGuiScale, getMaxAllowedScale());

		if(persistentScale > 0 && persistentScale != guiScale) {
			mc.gameSettings.guiScale = persistentScale;
			ScaledResolution res = new ScaledResolution(mc);
			width = res.getScaledWidth();
			height = res.getScaledHeight();
			mc.gameSettings.guiScale = guiScale;
		}

		lastTime = ClientTickHandler.ticksInGame;

		title = ItemLexicon.getTitle(stackUsed);
		currentOpenLexicon = this;

		left = width / 2 - guiWidth / 2;
		top = height / 2 - guiHeight / 2;

		buttonList.clear();
		if(isIndex()) {
			int x = 18;
			for(int i = 0; i < 12; i++) {
				int y = 16 + i * 12;
				buttonList.add(new GuiButtonInvisible((GuiLexiconIndex) this, i, left + x, top + y, 110, 10, ""));
			}
			populateIndex();
		} else if(isCategoryIndex()) {
			List<LexiconCategory> allCategories = new ArrayList<>(BotaniaAPI.getAllCategories());
			allCategories.removeIf(cat -> !cat.isVisible(stackUsed));
			Collections.sort(allCategories);

			int categories = allCategories.size();
			for(int i = 0; i < categories + 1; i++) {
				LexiconCategory category;
				category = i >= categories ? null : allCategories.get(i);
				int perline = 5;
				int x = i % perline;
				int y = i / perline;

				int size = 22;
				GuiButtonCategory button = new GuiButtonCategory(i, left + 18 + x * size, top + 50 + y * size, this, category);
				buttonList.add(button);
			}
		}
		populateBookmarks();
		if(isMainPage()) {
			buttonList.add(new GuiButtonOptions(-1, left + 20, top + guiHeight - 25));
			buttonList.add(new GuiButtonAchievement(-2, left + 33, top + guiHeight - 25));
			buttonList.add(new GuiButtonChallenges(-3, left + 45, top + guiHeight - 25));
			buttonList.add(new GuiButtonScaleChange(-4, left + 57, top + guiHeight - 25));

			GuiButtonUpdateWarning button = new GuiButtonUpdateWarning(-98, left - 6, top + guiHeight - 70);
			buttonList.add(button);

			if(PersistentVariableHelper.lastBotaniaVersion.equals(LibMisc.VERSION)) {
				button.enabled = false;
				button.visible = false;
			}

			LocalDateTime now = LocalDateTime.now();

			if(now.getMonth() == Month.NOVEMBER && now.getDayOfMonth() == 22)
				buttonList.add(new GuiButtonDoot(-99, left + 100, top + 12));
		}

		buttonList.add(new GuiButtonNotes(this, NOTES_BUTTON_ID, left - 4, top - 4));
	}

	@Override
	public void updateScreen() {
		if(notesEnabled && notesMoveTime < NOTE_TWEEN_TIME)
			notesMoveTime++;
		else if(!notesEnabled && notesMoveTime > 0)
			notesMoveTime--;

		if(konamiTime > 0)
			konamiTime--;
	}

	@Override
	public final void drawScreen(int x, int y, float partialTicks) {
		ScaledResolution res = new ScaledResolution(mc);
		int guiScale = mc.gameSettings.guiScale;
		
		GlStateManager.pushMatrix();
		int persistentScale = Math.min(PersistentVariableHelper.lexiconGuiScale, getMaxAllowedScale());

		if(persistentScale > 0 && persistentScale != guiScale) {
			mc.gameSettings.guiScale = persistentScale;
			float s = (float) persistentScale / (float) res.getScaleFactor();
			
			GlStateManager.scale(s, s, s);

			res = new ScaledResolution(mc);
			int sw = res.getScaledWidth();
			int sh = res.getScaledHeight();
			x = Mouse.getX() * sw / mc.displayWidth;
			y = sh - Mouse.getY() * sh / mc.displayHeight - 1;
		}

		drawScreenAfterScale(x, y, partialTicks);

		mc.gameSettings.guiScale = guiScale;
		GlStateManager.popMatrix();

		if(konamiTime > 0) {
			String meme = I18n.format("botania.subtitle.way");
			GlStateManager.pushMatrix();
			int fullWidth = fontRenderer.getStringWidth(meme);
			int left = width;
			double widthPerTick = (fullWidth + width) / 240;
			double currWidth = left - widthPerTick * (240 - (konamiTime - partialTicks)) * 3.2;

			GlStateManager.translate(currWidth, height / 2 - 10, 0);
			GlStateManager.scale(4, 4, 4);
			mc.fontRenderer.drawStringWithShadow(meme, 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
	}
	
	public void drawScreenAfterScale(int xCoord, int yCoord, float newPartialTicks) {
		float time = ClientTickHandler.ticksInGame + newPartialTicks;
		timeDelta = time - lastTime + partialTicks;
		lastTime = time;
		partialTicks = newPartialTicks;

		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.renderEngine.bindTexture(texture);
		drawNotes(newPartialTicks);

		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);

		if(ClientProxy.jingleTheBells)
			drawTexturedModalRect(left + 3, top + 1, 0, 212, 138, 6);

		String subtitle = getSubtitle();

		if(subtitle != null)
			drawBookmark(left + guiWidth / 2, top - getTitleHeight() + 10, subtitle, true, 191);
		drawBookmark(left + guiWidth / 2, top - getTitleHeight(), getTitle(), true);

		if(isMainPage())
			drawHeader();

		if(bookmarksNeedPopulation) {
			populateBookmarks();
			bookmarksNeedPopulation = false;
		}

		if(mc.player.getName().equals("haighyorkie")) {
			GlStateManager.color(1F, 1F, 1F, 1F);
			mc.renderEngine.bindTexture(texture);
			drawTexturedModalRect(left - 19, top + 42, 67, 180, 19, 26);
			if(xCoord >= left - 19 && xCoord < left && yCoord >= top + 62 && yCoord < top + 88) {
				mc.renderEngine.bindTexture(textureToff);
				GlStateManager.pushMatrix();
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.translate(0F, 0F, 2F);

				int w = 256;
				int h = 152;
				int x = (int) ((ClientTickHandler.ticksInGame + newPartialTicks) * 6) % (width + w) - w;
				int y = (int) (top + guiHeight / 2 - h / 4 + Math.sin((ClientTickHandler.ticksInGame + newPartialTicks) / 6.0) * 40);

				drawTexturedModalRect(x * 2, y * 2, 0, 0, w, h);
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();

				RenderHelper.renderTooltip(xCoord, yCoord, Arrays.asList(TextFormatting.GOLD + "#goldfishchris", TextFormatting.AQUA + "IT SAYS MANUAL"));
			}
		}

		super.drawScreen(xCoord, yCoord, newPartialTicks);

		if(hasTutorialArrow) {
			mc.renderEngine.bindTexture(texture);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 1F, 1F, 0.7F + (float) (Math.sin((ClientTickHandler.ticksInGame + newPartialTicks) * 0.3F) + 1) * 0.15F);
			drawTexturedModalRect(tutorialArrowX, tutorialArrowY, 20, 200, TUTORIAL_ARROW_WIDTH, TUTORIAL_ARROW_HEIGHT);
			GlStateManager.disableBlend();
		}
	}

	public void drawNotes(float part) {
		int size = 105;

		float time = notesMoveTime;
		if(notesMoveTime < NOTE_TWEEN_TIME && notesEnabled)
			time += part;
		else if(notesMoveTime > 0 && !notesEnabled)
			time -= part;

		int drawSize = (int) (size * time / NOTE_TWEEN_TIME);
		int x = left - drawSize;
		int y = top + 10;

		drawTexturedModalRect(x, y, 146, 0, drawSize, 125);

		String noteDisplay = note;
		if(notesEnabled && ClientTickHandler.ticksInGame % 20 < 10)
			noteDisplay += "&r_";

		fontRenderer.drawString(I18n.format("botaniamisc.notes"), x + 5, y - 7, 0x666666);

		boolean unicode = fontRenderer.getUnicodeFlag();
		fontRenderer.setUnicodeFlag(true);

		PageText.renderText(x + 5, y - 3, 92, 120, 0, true, 0, TextFormatting.RESET + noteDisplay);
		fontRenderer.setUnicodeFlag(unicode);
	}

	public void drawBookmark(int x, int y, String s, boolean drawLeft) {
		drawBookmark(x, y, s, drawLeft, 180);
	}

	public void drawBookmark(int x, int y, String s, boolean drawLeft, int v) {
		drawBookmark(x, y, s, drawLeft, 0x111111, v);
	}

	public void drawBookmark(int x, int y, String s, boolean drawLeft, int color, int v) {
		// This function is called from the buttons so I can't use fontRenderer
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		boolean unicode = font.getUnicodeFlag();
		font.setUnicodeFlag(true);
		int l = font.getStringWidth(s);
		int fontOff = 0;

		if(!drawLeft) {
			x += l / 2;
			fontOff = 2;
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GlStateManager.color(1F, 1F, 1F, 1F);
		drawTexturedModalRect(x + l / 2 + 3, y - 1, 54, v, 6, 11);

		if(drawLeft)
			drawTexturedModalRect(x - l / 2 - 9, y - 1, 61, v, 6, 11);
		for(int i = 0; i < l + 6; i++)
			drawTexturedModalRect(x - l / 2 - 3 + i, y - 1, 60, v, 1, 11);

		font.drawString(s, x - l / 2 + fontOff, y, color, false);
		font.setUnicodeFlag(unicode);
	}

	void drawHeader() {
		GlStateManager.pushMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(left - 8, top + 9, 0, 224, 140, 31);

		int color = 0xffd200;
		boolean unicode = fontRenderer.getUnicodeFlag();
		fontRenderer.drawString(title, left + 18, top + 13, color);
		fontRenderer.setUnicodeFlag(true);
		fontRenderer.drawString(I18n.format("botaniamisc.edition", ItemLexicon.getEdition()), left + 24, top + 22, color);

		String s = TextFormatting.BOLD + categoryHighlight;
		fontRenderer.drawString(s, left + guiWidth / 2 - fontRenderer.getStringWidth(s) / 2, top + 36, 0);

		fontRenderer.setUnicodeFlag(unicode);
		GlStateManager.popMatrix();

		categoryHighlight = "";
	}

	boolean isMainPage() {
		return true;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id >= BOOKMARK_START) {
			if(par1GuiButton.id >= BOOKMARK_START + MAX_BOOKMARK_COUNT) {
				if(par1GuiButton instanceof GuiButtonChallengeInfo)
					mc.displayGuiScreen(new GuiLexiconEntry(LexiconData.challenges, this));
				else mc.displayGuiScreen(new GuiLexiconHistory());
				ClientTickHandler.notifyPageChange();
			} else handleBookmark(par1GuiButton);
		} else if(par1GuiButton instanceof GuiButtonCategory) {
			LexiconCategory category = ((GuiButtonCategory) par1GuiButton).getCategory();

			mc.displayGuiScreen(new GuiLexiconIndex(category));
			ClientTickHandler.notifyPageChange();
		} else switch(par1GuiButton.id) {
		case -1 :
			mc.displayGuiScreen(new GuiBotaniaConfig(this));
			break;
		case -2 :
			if(mc.player != null) {
				GuiScreenAdvancements gui = new GuiScreenAdvancements(this.mc.player.connection.getAdvancementManager());
				this.mc.displayGuiScreen(gui);
				ResourceLocation tab = new ResourceLocation(LibMisc.MOD_ID, "main/root");
				gui.setSelectedTab(mc.player.connection.getAdvancementManager().getAdvancementList().getAdvancement(tab));
			}
			break;
		case -3 :
			mc.displayGuiScreen(new GuiLexiconChallengesList());
			break;
		case -4:
			int maxAllowed = getMaxAllowedScale();
			if(PersistentVariableHelper.lexiconGuiScale >= maxAllowed)
				PersistentVariableHelper.lexiconGuiScale = 2;
			else PersistentVariableHelper.lexiconGuiScale++;

			PersistentVariableHelper.saveSafe();
			mc.displayGuiScreen(new GuiLexicon());
			break;
		case -98 :
			if(isShiftKeyDown()) {
				try {
					if(Desktop.isDesktopSupported())
						Desktop.getDesktop().browse(new URI("http://botaniamod.net/changelog.php#" + PersistentVariableHelper.lastBotaniaVersion.replaceAll("\\.|\\s", "-")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				PersistentVariableHelper.lastBotaniaVersion = LibMisc.VERSION;
				PersistentVariableHelper.saveSafe();
				par1GuiButton.visible = false;
				par1GuiButton.enabled = false;
			}

			break;
		case NOTES_BUTTON_ID :
			notesEnabled = !notesEnabled;
			break;
		}
	}

	public void handleBookmark(GuiButton par1GuiButton) {
		boolean modified = false;
		int i = par1GuiButton.id - BOOKMARK_START;
		String key = getNotesKey();
		if(i == bookmarks.size()) {
			if(!bookmarkKeys.contains(key)) {
				bookmarks.add(copy());
				bookmarkKeys.add(key);
				modified = true;
			}
		} else {
			if(isShiftKeyDown()) {
				bookmarks.remove(i);
				bookmarkKeys.remove(i);

				modified = true;
			} else {
				GuiLexicon bookmark = bookmarks.get(i).copy();
				if(!bookmark.getTitle().equals(getTitle())) {
					Minecraft.getMinecraft().displayGuiScreen(bookmark);
					if(bookmark instanceof IParented)
						((IParented) bookmark).setParent(this);
					ClientTickHandler.notifyPageChange();
				}
			}
		}

		bookmarksNeedPopulation = true;
		if(modified)
			PersistentVariableHelper.saveSafe();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public int bookmarkWidth(String b) {
		if(fontRenderer == null)
			fontRenderer = Minecraft.getMinecraft().fontRenderer;

		boolean unicode = fontRenderer.getUnicodeFlag();
		fontRenderer.setUnicodeFlag(true);
		int width = fontRenderer.getStringWidth(b) + 15;
		fontRenderer.setUnicodeFlag(unicode);
		return width;
	}

	String getTitle() {
		return title;
	}

	String getSubtitle() {
		return null;
	}

	int getTitleHeight() {
		return getSubtitle() == null ? 12 : 22;
	}

	boolean isIndex() {
		return false;
	}

	boolean isChallenge() {
		return false;
	}

	boolean isCategoryIndex() {
		return true;
	}
	
	public static int getMaxAllowedScale() {
		Minecraft mc = Minecraft.getMinecraft();
		int scale = mc.gameSettings.guiScale;
		mc.gameSettings.guiScale = 0;
		ScaledResolution res = new ScaledResolution(mc);
		mc.gameSettings.guiScale = scale;
		
		return res.getScaleFactor();
	}

	void populateIndex() {
		List<LexiconCategory> categoryList = BotaniaAPI.getAllCategories();
		int shift = 2;
		for(int i = shift; i < 12; i++) {
			int i_ = i - shift;
			GuiButtonInvisible button = (GuiButtonInvisible) buttonList.get(i);
			LexiconCategory category = i_ >= categoryList.size() ? null : categoryList.get(i_);
			if(category != null)
				button.displayString = I18n.format(category.getUnlocalizedName());
			else {
				button.displayString = I18n.format("botaniamisc.lexiconIndex");
				break;
			}
		}
	}

	void populateBookmarks() {
		List<GuiButton> remove = new ArrayList<>();
		List<GuiButton> buttons = buttonList;
		for(GuiButton button : buttons)
			if(button.id >= BOOKMARK_START)
				remove.add(button);
		buttonList.removeAll(remove);

		int len = bookmarks.size();
		boolean thisExists = false;
		for(GuiLexicon lex : bookmarks)
			if(lex.getTitle().equals(getTitle()))
				thisExists = true;

		boolean addEnabled = len < MAX_BOOKMARK_COUNT && this instanceof IParented && !thisExists;
		for(int i = 0; i < len + (addEnabled ? 1 : 0); i++) {
			boolean isAdd = i == bookmarks.size();
			GuiLexicon gui = isAdd ? null : bookmarks.get(i);
			buttonList.add(new GuiButtonBookmark(BOOKMARK_START + i, left + 138, top + 18 + 14 * i, gui == null ? this : gui, gui == null ? "+" : gui.getTitle()));
		}

		if(isMainPage())
			buttonList.add(new GuiButtonHistory(BOOKMARK_START + MAX_BOOKMARK_COUNT, left + 138, top + guiHeight - 24, I18n.format("botaniamisc.history"), this));
		else if(isChallenge())
			buttonList.add(new GuiButtonChallengeInfo(BOOKMARK_START + MAX_BOOKMARK_COUNT, left + 138, top + guiHeight - 24, I18n.format("botaniamisc.info"), this));
	}

	public static void startTutorial() {
		tutorial.clear();

		tutorial.add(LexiconData.lexicon);
		tutorial.add(LexiconData.flowers);
		tutorial.add(LexiconData.apothecary);
		tutorial.add(LexiconData.pureDaisy);
		tutorial.add(LexiconData.wand);
		tutorial.add(LexiconData.manaIntro);
		tutorial.add(LexiconData.pool);
		tutorial.add(LexiconData.spreader);
		tutorial.add(LexiconData.generatingIntro);
		tutorial.add(LexiconData.endoflame);
		tutorial.add(LexiconData.functionalIntro);
		tutorial.add(LexiconData.runicAltar);
		if(Botania.gardenOfGlassLoaded)
			tutorial.add(LexiconData.gardenOfGlass);

		MinecraftForge.EVENT_BUS.post(new BotaniaTutorialStartEvent(tutorial));
	}

	public final void putTutorialArrow() {
		hasTutorialArrow = !tutorial.isEmpty();
		if(hasTutorialArrow)
			positionTutorialArrow();
	}

	public void positionTutorialArrow() {
		LexiconEntry entry = tutorial.peek();
		LexiconCategory category = entry.category;

		List<GuiButton> buttons = buttonList;
		for(GuiButton button : buttons)
			if(button instanceof GuiButtonCategory) {
				GuiButtonCategory catButton = (GuiButtonCategory) button;
				if(catButton.getCategory() == category) {
					orientTutorialArrowWithButton(button);
					break;
				}
			}
	}

	public void orientTutorialArrowWithButton(GuiButton button) {
		tutorialArrowX = button.x - TUTORIAL_ARROW_WIDTH;
		tutorialArrowY = button.y - TUTORIAL_ARROW_HEIGHT;
	}

	boolean closeScreenOnInvKey() {
		return true;
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		handleNoteKey(par1, par2);

		if(!notesEnabled && closeScreenOnInvKey() && mc.gameSettings.keyBindInventory.getKeyCode() == par2) {
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}

		if(konamiTime == 0 && par2 == KONAMI_CODE[konamiIndex]) {
			konamiIndex++;
			if(konamiIndex >= KONAMI_CODE.length) {
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(ModSounds.way, 1.0F));
				konamiIndex = 0;
				konamiTime = 240;
			}
		} else konamiIndex = 0;

		super.keyTyped(par1, par2);
	}

	public void handleNoteKey(char par1, int par2) {
		if(notesEnabled) {
			Keyboard.enableRepeatEvents(true);
			boolean changed = false;

			if(par2 == 14 && note.length() > 0) {
				if(isCtrlKeyDown())
					note = "";
				else {
					if(note.endsWith("<br>"))
						note = note.substring(0, note.length() - 4);
					else note = note.substring(0, note.length() - 1);
				}
				changed = true;
			}

			if((ChatAllowedCharacters.isAllowedCharacter(par1) || par2 == 28) && note.length() < 250) {
				note += par2 == 28 ? "<br>" : par1;
				changed = true;
			}

			if(changed) {
				notes.put(getNotesKey(), note);
				PersistentVariableHelper.saveSafe();
			}
		} else Keyboard.enableRepeatEvents(false);
	}

	public static GuiLexicon create(NBTTagCompound cmp) {
		String type = cmp.getString(TAG_TYPE);
		try {
			GuiLexicon lex = (GuiLexicon) Class.forName(type).newInstance();
			if(lex != null)
				lex.load(cmp);
			if(isValidLexiconGui(lex))
				return lex;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void serialize(NBTTagCompound cmp) {
		cmp.setString(TAG_TYPE, getClass().getName());
	}

	public String getNotesKey() {
		return "index";
	}

	public void load(NBTTagCompound cmp) {}

	public GuiLexicon copy() {
		return new GuiLexicon();
	}

	public static boolean isValidLexiconGui(GuiLexicon gui)	{
		if(gui == null)
			return false;
		if(gui.isCategoryIndex() || gui.isChallenge())
			return true;
		if(gui.isIndex()) {
			GuiLexiconIndex indexGui = (GuiLexiconIndex) gui;
			if(indexGui.category == null)
				return true;
			return BotaniaAPI.getAllCategories().contains(indexGui.category);
		}

		GuiLexiconEntry entryGui = (GuiLexiconEntry) gui;
		if(!BotaniaAPI.getAllEntries().contains(entryGui.entry))
			return false;

		return entryGui.page < entryGui.entry.pages.size();
	}
}

