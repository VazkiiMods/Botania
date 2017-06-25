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
import net.minecraft.util.ResourceLocation;
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
		speedBrew = BotaniaAPI.registerBrewRecipe(ModBrews.speed, new ItemStack(Items.NETHER_WART), new ItemStack(Items.SUGAR), new ItemStack(Items.REDSTONE));
		strengthBrew = BotaniaAPI.registerBrewRecipe(ModBrews.strength, new ItemStack(Items.NETHER_WART), new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.GLOWSTONE_DUST));
		hasteBrew = BotaniaAPI.registerBrewRecipe(ModBrews.haste, new ItemStack(Items.NETHER_WART), new ItemStack(Items.SUGAR), new ItemStack(Items.GOLD_NUGGET));
		healingBrew = BotaniaAPI.registerBrewRecipe(ModBrews.healing, new ItemStack(Items.NETHER_WART), new ItemStack(Items.SPECKLED_MELON), new ItemStack(Items.POTATO));
		jumpBoostBrew = BotaniaAPI.registerBrewRecipe(ModBrews.jumpBoost, new ItemStack(Items.NETHER_WART), new ItemStack(Items.FEATHER), new ItemStack(Items.CARROT));
		regenerationBrew = BotaniaAPI.registerBrewRecipe(ModBrews.regen, new ItemStack(Items.NETHER_WART), new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.GLOWSTONE_DUST));
		weakRegenerationBrew = BotaniaAPI.registerBrewRecipe(ModBrews.regenWeak, new ItemStack(Items.NETHER_WART), new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.REDSTONE));
		resistanceBrew = BotaniaAPI.registerBrewRecipe(ModBrews.resistance, new ItemStack(Items.NETHER_WART), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.LEATHER));
		fireResistanceBrew = BotaniaAPI.registerBrewRecipe(ModBrews.fireResistance, new ItemStack(Items.NETHER_WART), new ItemStack(Items.MAGMA_CREAM), new ItemStack(Blocks.NETHERRACK));
		waterBreathingBrew = BotaniaAPI.registerBrewRecipe(ModBrews.waterBreathing, new ItemStack(Items.NETHER_WART), "gemPrismarine", new ItemStack(Items.GLOWSTONE_DUST));
		invisibilityBrew = BotaniaAPI.registerBrewRecipe(ModBrews.invisibility, new ItemStack(Items.NETHER_WART), new ItemStack(Items.SNOWBALL), new ItemStack(Items.GLOWSTONE_DUST));
		nightVisionBrew = BotaniaAPI.registerBrewRecipe(ModBrews.nightVision, new ItemStack(Items.NETHER_WART), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.GOLDEN_CARROT));
		absorptionBrew = BotaniaAPI.registerBrewRecipe(ModBrews.absorption, new ItemStack(Items.NETHER_WART), new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.POTATO));

		overloadBrew = BotaniaAPI.registerBrewRecipe(ModBrews.overload, new ItemStack(Items.NETHER_WART), new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.SUGAR), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(ModItems.manaResource), new ItemStack(Items.SPIDER_EYE));
		soulCrossBrew = BotaniaAPI.registerBrewRecipe(ModBrews.soulCross, new ItemStack(Items.NETHER_WART), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Items.PAPER), new ItemStack(Items.APPLE), new ItemStack(Items.BONE));
		featherFeetBrew = BotaniaAPI.registerBrewRecipe(ModBrews.featherfeet, new ItemStack(Items.NETHER_WART), new ItemStack(Items.FEATHER), new ItemStack(Items.LEATHER), new ItemStack(Blocks.WOOL, 1, -1));
		emptinessBrew = BotaniaAPI.registerBrewRecipe(ModBrews.emptiness, new ItemStack(Items.NETHER_WART), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.BONE), new ItemStack(Items.STRING), new ItemStack(Items.ENDER_PEARL));
		bloodthirstBrew = BotaniaAPI.registerBrewRecipe(ModBrews.bloodthirst, new ItemStack(Items.NETHER_WART), new ItemStack(Items.FERMENTED_SPIDER_EYE), new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.FIRE_CHARGE), new ItemStack(Items.IRON_INGOT));
		allureBrew = BotaniaAPI.registerBrewRecipe(ModBrews.allure, new ItemStack(Items.NETHER_WART), new ItemStack(Items.FISH), new ItemStack(Items.QUARTZ), new ItemStack(Items.GOLDEN_CARROT));
		clearBrew = BotaniaAPI.registerBrewRecipe(ModBrews.clear, new ItemStack(Items.NETHER_WART), new ItemStack(Items.QUARTZ), new ItemStack(Items.EMERALD), new ItemStack(Items.MELON));
	}

	public static void initTC() {
		Item salisMundus = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "salis_mundus"));
		Item bathSalts = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "bath_salts"));
		Item amber = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "amber"));

		warpWardBrew = BotaniaAPI.registerBrewRecipe(ModBrews.warpWard, new ItemStack(Items.NETHER_WART), new ItemStack(salisMundus), new ItemStack(bathSalts), new ItemStack(amber));
	}


}
