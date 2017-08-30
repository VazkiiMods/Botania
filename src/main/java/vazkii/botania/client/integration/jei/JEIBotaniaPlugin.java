/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.state.enums.AltarVariant;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
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
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

@JEIPlugin
public class JEIBotaniaPlugin implements IModPlugin {

	@Override
	public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
		subtypeRegistry.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.specialFlower), ItemBlockSpecialFlower::getType);
		subtypeRegistry.registerNbtInterpreter(Item.getItemFromBlock(ModBlocks.floatingSpecialFlower), ItemBlockSpecialFlower::getType);
	}

	@Override
	public void registerIngredients(@Nonnull IModIngredientRegistration registry) {}

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

		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.brewery), BreweryRecipeCategory.UID);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.alfPortal), ElvenTradeRecipeCategory.UID);

		for(PoolVariant v : PoolVariant.values()) {
			registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.pool, 1, v.ordinal()), ManaPoolRecipeCategory.UID);
		}

		for(AltarVariant v : AltarVariant.values()) {
			if(v == AltarVariant.MOSSY) continue;
			registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.altar, 1, v.ordinal()), PetalApothecaryRecipeCategory.UID);
		}

		registry.addRecipeCategoryCraftingItem(ItemBlockSpecialFlower.ofType("puredaisy"), PureDaisyRecipeCategory.UID);
		registry.addRecipeCategoryCraftingItem(ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), "puredaisy"), PureDaisyRecipeCategory.UID);


		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.runeAltar), RunicAltarRecipeCategory.UID);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModItems.autocraftingHalo), VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModItems.craftingHalo), VanillaRecipeCategoryUid.CRAFTING);

		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerCraftingHalo.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {}

}
