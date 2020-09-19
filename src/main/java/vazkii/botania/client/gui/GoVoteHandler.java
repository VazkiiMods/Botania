/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import org.lwjgl.glfw.GLFW;

import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT)
public class GoVoteHandler {
	private static final String LINK = "https://vote.gov/";
	private static boolean shownThisSession = false;

	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (shouldShow(mc)) {
			Screen curr = mc.currentScreen;

			if (curr instanceof WorldSelectionScreen || curr instanceof MultiplayerScreen) {
				mc.displayGuiScreen(new GoVoteScreen(curr));
				shownThisSession = true;
			}
		}
	}

	private static boolean shouldShow(Minecraft mc) {
		if (!isEnglish(mc) || shownThisSession) {
			return false;
		}

		if (PersistentVariableHelper.seenGoVoteScreen) {
			return false;
		}

		LocalDate electionDay = LocalDate.of(2020, Month.NOVEMBER, 3);
		if (LocalDate.now().isAfter(electionDay)) {
			return false;
		}

		return true;
	}

	private static boolean isEnglish(Minecraft mc) {
		boolean englishLang = mc.getLanguageManager() != null
				&& mc.getLanguageManager().getCurrentLanguage() != null
				&& "English".equals(mc.getLanguageManager().getCurrentLanguage().getName());
		boolean isUsLocale = "US".equals(Locale.getDefault().getCountry());
		return englishLang && isUsLocale;
	}

	private static class GoVoteScreen extends Screen {

		final Screen parent;
		int ticksElapsed = 0;
		boolean openedWebsite = false;
		private final List<List<ITextComponent>> message = new ArrayList<>();

		protected GoVoteScreen(Screen parent) {
			super(new StringTextComponent(""));
			this.parent = parent;
			addGroup(
					s("Note: If you can't vote in the United States"),
					s("Please press ESC and carry on.")
			);
			addGroup(StringTextComponent.EMPTY);
			addGroup(s("We are at a unique crossroads in the history of our country."));
			addGroup(s("In this time of heightened polarization,"),
					s("breakdown of political decorum, and fear,"));
			addGroup(s("it is tempting to succumb to apathy,"),
					s("to think that nothing you do will matter."));
			addGroup(StringTextComponent.EMPTY);
			addGroup(s("But the power is still in the hands of We, the People."));
			addGroup(s("The Constitution and its amendments grant every citizen the right to vote."));
			addGroup(s("And it is not only our right, but our ")
					.append(s("responsibility").mergeStyle(TextFormatting.ITALIC, TextFormatting.GOLD))
					.appendString(" to do so."));
			addGroup(s("Your vote matters. Always."));
			addGroup(StringTextComponent.EMPTY);
			addGroup(s("Click anywhere to check if you are registered to vote."));
			addGroup(s("The website is an official government site, unaffiliated with " + LibMisc.MOD_NAME + "."));
			addGroup(s("Press ESC to exit. (This screen will not show up again.)"));
		}

		// Each group appears at the same time
		private void addGroup(ITextComponent... lines) {
			message.add(Arrays.asList(lines));
		}

		private static StringTextComponent s(String txt) {
			return new StringTextComponent(txt);
		}

		@Override
		public void render(MatrixStack mstack, int mx, int my, float pticks) {
			super.render(mstack, mx, my, pticks);

			fill(mstack, 0, 0, width, height, 0xFF696969);
			int middle = width / 2;

			int dist = 12;

			int y = 10;
			for (int groupIdx = 0; groupIdx < message.size(); groupIdx++) {
				List<ITextComponent> group = message.get(groupIdx);
				if ((ticksElapsed - 20) > groupIdx * 50) {
					for (ITextComponent line : group) {
						drawCenteredString(mstack, font, line, middle, y, 0xFFFFFF);
						y += dist;
					}
				}
			}
		}

		private void leave() {
			PersistentVariableHelper.seenGoVoteScreen = true;
			PersistentVariableHelper.save();
			minecraft.displayGuiScreen(parent);
		}

		@Nonnull
		@Override
		public String getNarrationMessage() {
			StringBuilder builder = new StringBuilder();
			for (List<ITextComponent> group : message) {
				for (ITextComponent line : group) {
					builder.append(line.getString());
				}
			}
			return builder.toString();
		}

		@Override
		public boolean keyPressed(int keycode, int scanCode, int modifiers) {
			if (keycode == GLFW.GLFW_KEY_ESCAPE) {
				leave();
			}

			return super.keyPressed(keycode, scanCode, modifiers);
		}

		@Override
		public boolean mouseClicked(double x, double y, int modifiers) {
			if (ticksElapsed < 200) {
				return false;
			}

			if (modifiers == 0 && !openedWebsite) {
				minecraft.displayGuiScreen(new ConfirmOpenLinkScreen(this::consume, LINK, true));
				return true;
			}

			return super.mouseClicked(x, y, modifiers);
		}

		private void consume(boolean doIt) {
			minecraft.displayGuiScreen(this);
			if (doIt) {
				Util.getOSType().openURI(LINK);
			}
			openedWebsite = doIt;
		}

	}

}
