/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.block.IslandType;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.recipe.StateIngredientType;

@EventBusSubscriber(modid = BotaniaAPI.MODID)
public class NeoForgeRegistryHandler {
	@SubscribeEvent
	public static void registerRegistry(NewRegistryEvent evt) {
		evt.create(new RegistryBuilder<>(BotaniaRegistries.BREWS)
				.defaultKey(Brew.DEFAULT_ID).sync(true));
		evt.create(new RegistryBuilder<>(BotaniaRegistries.ISLAND_TYPES)
				.defaultKey(IslandType.DEFAULT_ID).sync(true));
		evt.create(new RegistryBuilder<>(BotaniaRegistries.STATE_INGREDIENT_TYPE)
				.defaultKey(StateIngredientType.DEFAULT_ID).sync(true));
		evt.create(new RegistryBuilder<>(BotaniaRegistries.ITEM_SOURCE));
	}
}
