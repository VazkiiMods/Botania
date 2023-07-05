/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.impl;

import com.google.common.collect.Maps;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.FloatingFlower;
import vazkii.botania.client.gui.HUDHandler;

import java.util.Collections;
import java.util.Map;

public class BotaniaAPIClientImpl implements BotaniaAPIClient {
	private final Map<FloatingFlower.IslandType, ResourceLocation> islandTypeModels = Maps.newHashMap();

	@Override
	public void registerIslandTypeModel(FloatingFlower.IslandType islandType, ResourceLocation model) {
		islandTypeModels.put(islandType, model);
	}

	@Override
	public Map<FloatingFlower.IslandType, ResourceLocation> getRegisteredIslandTypeModels() {
		return Collections.unmodifiableMap(islandTypeModels);
	}

	@Override
	public void drawSimpleManaHUD(GuiGraphics gui, int color, int mana, int maxMana, String name) {
		HUDHandler.drawSimpleManaHUD(gui, color, mana, maxMana, name);
	}

	@Override
	public void drawComplexManaHUD(GuiGraphics gui, int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {
		HUDHandler.drawComplexManaHUD(color, gui, mana, maxMana, name, bindDisplay, properlyBound);
	}
}
