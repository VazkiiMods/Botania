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
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.BotaniaTutorialStartEvent;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.gui.lexicon.button.GuiButtonAchievement;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBookmark;
import vazkii.botania.client.gui.lexicon.button.GuiButtonCategory;
import vazkii.botania.client.gui.lexicon.button.GuiButtonChallengeInfo;
import vazkii.botania.client.gui.lexicon.button.GuiButtonChallenges;
import vazkii.botania.client.gui.lexicon.button.GuiButtonDoot;
import vazkii.botania.client.gui.lexicon.button.GuiButtonHistory;
import vazkii.botania.client.gui.lexicon.button.GuiButtonIndexEntry;
import vazkii.botania.client.gui.lexicon.button.GuiButtonNotes;
import vazkii.botania.client.gui.lexicon.button.GuiButtonOptions;
import vazkii.botania.client.gui.lexicon.button.GuiButtonScaleChange;
import vazkii.botania.client.gui.lexicon.button.GuiButtonUpdateWarning;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
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

public class GuiLexicon extends Screen {

	public static GuiLexicon currentOpenLexicon = new GuiLexicon();
	public static ItemStack stackUsed = ItemStack.EMPTY;

	public static final Map<String, String> notes = new HashMap<>();

	private static final String TAG_TYPE = "type";

	private static final int[] KONAMI_CODE = {
			GLFW.GLFW_KEY_UP, GLFW.GLFW_KEY_UP, GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_DOWN,
			GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT, GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT,
			GLFW.GLFW_KEY_B, GLFW.GLFW_KEY_A
	};

	public static final int BOOKMARK_START = 1337;
	public static final int MAX_BOOKMARK_COUNT = 8;
	public static final List<GuiLexicon> bookmarks = new ArrayList<>();
	public static final List<String> bookmarkKeys = new ArrayList<>();
	public boolean bookmarksNeedPopulation = false;

