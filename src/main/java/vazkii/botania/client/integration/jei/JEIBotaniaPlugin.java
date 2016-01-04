/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 3, 2016, 2:32:39 AM (GMT)]
 */

package vazkii.botania.client.integration.jei;

import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeCategory;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeHandler;

@JEIPlugin
public class JEIBotaniaPlugin implements IModPlugin {

    private IJeiHelpers jeiHelpers;
    private IItemRegistry itemRegistry;

    @Override
    public boolean isModLoaded() {
        return true;
    }

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCategories(
                new BreweryRecipeCategory(jeiHelpers.getGuiHelper())
        );

        registry.addRecipeHandlers(
                new BreweryRecipeHandler()
        );

        registry.addRecipes(BotaniaAPI.brewRecipes);
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

    }

}
