/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 1, 2014, 9:15:15 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.item.ModItems;

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
	
	public static RecipeBrew warpWardBrew;

	public static void init() {
		speedBrew = BotaniaAPI.registerBrewRecipe(ModBrews.speed, new ItemStack(Items.nether_wart), new ItemStack(Items.sugar), new ItemStack(Items.redstone));
		strengthBrew = BotaniaAPI.registerBrewRecipe(ModBrews.strength, new ItemStack(Items.nether_wart), new ItemStack(Items.blaze_powder), new ItemStack(Items.glowstone_dust));
		hasteBrew = BotaniaAPI.registerBrewRecipe(ModBrews.haste, new ItemStack(Items.nether_wart), new ItemStack(Items.sugar), new ItemStack(Items.gold_nugget));
		healingBrew = BotaniaAPI.registerBrewRecipe(ModBrews.healing, new ItemStack(Items.nether_wart), new ItemStack(Items.speckled_melon), new ItemStack(Items.potato));
		jumpBoostBrew = BotaniaAPI.registerBrewRecipe(ModBrews.jumpBoost, new ItemStack(Items.nether_wart), new ItemStack(Items.feather), new ItemStack(Items.carrot));
		regenerationBrew = BotaniaAPI.registerBrewRecipe(ModBrews.regen, new ItemStack(Items.nether_wart), new ItemStack(Items.ghast_tear), new ItemStack(Items.glowstone_dust));
		weakRegenerationBrew = BotaniaAPI.registerBrewRecipe(ModBrews.regenWeak, new ItemStack(Items.nether_wart), new ItemStack(Items.ghast_tear), new ItemStack(Items.redstone));
		resistanceBrew = BotaniaAPI.registerBrewRecipe(ModBrews.resistance, new ItemStack(Items.nether_wart), new ItemStack(Items.iron_ingot), new ItemStack(Items.leather));
		fireResistanceBrew = BotaniaAPI.registerBrewRecipe(ModBrews.fireResistance, new ItemStack(Items.nether_wart), new ItemStack(Items.magma_cream), new ItemStack(Blocks.netherrack));
		waterBreathingBrew = BotaniaAPI.registerBrewRecipe(ModBrews.waterBreathing, new ItemStack(Items.nether_wart), new ItemStack(ModItems.manaResource, 1, 10), new ItemStack(Items.glowstone_dust));
		invisibilityBrew = BotaniaAPI.registerBrewRecipe(ModBrews.invisibility, new ItemStack(Items.nether_wart), new ItemStack(Items.snowball), new ItemStack(Items.glowstone_dust));
		nightVisionBrew = BotaniaAPI.registerBrewRecipe(ModBrews.nightVision, new ItemStack(Items.nether_wart), new ItemStack(Items.spider_eye), new ItemStack(Items.golden_carrot));
		absorptionBrew = BotaniaAPI.registerBrewRecipe(ModBrews.absorption, new ItemStack(Items.nether_wart), new ItemStack(Items.golden_apple), new ItemStack(Items.potato));
	}
	
	public static void initTC() {
		Item resource = (Item) Item.itemRegistry.getObject("Thaumcraft:ItemResource");
		Item bathSalts = (Item) Item.itemRegistry.getObject("Thaumcraft:ItemBathSalts");

		warpWardBrew = BotaniaAPI.registerBrewRecipe(ModBrews.warpWard, new ItemStack(Items.nether_wart), new ItemStack(resource, 1, 14), new ItemStack(bathSalts), new ItemStack(resource, 1, 6));
	}


}
