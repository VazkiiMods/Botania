/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

/**
 * Class for API calls that must be made clientside
 */
public interface BotaniaAPIClient {
	BotaniaAPIClient INSTANCE = ServiceUtil.findService(BotaniaAPIClient.class, () -> new BotaniaAPIClient() {});

	static BotaniaAPIClient instance() {
		return INSTANCE;
	}

	/**
	 * Draw a mana bar on the screen
	 */
	default void drawSimpleManaHUD(GuiGraphics gui, Window window, Font font, int color, int mana, int maxMana, String name) {}

	/**
	 * Performs the effects of {@link #drawSimpleManaHUD}, then renders {@code bindDisplay}, and a checkmark or x-mark
	 * depending on the value of {@code properlyBound}.
	 */
	default void drawComplexManaHUD(GuiGraphics gui, Window window, Font font, int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {}
}
