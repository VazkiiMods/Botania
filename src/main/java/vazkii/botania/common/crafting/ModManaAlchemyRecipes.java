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
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.SPRUCE_LOG), new ItemStack(Blocks.OAK_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.BIRCH_LOG), new ItemStack(Blocks.SPRUCE_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.JUNGLE_LOG), new ItemStack(Blocks.BIRCH_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ACACIA_LOG), new ItemStack(Blocks.JUNGLE_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DARK_OAK_LOG), new ItemStack(Blocks.ACACIA_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.OAK_LOG), new ItemStack(Blocks.DARK_OAK_LOG), 40));

		saplingRecipes = new ArrayList<>();
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.SPRUCE_SAPLING), new ItemStack(Blocks.OAK_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.BIRCH_SAPLING), new ItemStack(Blocks.SPRUCE_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.JUNGLE_SAPLING), new ItemStack(Blocks.BIRCH_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ACACIA_SAPLING), new ItemStack(Blocks.JUNGLE_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DARK_OAK_SAPLING), new ItemStack(Blocks.ACACIA_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.OAK_SAPLING), new ItemStack(Blocks.DARK_OAK_SAPLING), 120));

		glowstoneDustRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.GLOWSTONE_DUST, 4), new ItemStack(Blocks.GLOWSTONE), 25);
		quartzRecipes = new ArrayList<>();
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.QUARTZ, 4), new ItemStack(Blocks.QUARTZ_BLOCK), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.darkQuartz, 4), new ItemStack(ModFluffBlocks.darkQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.manaQuartz, 4), new ItemStack(ModFluffBlocks.manaQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.blazeQuartz, 4), new ItemStack(ModFluffBlocks.blazeQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.lavenderQuartz, 4), new ItemStack(ModFluffBlocks.lavenderQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.redQuartz, 4), new ItemStack(ModFluffBlocks.redQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.elfQuartz, 4), new ItemStack(ModFluffBlocks.elfQuartz), 25));

		chiseledBrickRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.CHISELED_STONE_BRICKS, 1), new ItemStack(Blocks.STONE_BRICKS), 150);
		iceRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ICE), new ItemStack(Blocks.SNOW), 2250);

		swampFolliageRecipes = new ArrayList<>();
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LILY_PAD), new ItemStack(Blocks.VINE), 320));
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.VINE), new ItemStack(Blocks.LILY_PAD), 320));

		fishRecipes = new ArrayList<>();
		for(int i = 0; i < 4; i++)
			fishRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.FISH, 1, i == 3 ? 0 : i + 1), new ItemStack(Items.FISH, 1, i), 200));

		cropRecipes = new ArrayList<>();
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.COCOA_BEANS), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.POTATO), new ItemStack(Items.WHEAT), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.CARROT), new ItemStack(Items.POTATO), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.BEETROOT_SEEDS), new ItemStack(Items.CARROT), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.MELON_SEEDS), new ItemStack(Items.BEETROOT_SEEDS), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Items.MELON_SEEDS), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.COCOA_BEANS), new ItemStack(Items.PUMPKIN_SEEDS), 6000));

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

		sandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.SAND), new ItemStack(Blocks.COBBLESTONE), 50);
		redSandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_SAND), new ItemStack(Blocks.TERRACOTTA), 50);

		clayBreakdownRecipes = new ArrayList<>();
		clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.CLAY_BALL, 4), new ItemStack(Blocks.CLAY), 25));
		clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.BRICK, 4), new ItemStack(Blocks.BRICKS), 25));

		coarseDirtRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.COARSE_DIRT), new ItemStack(Blocks.DIRT), 120);

		stoneRecipes = new ArrayList<>();
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ANDESITE), new ItemStack(Blocks.STONE), 200));
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DIORITE), new ItemStack(Blocks.ANDESITE), 200));
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.GRANITE), new ItemStack(Blocks.DIORITE), 200));
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ANDESITE), new ItemStack(Blocks.GRANITE), 200));

		tallgrassRecipes = new ArrayList<>();
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DEAD_BUSH), new ItemStack(Blocks.FERN), 500));
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.GRASS), new ItemStack(Blocks.DEAD_BUSH), 500));
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.FERN), new ItemStack(Blocks.GRASS), 500));

		flowersRecipes = new ArrayList<>();
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.POPPY), new ItemStack(Blocks.DANDELION), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.BLUE_ORCHID), new ItemStack(Blocks.POPPY), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ALLIUM), new ItemStack(Blocks.BLUE_ORCHID), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.AZURE_BLUET), new ItemStack(Blocks.ALLIUM), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_TULIP), new ItemStack(Blocks.AZURE_BLUET), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ORANGE_TULIP), new ItemStack(Blocks.RED_TULIP), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.WHITE_TULIP), new ItemStack(Blocks.ORANGE_TULIP), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.PINK_TULIP), new ItemStack(Blocks.WHITE_TULIP), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.OXEYE_DAISY), new ItemStack(Blocks.PINK_TULIP), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.SUNFLOWER), new ItemStack(Blocks.OXEYE_DAISY), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LILAC), new ItemStack(Blocks.SUNFLOWER), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ROSE_BUSH), new ItemStack(Blocks.LILAC), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.PEONY), new ItemStack(Blocks.ROSE_BUSH), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DANDELION), new ItemStack(Blocks.PEONY), 400));

		chorusRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.CHORUS_FLOWER), new ItemStack(Items.POPPED_CHORUS_FRUIT), 10000);
		
		if(Botania.gardenOfGlassLoaded) {
			prismarineRecipes = new ArrayList<>();
			prismarineRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Items.QUARTZ), 1000));
			prismarineRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.PRISMARINE_CRYSTALS), new ItemStack(Items.PRISMARINE_SHARD), 500));
		}
	}
}
