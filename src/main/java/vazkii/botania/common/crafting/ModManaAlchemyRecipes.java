/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 2, 2014, 7:50:07 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public final class ModManaAlchemyRecipes {

	public static RecipeManaInfusion leatherRecipe;
	public static List<RecipeManaInfusion> woodRecipes;
	public static List<RecipeManaInfusion> saplingRecipes;
	public static RecipeManaInfusion glowstoneDustRecipe;
	public static List<RecipeManaInfusion> quartzRecipes;
	public static RecipeManaInfusion chiseledBrickRecipe;
	public static RecipeManaInfusion iceRecipe;
	public static List<RecipeManaInfusion> swampFolliageRecipes;
	public static List<RecipeManaInfusion> fishRecipes;
	public static List<RecipeManaInfusion> cropRecipes;
	public static RecipeManaInfusion potatoRecipe;
	public static RecipeManaInfusion netherWartRecipe;
	public static List<RecipeManaInfusion> gunpowderAndFlintRecipes;
	public static RecipeManaInfusion nameTagRecipe;
	public static List<RecipeManaInfusion> stringRecipes;
	public static List<RecipeManaInfusion> slimeballCactusRecipes;
	public static RecipeManaInfusion enderPearlRecipe;
	public static List<RecipeManaInfusion> redstoneToGlowstoneRecipes;
	public static RecipeManaInfusion sandRecipe;
	public static RecipeManaInfusion redSandRecipe;
	public static List<RecipeManaInfusion> clayBreakdownRecipes;
	public static RecipeManaInfusion coarseDirtRecipe;
	public static List<RecipeManaInfusion> stoneRecipes;
	public static List<RecipeManaInfusion> tallgrassRecipes;
	public static List<RecipeManaInfusion> flowersRecipes;
	public static RecipeManaInfusion chorusRecipe;

	// Garden of Glass
	public static List<RecipeManaInfusion> prismarineRecipes;

	public static void init() {
		leatherRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.LEATHER), new ItemStack(Items.ROTTEN_FLESH), 600);

		woodRecipes = new ArrayList<>();
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LOG, 1, 0), new ItemStack(Blocks.LOG2, 1, 1), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LOG, 1, 1), new ItemStack(Blocks.LOG, 1, 0), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LOG, 1, 2), new ItemStack(Blocks.LOG, 1, 1), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LOG, 1, 3), new ItemStack(Blocks.LOG, 1, 2), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LOG2, 1, 0), new ItemStack(Blocks.LOG, 1, 3), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LOG2, 1, 1), new ItemStack(Blocks.LOG2, 1, 0), 40));

		saplingRecipes = new ArrayList<>();
		for(int i = 0; i < 6; i++)
			saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.SAPLING, 1, i == 5 ? 0 : i + 1), new ItemStack(Blocks.SAPLING, 1, i), 120));

		glowstoneDustRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.GLOWSTONE_DUST, 4), new ItemStack(Blocks.GLOWSTONE), 25);
		quartzRecipes = new ArrayList<>();
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.QUARTZ, 4), new ItemStack(Blocks.QUARTZ_BLOCK, 1, Short.MAX_VALUE), 25));
		if(ConfigHandler.darkQuartzEnabled)
			quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 0), new ItemStack(ModFluffBlocks.darkQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 1), new ItemStack(ModFluffBlocks.manaQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 2), new ItemStack(ModFluffBlocks.blazeQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 3), new ItemStack(ModFluffBlocks.lavenderQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 4), new ItemStack(ModFluffBlocks.redQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 5), new ItemStack(ModFluffBlocks.elfQuartz, 1, Short.MAX_VALUE), 25));

		chiseledBrickRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.STONEBRICK, 1, 3), new ItemStack(Blocks.STONEBRICK), 150);
		iceRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ICE), new ItemStack(Blocks.SNOW), 2250);

		swampFolliageRecipes = new ArrayList<>();
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.WATERLILY), new ItemStack(Blocks.VINE), 320));
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.VINE), new ItemStack(Blocks.WATERLILY), 320));

		fishRecipes = new ArrayList<>();
		for(int i = 0; i < 4; i++)
			fishRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.FISH, 1, i == 3 ? 0 : i + 1), new ItemStack(Items.FISH, 1, i), 200));

		cropRecipes = new ArrayList<>();
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.DYE, 1, 3), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.POTATO), new ItemStack(Items.WHEAT), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.CARROT), new ItemStack(Items.POTATO), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.BEETROOT_SEEDS), new ItemStack(Items.CARROT), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.MELON_SEEDS), new ItemStack(Items.BEETROOT_SEEDS), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Items.MELON_SEEDS), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.DYE, 1, 3), new ItemStack(Items.PUMPKIN_SEEDS), 6000));

		potatoRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.POTATO), new ItemStack(Items.POISONOUS_POTATO), 1200);
		netherWartRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.NETHER_WART), new ItemStack(Items.BLAZE_ROD), 4000);

		gunpowderAndFlintRecipes = new ArrayList<>();
		gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.FLINT), new ItemStack(Items.GUNPOWDER), 200));
		gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.GUNPOWDER), new ItemStack(Items.FLINT), 200));

		nameTagRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.NAME_TAG), new ItemStack(Items.WRITABLE_BOOK), 16000);

		stringRecipes = new ArrayList<>();
		for(int i = 0; i < 16; i++)
			stringRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.STRING, 3), new ItemStack(Blocks.WOOL, 1, i), 100));

		slimeballCactusRecipes = new ArrayList<>();
		slimeballCactusRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.SLIME_BALL), new ItemStack(Blocks.CACTUS), 1200));
		slimeballCactusRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.CACTUS), new ItemStack(Items.SLIME_BALL), 1200));

		enderPearlRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.GHAST_TEAR), 28000);

		redstoneToGlowstoneRecipes = new ArrayList<>();
		redstoneToGlowstoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.REDSTONE), new ItemStack(Items.GLOWSTONE_DUST), 300));
		redstoneToGlowstoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.REDSTONE), 300));

		sandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Block.getBlockFromName("sand")), new ItemStack(Blocks.COBBLESTONE), 50);
		redSandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Block.getBlockFromName("sand"), 1, 1), new ItemStack(Blocks.HARDENED_CLAY), 50);

		clayBreakdownRecipes = new ArrayList<>();
		clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.CLAY_BALL, 4), new ItemStack(Blocks.CLAY), 25));
		clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.BRICK, 4), new ItemStack(Blocks.BRICK_BLOCK), 25));

		coarseDirtRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DIRT, 1, 1), new ItemStack(Blocks.DIRT), 120);

		stoneRecipes = new ArrayList<>();
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.STONE, 1, 5), new ItemStack(Blocks.STONE), 200)); // Stone->Andesite
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.STONE, 1, 3), new ItemStack(Blocks.STONE, 1, 5), 200)); // Andesite->Diorite
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.STONE, 1, 1), new ItemStack(Blocks.STONE, 1, 3), 200)); // Diorite->Granite
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.STONE, 1, 5), new ItemStack(Blocks.STONE, 1, 1), 200)); // Granite->Andesite

		tallgrassRecipes = new ArrayList<>();
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DEADBUSH), new ItemStack(Blocks.TALLGRASS, 1, 2), 500));
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.TALLGRASS, 1, 1), new ItemStack(Blocks.DEADBUSH), 500));
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.TALLGRASS, 1, 2), new ItemStack(Blocks.TALLGRASS, 1, 1), 500));

		flowersRecipes = new ArrayList<>();
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_FLOWER), new ItemStack(Blocks.YELLOW_FLOWER), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 1), new ItemStack(Blocks.RED_FLOWER), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 2), new ItemStack(Blocks.RED_FLOWER, 1, 1), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 3), new ItemStack(Blocks.RED_FLOWER, 1, 2), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 4), new ItemStack(Blocks.RED_FLOWER, 1, 3), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 5), new ItemStack(Blocks.RED_FLOWER, 1, 4), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 6), new ItemStack(Blocks.RED_FLOWER, 1, 5), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 7), new ItemStack(Blocks.RED_FLOWER, 1, 6), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 8), new ItemStack(Blocks.RED_FLOWER, 1, 7), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DOUBLE_PLANT), new ItemStack(Blocks.RED_FLOWER, 1, 8), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1), new ItemStack(Blocks.DOUBLE_PLANT), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), new ItemStack(Blocks.DOUBLE_PLANT, 1, 1), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 5), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.YELLOW_FLOWER), new ItemStack(Blocks.DOUBLE_PLANT, 1, 5), 400));

		chorusRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.CHORUS_FLOWER), new ItemStack(Items.CHORUS_FRUIT_POPPED), 10000);
		
		if(Botania.gardenOfGlassLoaded) {
			prismarineRecipes = new ArrayList<>();
			prismarineRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Items.QUARTZ), 1000));
			prismarineRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.PRISMARINE_CRYSTALS), new ItemStack(Items.PRISMARINE_SHARD), 500));
		}
	}
}
