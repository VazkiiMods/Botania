/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 9:15:15 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class ModBrewRecipes {

	public static RecipeBrew speedBrew;
	public static RecipeBrew strengthBrew;
	public static RecipeBrew hasteBrew;
	public static RecipeBrew healingBrew;
	public static RecipeBrew jumpBoostBrew;
	public static RecipeBrew regenerationBrew;
	public static RecipeBrew weakRegenerationBrew;
	public static RecipeBrew resistanceBrew;
	public static RecipeBrew fireResistanceBrew;
	public static RecipeBrew waterBreathingBrew;
	public static RecipeBrew invisibilityBrew;
	public static RecipeBrew nightVisionBrew;
	public static RecipeBrew absorptionBrew;

	public static RecipeBrew overloadBrew;
	public static RecipeBrew soulCrossBrew;
	public static RecipeBrew featherFeetBrew;
	public static RecipeBrew emptinessBrew;
	public static RecipeBrew bloodthirstBrew;
	public static RecipeBrew allureBrew;
	public static RecipeBrew clearBrew;

	public static RecipeBrew warpWardBrew;

	public static void init() {
		speedBrew = BotaniaAPI.registerBrewRecipe(ModBrews.speed, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.REDSTONE));
		strengthBrew = BotaniaAPI.registerBrewRecipe(ModBrews.strength, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.BLAZE_POWDER), Ingredient.fromItems(Items.GLOWSTONE_DUST));
		hasteBrew = BotaniaAPI.registerBrewRecipe(ModBrews.haste, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.GOLD_NUGGET));
		healingBrew = BotaniaAPI.registerBrewRecipe(ModBrews.healing, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GLISTERING_MELON_SLICE), Ingredient.fromItems(Items.POTATO));
		jumpBoostBrew = BotaniaAPI.registerBrewRecipe(ModBrews.jumpBoost, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.FEATHER), Ingredient.fromItems(Items.CARROT));
		regenerationBrew = BotaniaAPI.registerBrewRecipe(ModBrews.regen, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GHAST_TEAR), Ingredient.fromItems(Items.GLOWSTONE_DUST));
		weakRegenerationBrew = BotaniaAPI.registerBrewRecipe(ModBrews.regenWeak, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GHAST_TEAR), Ingredient.fromItems(Items.REDSTONE));
		resistanceBrew = BotaniaAPI.registerBrewRecipe(ModBrews.resistance, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.IRON_INGOT), Ingredient.fromItems(Items.LEATHER));
		fireResistanceBrew = BotaniaAPI.registerBrewRecipe(ModBrews.fireResistance, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.MAGMA_CREAM), Ingredient.fromItems(Blocks.NETHERRACK));
		waterBreathingBrew = BotaniaAPI.registerBrewRecipe(ModBrews.waterBreathing, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.PRISMARINE_CRYSTALS), Ingredient.fromItems(Items.GLOWSTONE_DUST));
		invisibilityBrew = BotaniaAPI.registerBrewRecipe(ModBrews.invisibility, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SNOWBALL), Ingredient.fromItems(Items.GLOWSTONE_DUST));
		nightVisionBrew = BotaniaAPI.registerBrewRecipe(ModBrews.nightVision, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SPIDER_EYE), Ingredient.fromItems(Items.GOLDEN_CARROT));
		absorptionBrew = BotaniaAPI.registerBrewRecipe(ModBrews.absorption, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GOLDEN_APPLE), Ingredient.fromItems(Items.POTATO));

		overloadBrew = BotaniaAPI.registerBrewRecipe(ModBrews.overload, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.BLAZE_POWDER), Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.GLOWSTONE_DUST), Ingredient.fromItems(ModItems.manaSteel), Ingredient.fromItems(Items.SPIDER_EYE));
		soulCrossBrew = BotaniaAPI.registerBrewRecipe(ModBrews.soulCross, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Blocks.SOUL_SAND), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.APPLE), Ingredient.fromItems(Items.BONE));
		featherFeetBrew = BotaniaAPI.registerBrewRecipe(ModBrews.featherfeet, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.FEATHER), Ingredient.fromItems(Items.LEATHER), Ingredient.fromTag(ItemTags.WOOL));
		emptinessBrew = BotaniaAPI.registerBrewRecipe(ModBrews.emptiness, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GUNPOWDER), Ingredient.fromItems(Items.ROTTEN_FLESH), Ingredient.fromItems(Items.BONE), Ingredient.fromItems(Items.STRING), Ingredient.fromItems(Items.ENDER_PEARL));
		bloodthirstBrew = BotaniaAPI.registerBrewRecipe(ModBrews.bloodthirst, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE), Ingredient.fromItems(Items.LAPIS_LAZULI), Ingredient.fromItems(Items.FIRE_CHARGE), Ingredient.fromItems(Items.IRON_INGOT));
		allureBrew = BotaniaAPI.registerBrewRecipe(ModBrews.allure, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.COD), Ingredient.fromItems(Items.QUARTZ), Ingredient.fromItems(Items.GOLDEN_CARROT));
		clearBrew = BotaniaAPI.registerBrewRecipe(ModBrews.clear, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.QUARTZ), Ingredient.fromItems(Items.EMERALD), Ingredient.fromItems(Items.MELON_SLICE));
	}

	public static void initTC() {
		Item salisMundus = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "salis_mundus"));
		Item bathSalts = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "bath_salts"));
		Item amber = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "amber"));

		warpWardBrew = BotaniaAPI.registerBrewRecipe(ModBrews.warpWard, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(salisMundus), Ingredient.fromItems(bathSalts), Ingredient.fromItems(amber));
	}


}
