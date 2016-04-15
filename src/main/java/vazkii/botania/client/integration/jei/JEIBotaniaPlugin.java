/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeCategory;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeHandler;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeCategory;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeHandler;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeCategory;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeHandler;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeCategory;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeHandler;
import vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeCategory;
import vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeHandler;
import vazkii.botania.client.integration.jei.runicaltar.RunicAltarRecipeCategory;
import vazkii.botania.client.integration.jei.runicaltar.RunicAltarRecipeHandler;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIBotaniaPlugin implements IModPlugin {

	@Override
	public void register(@Nonnull IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();

		registry.addRecipeCategories(
				new BreweryRecipeCategory(jeiHelpers.getGuiHelper()),
				new PureDaisyRecipeCategory(jeiHelpers.getGuiHelper()),
				new RunicAltarRecipeCategory(jeiHelpers.getGuiHelper()), // Runic must come before petals. See williewillus/Botania#172
				new PetalApothecaryRecipeCategory(jeiHelpers.getGuiHelper()),
				new ElvenTradeRecipeCategory(jeiHelpers.getGuiHelper()),
				new ManaPoolRecipeCategory(jeiHelpers.getGuiHelper())
		);

		registry.addRecipeHandlers(
				new BreweryRecipeHandler(),
				new PureDaisyRecipeHandler(),
				new RunicAltarRecipeHandler(), // Runic must come before petals. See williewillus/Botania#172
				new PetalApothecaryRecipeHandler(),
				new ElvenTradeRecipeHandler(),
				new ManaPoolRecipeHandler()
		);

		registry.addRecipes(BotaniaAPI.brewRecipes);
		registry.addRecipes(BotaniaAPI.pureDaisyRecipes);
		registry.addRecipes(BotaniaAPI.petalRecipes);
		registry.addRecipes(BotaniaAPI.elvenTradeRecipes);
		registry.addRecipes(BotaniaAPI.runeAltarRecipes);
		registry.addRecipes(BotaniaAPI.manaInfusionRecipes);
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {}

}
