/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeCategory;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.SpecialFloatingFlowerWrapper;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeCategory;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeWrapper;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeCategory;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeWrapper;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeCategory;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeWrapper;
import vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeCategory;
import vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeWrapper;
import vazkii.botania.client.integration.jei.runicaltar.RunicAltarRecipeCategory;
import vazkii.botania.client.integration.jei.runicaltar.RunicAltarRecipeWrapper;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.recipe.SpecialFloatingFlowerRecipe;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIBotaniaPlugin implements IModPlugin {

	@Override
	public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
		subtypeRegistry.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.specialFlower), ItemBlockSpecialFlower::getType);
		subtypeRegistry.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.floatingSpecialFlower), ItemBlockSpecialFlower::getType);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(
				new BreweryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new PureDaisyRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new RunicAltarRecipeCategory(registry.getJeiHelpers().getGuiHelper()), // Runic must come before petals. See williewillus/Botania#172
				new PetalApothecaryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ElvenTradeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ManaPoolRecipeCategory(registry.getJeiHelpers().getGuiHelper())
				);
	}

	@Override
	public void register(@Nonnull IModRegistry registry) {
		registry.handleRecipes(RecipeBrew.class, BreweryRecipeWrapper::new, BreweryRecipeCategory.UID);
		registry.handleRecipes(RecipePureDaisy.class, PureDaisyRecipeWrapper::new, PureDaisyRecipeCategory.UID);
		registry.handleRecipes(RecipeRuneAltar.class, RunicAltarRecipeWrapper::new, RunicAltarRecipeCategory.UID); // Runic must come before petals. See williewillus/Botania#172
		registry.handleRecipes(RecipePetals.class, PetalApothecaryRecipeWrapper::new, PetalApothecaryRecipeCategory.UID);
		registry.handleRecipes(RecipeElvenTrade.class, ElvenTradeRecipeWrapper::new, ElvenTradeRecipeCategory.UID);
		registry.handleRecipes(RecipeManaInfusion.class, ManaPoolRecipeWrapper::new, ManaPoolRecipeCategory.UID);
		registry.handleRecipes(SpecialFloatingFlowerRecipe.class, SpecialFloatingFlowerWrapper::new, VanillaRecipeCategoryUid.CRAFTING);

		registry.addRecipes(BotaniaAPI.brewRecipes, BreweryRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.pureDaisyRecipes, PureDaisyRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.petalRecipes, PetalApothecaryRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.elvenTradeRecipes, ElvenTradeRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.runeAltarRecipes, RunicAltarRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.manaInfusionRecipes, ManaPoolRecipeCategory.UID);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.brewery), BreweryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.alfPortal), ElvenTradeRecipeCategory.UID);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.manaPool), ManaPoolRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.creativePool), ManaPoolRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.dilutedPool), ManaPoolRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.fabulousPool), ManaPoolRecipeCategory.UID);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.defaultAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.forestAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.plainsAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.mountainAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.fungalAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.swampAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.desertAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.taigaAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.mesaAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.mossyAltar), PetalApothecaryRecipeCategory.UID);

		registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType("puredaisy"), PureDaisyRecipeCategory.UID);
		registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), "puredaisy"), PureDaisyRecipeCategory.UID);


		registry.addRecipeCatalyst(new ItemStack(ModBlocks.runeAltar), RunicAltarRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModItems.autocraftingHalo), VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(ModItems.craftingHalo), VanillaRecipeCategoryUid.CRAFTING);

		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerCraftingHalo.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
	}

}