	public static final Queue<LexiconEntry> tutorial = new ArrayDeque<>();

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_LEXICON);
	public static final ResourceLocation textureToff = new ResourceLocation(LibResources.GUI_TOFF);

	public float lastTime = 0F;
	public float partialTicks = 0F;
	public float timeDelta = 0F;

	private static final int TUTORIAL_ARROW_WIDTH = 10;
	private static final int TUTORIAL_ARROW_HEIGHT = 12;
	boolean hasTutorialArrow;
	private int tutorialArrowX, tutorialArrowY;
	private int konamiIndex;
	private int konamiTime;

	private static final int NOTE_TWEEN_TIME = 5;
	public static boolean notesEnabled;
	private static int notesMoveTime;
	public String note = "";
	public String categoryHighlight = "";

	final int guiWidth = 146;
	final int guiHeight = 180;
	protected final Minecraft mc = Minecraft.getInstance();
	int left, top;

	public GuiLexicon() {
		this(new TranslationTextComponent(ModItems.lexicon.getTranslationKey()));
	}

	protected GuiLexicon(ITextComponent title) {
		super(title);
	}

	@Override
	public final void init() {
		super.init();

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
			// Fake the scale temporarily so we can get the scaled width from mainWindow
			mc.gameSettings.guiScale = persistentScale;
			mc.updateWindowSize();

			// Get the info
			width = mc.mainWindow.getScaledWidth();
			height = mc.mainWindow.getScaledHeight();

			// Change it back
			mc.gameSettings.guiScale = guiScale;
			mc.updateWindowSize();
		}

		List<LexiconCategory> allCategories = new ArrayList<>(BotaniaAPI.getAllCategories());
		Collections.sort(allCategories);

		lastTime = ClientTickHandler.ticksInGame;

		currentOpenLexicon = this;

		left = width / 2 - guiWidth / 2;
		top = height / 2 - guiHeight / 2;

		buttons.clear();
		if(isIndex()) {
			int x = 18;
			for(int i = 0; i < 12; i++) {
				int y = 16 + i * 12;
				buttons.add(new GuiButtonIndexEntry((GuiLexiconIndex) this, i, left + x, top + y, 110, 10, ""));
			}
			populateIndex();
		} else if(isCategoryIndex()) {
			int categories = allCategories.size();
			for(int i = 0; i < categories + 1; i++) {
				LexiconCategory category;
				category = i >= categories ? null : allCategories.get(i);
				int perline = 5;
				int x = i % perline;
				int y = i / perline;

				int size = 22;
				GuiButtonCategory button = new GuiButtonCategory(left + 18 + x * size, top + 50 + y * size, this, category);
				buttons.add(button);
			}
		}
		populateBookmarks();
		if(isMainPage()) {
			buttons.add(new GuiButtonOptions(left + 20, top + guiHeight - 25));
			buttons.add(new GuiButtonAchievement(left + 33, top + guiHeight - 25));
			buttons.add(new GuiButtonChallenges(left + 45, top + guiHeight - 25));
			buttons.add(new GuiButtonScaleChange(left + 57, top + guiHeight - 25));

			GuiButtonUpdateWarning button = new GuiButtonUpdateWarning(left - 6, top + guiHeight - 70);
			buttons.add(button);

			if(PersistentVariableHelper.lastBotaniaVersion.equals(LibMisc.VERSION)) {
				button.active = false;
				button.visible = false;
			}

			LocalDateTime now = LocalDateTime.now();

			if(now.getMonth() == Month.NOVEMBER && now.getDayOfMonth() == 22)
				buttons.add(new GuiButtonDoot(left + 100, top + 12));
		}

		buttons.add(new GuiButtonNotes(this, left - 4, top - 4));
	}

	@Override
	public void tick() {
		if(notesEnabled && notesMoveTime < NOTE_TWEEN_TIME)
			notesMoveTime++;
		else if(!notesEnabled && notesMoveTime > 0)
			notesMoveTime--;

		if(konamiTime > 0)
			konamiTime--;
	}

	@Override
	public final void render(int x, int y, float partialTicks) {
		int guiScale = mc.gameSettings.guiScale;
		
		GlStateManager.pushMatrix();
		int persistentScale = Math.min(PersistentVariableHelper.lexiconGuiScale, getMaxAllowedScale());

		if(persistentScale > 0 && persistentScale != guiScale) {
			mc.gameSettings.guiScale = persistentScale;
			float s = (float) persistentScale / (float) mc.mainWindow.getGuiScaleFactor();
			
			GlStateManager.scalef(s, s, s);

			int sw = mc.mainWindow.getScaledWidth();
			int sh = mc.mainWindow.getScaledHeight();
			x = (int) (mc.mouseHelper.getMouseX() * sw / mc.mainWindow.getWidth());
			y = (int) (sh - mc.mouseHelper.getMouseY() * sh / mc.mainWindow.getHeight() - 1);
		}

		drawScreenAfterScale(x, y, partialTicks);

		mc.gameSettings.guiScale = guiScale;
		GlStateManager.popMatrix();

		if(konamiTime > 0) {
			String meme = I18n.format("botania.subtitle.way");
			GlStateManager.pushMatrix();
			int fullWidth = font.getStringWidth(meme);
			int left = width;
			double widthPerTick = (fullWidth + width) / 240;
			double currWidth = left - widthPerTick * (240 - (konamiTime - partialTicks)) * 3.2;

			GlStateManager.translated(currWidth, height / 2 - 10, 0);
			GlStateManager.scalef(4, 4, 4);
			mc.fontRenderer.drawStringWithShadow(meme, 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
	}
	
	public void drawScreenAfterScale(int xCoord, int yCoord, float newPartialTicks) {
		float time = ClientTickHandler.ticksInGame + newPartialTicks;
		timeDelta = time - lastTime + partialTicks;
		lastTime = time;
		partialTicks = newPartialTicks;

		GlStateManager.color4f(1F, 1F, 1F, 1F);
		mc.textureManager.bindTexture(texture);
		drawNotes(newPartialTicks);

		GlStateManager.color4f(1F, 1F, 1F, 1F);
		mc.textureManager.bindTexture(texture);
		blit(left, top, 0, 0, guiWidth, guiHeight);

		if(ClientProxy.jingleTheBells)
			blit(left + 3, top + 1, 0, 212, 138, 6);

		String subtitle = getSubtitle();

		if(subtitle != null)
			drawBookmark(left + guiWidth / 2, top - getTitleHeight() + 10, subtitle, true, 191);
		drawBookmark(left + guiWidth / 2, top - getTitleHeight(), getTitle().getFormattedText(), true);

		if(isMainPage())
			drawHeader();

		if(bookmarksNeedPopulation) {
			populateBookmarks();
			bookmarksNeedPopulation = false;
		}

		if(mc.player.getGameProfile().getName().equals("haighyorkie")) {
			GlStateManager.color4f(1F, 1F, 1F, 1F);
			mc.textureManager.bindTexture(texture);
			blit(left - 19, top + 42, 67, 180, 19, 26);
			if(xCoord >= left - 19 && xCoord < left && yCoord >= top + 62 && yCoord < top + 88) {
				mc.textureManager.bindTexture(textureToff);
				GlStateManager.pushMatrix();
				GlStateManager.scalef(0.5F, 0.5F, 0.5F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.translatef(0F, 0F, 2F);

				int w = 256;
				int h = 152;
				int x = (int) ((ClientTickHandler.ticksInGame + newPartialTicks) * 6) % (width + w) - w;
				int y = (int) (top + guiHeight / 2 - h / 4 + Math.sin((ClientTickHandler.ticksInGame + newPartialTicks) / 6.0) * 40);

				blit(x * 2, y * 2, 0, 0, w, h);
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();

				RenderHelper.renderTooltip(xCoord, yCoord, Arrays.asList(TextFormatting.GOLD + "#goldfishchris", TextFormatting.AQUA + "IT SAYS MANUAL"));
			}
		}

		super.render(xCoord, yCoord, newPartialTicks);

		if(hasTutorialArrow) {
			mc.textureManager.bindTexture(texture);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color4f(1F, 1F, 1F, 0.7F + (float) (Math.sin((ClientTickHandler.ticksInGame + newPartialTicks) * 0.3F) + 1) * 0.15F);
			blit(tutorialArrowX, tutorialArrowY, 20, 200, TUTORIAL_ARROW_WIDTH, TUTORIAL_ARROW_HEIGHT);
			GlStateManager.disableBlend();
		}
	}

	private void drawNotes(float part) {
		int size = 105;

		float time = notesMoveTime;
		if(notesMoveTime < NOTE_TWEEN_TIME && notesEnabled)
			time += part;
		else if(notesMoveTime > 0 && !notesEnabled)
			time -= part;

		int drawSize = (int) (size * time / NOTE_TWEEN_TIME);
		int x = left - drawSize;
		int y = top + 10;

		blit(x, y, 146, 0, drawSize, 125);

		String noteDisplay = note;
		if(notesEnabled && ClientTickHandler.ticksInGame % 20 < 10)
			noteDisplay += "&r_";

		font.drawString(I18n.format("botaniamisc.notes"), x + 5, y - 7, 0x666666);
		PageText.renderText(x + 5, y - 3, 92, 120, 0, true, 0, TextFormatting.RESET + noteDisplay);
	}

	public void drawBookmark(int x, int y, String s, boolean drawLeft) {
		drawBookmark(x, y, s, drawLeft, 180);
	}

	private void drawBookmark(int x, int y, String s, boolean drawLeft, int v) {
		drawBookmark(x, y, s, drawLeft, 0x111111, v);
	}

	public void drawBookmark(int x, int y, String s, boolean drawLeft, int color, int v) {
		// This function is called from the buttons so I can't use font
		FontRenderer font = Minecraft.getInstance().fontRenderer;
		int l = font.getStringWidth(s);
		int fontOff = 0;

		if(!drawLeft) {
			x += l / 2;
			fontOff = 2;
		}

		Minecraft.getInstance().textureManager.bindTexture(texture);

		GlStateManager.color4f(1F, 1F, 1F, 1F);
		blit(x + l / 2 + 3, y - 1, 54, v, 6, 11);

		if(drawLeft)
			blit(x - l / 2 - 9, y - 1, 61, v, 6, 11);
		for(int i = 0; i < l + 6; i++)
			blit(x - l / 2 - 3 + i, y - 1, 60, v, 1, 11);

		font.drawString(s, x - l / 2 + fontOff, y, color);
	}

	private void drawHeader() {
		GlStateManager.pushMatrix();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		Minecraft.getInstance().textureManager.bindTexture(texture);
		blit(left - 8, top + 9, 0, 224, 140, 31);

		int color = 0xffd200;
		font.drawString(title.getFormattedText(), left + 18, top + 13, color);
		font.drawString(I18n.format("botaniamisc.edition", ItemLexicon.getEdition()), left + 24, top + 22, color);

		String s = TextFormatting.BOLD + categoryHighlight;
		font.drawString(s, left + guiWidth / 2 - font.getStringWidth(s) / 2, top + 36, 0);

		GlStateManager.popMatrix();

		categoryHighlight = "";
	}

	boolean isMainPage() {
		return true;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	public int bookmarkWidth(String b) {
		if(font == null)
			font = Minecraft.getInstance().fontRenderer;

		return font.getStringWidth(b) + 15;
	}

	@Nonnull
	@Override
	public ITextComponent getTitle() {
		return ItemLexicon.getTitle(stackUsed);
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
		// Reference: AbstractOption.GUI_SCALE
		return Minecraft.getInstance().mainWindow.calcGuiScale(0, Minecraft.getInstance().getForceUnicodeFont());
	}

	void populateIndex() {
		List<LexiconCategory> categoryList = BotaniaAPI.getAllCategories();
		int shift = 2;
		for(int i = shift; i < 12; i++) {
			int i_ = i - shift;
			GuiButtonIndexEntry button = (GuiButtonIndexEntry) buttons.get(i);
			LexiconCategory category = i_ >= categoryList.size() ? null : categoryList.get(i_);
			if(category != null)
				button.setMessage(I18n.format(category.getUnlocalizedName()));
			else {
				button.setMessage(I18n.format("botaniamisc.lexiconIndex"));
				break;
			}
		}
	}

	private void populateBookmarks() {
		buttons.removeIf(b -> b instanceof GuiButtonBookmark
				|| b instanceof GuiButtonHistory
				|| b instanceof GuiButtonChallengeInfo);

		int len = bookmarks.size();
		boolean thisExists = false;
		for(GuiLexicon lex : bookmarks)
			if(lex.getTitle().equals(getTitle()))
				thisExists = true;

		boolean addEnabled = len < MAX_BOOKMARK_COUNT && this instanceof IParented && !thisExists;
		for(int i = 0; i < len + (addEnabled ? 1 : 0); i++) {
			boolean isAdd = i == bookmarks.size();
			GuiLexicon destination = isAdd ? null : bookmarks.get(i);
			buttons.add(new GuiButtonBookmark(i, left + 138, top + 18 + 14 * i, destination == null ? this : destination, destination == null ? "+" : destination.getTitle().getFormattedText()));
		}

		if(isMainPage())
			buttons.add(new GuiButtonHistory(left + 138, top + guiHeight - 24, I18n.format("botaniamisc.history"), this));
		else if(isChallenge())
			buttons.add(new GuiButtonChallengeInfo(left + 138, top + guiHeight - 24, I18n.format("botaniamisc.info"), this));
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

		for(Widget button : buttons)
			if(button instanceof GuiButtonCategory) {
				GuiButtonCategory catButton = (GuiButtonCategory) button;
				if(catButton.getCategory() == category) {
					orientTutorialArrowWithButton(catButton);
					break;
				}
			}
	}

	public void orientTutorialArrowWithButton(Button button) {
		tutorialArrowX = button.x - TUTORIAL_ARROW_WIDTH;
		tutorialArrowY = button.y - TUTORIAL_ARROW_HEIGHT;
	}

	boolean closeScreenOnInvKey() {
		return true;
	}

	@Override
	public boolean charTyped(char codePoint, int mods) {
		return handleNoteChar(codePoint) || super.charTyped(codePoint, mods);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int mods) {
		if(!notesEnabled && closeScreenOnInvKey() && mc.gameSettings.keyBindInventory.isActiveAndMatches(InputMappings.getInputByCode(keyCode, scanCode))) {
			mc.displayGuiScreen(null);
			mc.mouseHelper.grabMouse();
			return true;
		}

		if(konamiTime == 0 && keyCode == KONAMI_CODE[konamiIndex]) {
			konamiIndex++;
			if(konamiIndex >= KONAMI_CODE.length) {
				mc.getSoundHandler().play(SimpleSound.master(ModSounds.way, 1.0F));
				konamiIndex = 0;
				konamiTime = 240;
			}
			return true;
		} else konamiIndex = 0;

		return super.keyPressed(keyCode, scanCode, mods);
	}

	private boolean handleNoteChar(char codePoint) {
		if(notesEnabled) {
			mc.keyboardListener.enableRepeatEvents(true);
			boolean changed = false;

			if(codePoint == 8 && note.length() > 0) { // backspace
				if(hasControlDown())
					note = "";
				else {
					if(note.endsWith("<br>"))
						note = note.substring(0, note.length() - 4);
					else note = note.substring(0, note.length() - 1);
				}
				changed = true;
			}

			if((SharedConstants.isAllowedCharacter(codePoint) || codePoint == 0xA) && note.length() < 250) {
				note += codePoint == 0xA ? "<br>" : codePoint;
				changed = true;
			}

			if(changed) {
				notes.put(getNotesKey(), note);
				PersistentVariableHelper.saveSafe();
				return true;
			}
		} else mc.keyboardListener.enableRepeatEvents(false);
		return false;
	}

	public static GuiLexicon create(CompoundNBT cmp) {
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

	public void serialize(CompoundNBT cmp) {
		cmp.putString(TAG_TYPE, getClass().getName());
	}

	public String getNotesKey() {
		return "index";
	}

	public void load(CompoundNBT cmp) {}

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

