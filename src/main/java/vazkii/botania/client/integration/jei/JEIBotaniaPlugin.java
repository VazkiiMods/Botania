/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.IRecipesGui;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.state.enums.AltarVariant;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeCategory;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.AncientWillRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.CompositeLensRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.SpecialFloatingFlowerWrapper;
import vazkii.botania.client.integration.jei.crafting.TerraPickTippingRecipeWrapper;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeCategory;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeWrapper;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeCategory;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeWrapper;
import vazkii.botania.client.integration.jei.orechid.OrechidIgnemRecipeCategory;
import vazkii.botania.client.integration.jei.orechid.OrechidIgnemRecipeWrapper;
import vazkii.botania.client.integration.jei.orechid.OrechidRecipeCategory;
import vazkii.botania.client.integration.jei.orechid.OrechidRecipeWrapper;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeCategory;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeWrapper;
import vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeCategory;
import vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeWrapper;
import vazkii.botania.client.integration.jei.runicaltar.RunicAltarRecipeCategory;
import vazkii.botania.client.integration.jei.runicaltar.RunicAltarRecipeWrapper;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;
import vazkii.botania.common.crafting.recipe.SpecialFloatingFlowerRecipe;
import vazkii.botania.common.crafting.recipe.TerraPickTippingRecipe;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.item.brew.ItemBrewBase;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.LibBlockNames.SUBTILE_ORECHID;
import static vazkii.botania.common.lib.LibBlockNames.SUBTILE_ORECHID_IGNEM;
import static vazkii.botania.common.lib.LibBlockNames.SUBTILE_PUREDAISY;

@JEIPlugin
public class JEIBotaniaPlugin implements IModPlugin {

	@Override
	public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
		subtypeRegistry.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.specialFlower), ItemBlockSpecialFlower::getType);
		subtypeRegistry.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.floatingSpecialFlower), ItemBlockSpecialFlower::getType);
		subtypeRegistry.registerSubtypeInterpreter(ModItems.brewVial, ItemBrewBase::getSubtype);
		subtypeRegistry.registerSubtypeInterpreter(ModItems.brewFlask, ItemBrewBase::getSubtype);
		subtypeRegistry.registerSubtypeInterpreter(ModItems.incenseStick, ItemBrewBase::getSubtype);
		subtypeRegistry.registerSubtypeInterpreter(ModItems.bloodPendant, ItemBrewBase::getSubtype);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(
				new BreweryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new PureDaisyRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new RunicAltarRecipeCategory(registry.getJeiHelpers().getGuiHelper()), // Runic must come before petals. See williewillus/Botania#172
				new PetalApothecaryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ElvenTradeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ManaPoolRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new OrechidRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new OrechidIgnemRecipeCategory(registry.getJeiHelpers().getGuiHelper())
		);
	}

	public static boolean doesOreExist(String key) {
		return OreDictionary.doesOreNameExist(key)
				&& OreDictionary.getOres(key).stream()
				.anyMatch(s -> s.getItem() instanceof ItemBlock);
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
		registry.handleRecipes(AncientWillRecipe.class, AncientWillRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
		registry.handleRecipes(TerraPickTippingRecipe.class, TerraPickTippingRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
		registry.handleRecipes(CompositeLensRecipe.class, r -> new CompositeLensRecipeWrapper(), VanillaRecipeCategoryUid.CRAFTING);
		
		registry.addRecipes(BotaniaAPI.brewRecipes, BreweryRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.pureDaisyRecipes, PureDaisyRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.petalRecipes, PetalApothecaryRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.elvenTradeRecipes, ElvenTradeRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.runeAltarRecipes, RunicAltarRecipeCategory.UID);
		registry.addRecipes(BotaniaAPI.manaInfusionRecipes, ManaPoolRecipeCategory.UID);

		registry.addRecipes(
				BotaniaAPI.oreWeights.entrySet().stream()
						.filter(e -> doesOreExist(e.getKey()))
						.map(OrechidRecipeWrapper::new)
						.sorted()
						.collect(Collectors.toList()),
				OrechidRecipeCategory.UID);

		registry.addRecipes(
				BotaniaAPI.oreWeightsNether.entrySet().stream()
						.filter(e -> doesOreExist(e.getKey()))
						.map(OrechidIgnemRecipeWrapper::new)
						.sorted()
						.collect(Collectors.toList()),
				OrechidIgnemRecipeCategory.UID);


		registry.addRecipeCatalyst(new ItemStack(ModBlocks.brewery), BreweryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.alfPortal), ElvenTradeRecipeCategory.UID);

		for(PoolVariant v : PoolVariant.values()) {
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.pool, 1, v.ordinal()), ManaPoolRecipeCategory.UID);
		}

		for(AltarVariant v : AltarVariant.values()) {
			if(v == AltarVariant.MOSSY) continue;
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.altar, 1, v.ordinal()), PetalApothecaryRecipeCategory.UID);
		}

		registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(SUBTILE_ORECHID), OrechidRecipeCategory.UID);
		registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), SUBTILE_ORECHID), OrechidRecipeCategory.UID);
		registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(SUBTILE_ORECHID_IGNEM), OrechidIgnemRecipeCategory.UID);
		registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), SUBTILE_ORECHID_IGNEM), OrechidIgnemRecipeCategory.UID);
		registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(SUBTILE_PUREDAISY), PureDaisyRecipeCategory.UID);
		registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), SUBTILE_PUREDAISY), PureDaisyRecipeCategory.UID);


		registry.addRecipeCatalyst(new ItemStack(ModBlocks.runeAltar), RunicAltarRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModItems.autocraftingHalo), VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(ModItems.craftingHalo), VanillaRecipeCategoryUid.CRAFTING);

		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerCraftingHalo.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		IRecipeRegistry recipeRegistry = jeiRuntime.getRecipeRegistry();
		for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
			List<Object> inputs = recipe.getInputs();
			List<ItemStack> outputs = recipe.getOutputs();
			if(inputs.size() == 1 && outputs.size() == 1 && inputs.get(0) instanceof ItemStack
					&& ItemStack.areItemStacksEqual((ItemStack) inputs.get(0), outputs.get(0))) {
				IRecipeWrapper wrapper = recipeRegistry.getRecipeWrapper(recipe, ElvenTradeRecipeCategory.UID);
				if(wrapper != null) {
					recipeRegistry.hideRecipe(wrapper, ElvenTradeRecipeCategory.UID);
				}
			}
		}
		
		CorporeaInputHandler.jeiPanelSupplier = () -> {
			Object o = jeiRuntime.getIngredientListOverlay().getIngredientUnderMouse();

			if(o == null && Minecraft.getMinecraft().currentScreen == jeiRuntime.getRecipesGui())
				o = jeiRuntime.getRecipesGui().getIngredientUnderMouse();

			if(o instanceof ItemStack)
				return (ItemStack) o;
			return ItemStack.EMPTY;
		};

		CorporeaInputHandler.supportedGuiFilter = gui -> gui instanceof GuiContainer || gui instanceof IRecipesGui;
	}

}
