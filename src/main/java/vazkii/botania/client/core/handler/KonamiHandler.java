/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.lwjgl.glfw.GLFW;

import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.lib.LibMisc;
import vazkii.patchouli.api.BookDrawScreenEvent;
import vazkii.patchouli.client.book.gui.GuiBook;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT)
public class KonamiHandler {
	private static final int[] KONAMI_CODE = {
			GLFW.GLFW_KEY_UP, GLFW.GLFW_KEY_UP,
			GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_DOWN,
			GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT,
			GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT,
			GLFW.GLFW_KEY_B, GLFW.GLFW_KEY_A,
	};
	private static int nextLetter = 0;
	private static int konamiTime = 0;

	private static boolean isBookOpen() {
		Minecraft mc = Minecraft.getInstance();
		return mc.currentScreen instanceof GuiBook && ((GuiBook) mc.currentScreen).book == ItemLexicon.getBook();
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent evt) {
		if (konamiTime > 0) {
			konamiTime--;
		}

		if (!isBookOpen()) {
			nextLetter = 0;
		}
	}

	@SubscribeEvent
	public static void handleInput(InputEvent.KeyInputEvent evt) {
		Minecraft mc = Minecraft.getInstance();
		if (evt.getModifiers() == 0 && evt.getAction() == GLFW.GLFW_PRESS && isBookOpen()) {
			if (konamiTime == 0 && evt.getKey() == KONAMI_CODE[nextLetter]) {
				nextLetter++;
				if (nextLetter >= KONAMI_CODE.length) {
					mc.getSoundHandler().play(SimpleSound.master(ModSounds.way, 1.0F));
					nextLetter = 0;
					konamiTime = 240;
				}
			} else {
				nextLetter = 0;
			}
		}
	}

	@SubscribeEvent
	public static void renderBook(BookDrawScreenEvent evt) {
		if (konamiTime > 0) {
			String meme = I18n.format("botania.subtitle.way");
			GlStateManager.disableDepthTest();
			GlStateManager.pushMatrix();
			int fullWidth = Minecraft.getInstance().fontRenderer.getStringWidth(meme);
			int left = evt.gui.width;
			double widthPerTick = (fullWidth + evt.gui.width) / 240;
			double currWidth = left - widthPerTick * (240 - (konamiTime - evt.partialTicks)) * 3.2;

			GlStateManager.translated(currWidth, evt.gui.height / 2 - 10, 0);
			GlStateManager.scalef(4, 4, 4);
			Minecraft.getInstance().fontRenderer.drawStringWithShadow(meme, 0, 0, 0xFFFFFF);
			GlStateManager.popMatrix();
			GlStateManager.enableDepthTest();
		}
	}
}
