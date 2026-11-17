/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import vazkii.botania.api.block.IslandType;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.ItemSource;
import vazkii.botania.api.recipe.StateIngredientType;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaRegistries {
	public static final ResourceKey<Registry<Brew>> BREWS =
			ResourceKey.createRegistryKey(botaniaRL("brews"));
	public static final ResourceKey<Registry<IslandType>> ISLAND_TYPES =
			ResourceKey.createRegistryKey(botaniaRL("island_types"));

	/**
	 * The ID of Botania's Creative Tab
	 */
	public static final ResourceKey<CreativeModeTab> BOTANIA_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
			botaniaRL("botania"));

	public static final ResourceKey<Registry<StateIngredientType<?>>> STATE_INGREDIENT_TYPE =
			ResourceKey.createRegistryKey(botaniaRL("state_ingredient_type"));
	public static final ResourceKey<Registry<ItemSource>> ITEM_SOURCE =
			ResourceKey.createRegistryKey(botaniaRL("item_source"));
}
