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
import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.ingredients.IIngredientType;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.mana.IManaIngredient;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.integration.jei.mana.ManaIngredient;

import java.util.Collections;
import java.util.Map;

public class BotaniaAPIClientImpl implements BotaniaAPIClient {
	private final Map<IFloatingFlower.IslandType, ResourceLocation> islandTypeModels = Maps.newHashMap();

	@Override
	public void registerIslandTypeModel(IFloatingFlower.IslandType islandType, ResourceLocation model) {
		islandTypeModels.put(islandType, model);
	}

	@Override
	public Map<IFloatingFlower.IslandType, ResourceLocation> getRegisteredIslandTypeModels() {
		return Collections.unmodifiableMap(islandTypeModels);
	}

	@Override
	public void drawSimpleManaHUD(MatrixStack ms, int color, int mana, int maxMana, String name) {
		HUDHandler.drawSimpleManaHUD(ms, color, mana, maxMana, name);
	}

	@Override
	public void drawComplexManaHUD(MatrixStack ms, int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {
		HUDHandler.drawComplexManaHUD(color, ms, mana, maxMana, name, bindDisplay, properlyBound);
	}

	@Override
	public IManaIngredient createManaIngredient(int amount, boolean isCreative) {
		return new ManaIngredient(amount, isCreative);
	}

	@Override
	public IIngredientType<IManaIngredient> getManaIngredientType() {
		return ManaIngredient.TYPE;
	}

}
