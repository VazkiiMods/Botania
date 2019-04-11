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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
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
		leatherRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.LEATHER), Ingredient.fromItems(Items.ROTTEN_FLESH), 600);

		woodRecipes = new ArrayList<>();
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.SPRUCE_LOG), Ingredient.fromItems(Blocks.OAK_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.BIRCH_LOG), Ingredient.fromItems(Blocks.SPRUCE_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.JUNGLE_LOG), Ingredient.fromItems(Blocks.BIRCH_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ACACIA_LOG), Ingredient.fromItems(Blocks.JUNGLE_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DARK_OAK_LOG), Ingredient.fromItems(Blocks.ACACIA_LOG), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.OAK_LOG), Ingredient.fromItems(Blocks.DARK_OAK_LOG), 40));

		saplingRecipes = new ArrayList<>();
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.SPRUCE_SAPLING), Ingredient.fromItems(Blocks.OAK_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.BIRCH_SAPLING), Ingredient.fromItems(Blocks.SPRUCE_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.JUNGLE_SAPLING), Ingredient.fromItems(Blocks.BIRCH_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ACACIA_SAPLING), Ingredient.fromItems(Blocks.JUNGLE_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DARK_OAK_SAPLING), Ingredient.fromItems(Blocks.ACACIA_SAPLING), 120));
		saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.OAK_SAPLING), Ingredient.fromItems(Blocks.DARK_OAK_SAPLING), 120));

		glowstoneDustRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.GLOWSTONE_DUST, 4), Ingredient.fromItems(Blocks.GLOWSTONE), 25);
		quartzRecipes = new ArrayList<>();
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.QUARTZ, 4), Ingredient.fromItems(Blocks.QUARTZ_BLOCK), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.darkQuartz, 4), Ingredient.fromItems(ModFluffBlocks.darkQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.manaQuartz, 4), Ingredient.fromItems(ModFluffBlocks.manaQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.blazeQuartz, 4), Ingredient.fromItems(ModFluffBlocks.blazeQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.lavenderQuartz, 4), Ingredient.fromItems(ModFluffBlocks.lavenderQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.redQuartz, 4), Ingredient.fromItems(ModFluffBlocks.redQuartz), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.elfQuartz, 4), Ingredient.fromItems(ModFluffBlocks.elfQuartz), 25));

		chiseledBrickRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.CHISELED_STONE_BRICKS, 1), Ingredient.fromItems(Blocks.STONE_BRICKS), 150);
		iceRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ICE), Ingredient.fromItems(Blocks.SNOW), 2250);

		swampFolliageRecipes = new ArrayList<>();
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LILY_PAD), Ingredient.fromItems(Blocks.VINE), 320));
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.VINE), Ingredient.fromItems(Blocks.LILY_PAD), 320));

		fishRecipes = new ArrayList<>();
		Item[] fishes = { Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH };
		for(int i = 0; i < fishes.length; i++) {
			Ingredient in = Ingredient.fromItems(fishes[i]);
			ItemStack out = new ItemStack(i == fishes.length - 1 ? fishes[0] : fishes[i + 1]);
			fishRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(out, in, 200));
		}

		cropRecipes = new ArrayList<>();
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.WHEAT_SEEDS), Ingredient.fromItems(Items.COCOA_BEANS), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.POTATO), Ingredient.fromItems(Items.WHEAT), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.CARROT), Ingredient.fromItems(Items.POTATO), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.BEETROOT_SEEDS), Ingredient.fromItems(Items.CARROT), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.MELON_SEEDS), Ingredient.fromItems(Items.BEETROOT_SEEDS), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.PUMPKIN_SEEDS), Ingredient.fromItems(Items.MELON_SEEDS), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.COCOA_BEANS), Ingredient.fromItems(Items.PUMPKIN_SEEDS), 6000));

		potatoRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.POTATO), Ingredient.fromItems(Items.POISONOUS_POTATO), 1200);
		netherWartRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.NETHER_WART), Ingredient.fromItems(Items.BLAZE_ROD), 4000);

		gunpowderAndFlintRecipes = new ArrayList<>();
		gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.FLINT), Ingredient.fromItems(Items.GUNPOWDER), 200));
		gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.GUNPOWDER), Ingredient.fromItems(Items.FLINT), 200));

		nameTagRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.NAME_TAG), Ingredient.fromItems(Items.WRITABLE_BOOK), 16000);

		stringRecipes = new ArrayList<>();
		stringRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.STRING, 3), Ingredient.fromTag(ItemTags.WOOL), 100));

		slimeballCactusRecipes = new ArrayList<>();
		slimeballCactusRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.SLIME_BALL), Ingredient.fromItems(Blocks.CACTUS), 1200));
		slimeballCactusRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.CACTUS), Ingredient.fromItems(Items.SLIME_BALL), 1200));

		enderPearlRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.ENDER_PEARL), Ingredient.fromItems(Items.GHAST_TEAR), 28000);

		redstoneToGlowstoneRecipes = new ArrayList<>();
		redstoneToGlowstoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.REDSTONE), Ingredient.fromItems(Items.GLOWSTONE_DUST), 300));
		redstoneToGlowstoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.GLOWSTONE_DUST), Ingredient.fromItems(Items.REDSTONE), 300));

		sandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.SAND), Ingredient.fromItems(Blocks.COBBLESTONE), 50);
		redSandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_SAND), Ingredient.fromItems(Blocks.TERRACOTTA), 50);

		clayBreakdownRecipes = new ArrayList<>();
		clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.CLAY_BALL, 4), Ingredient.fromItems(Blocks.CLAY), 25));
		clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.BRICK, 4), Ingredient.fromItems(Blocks.BRICKS), 25));

		coarseDirtRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.COARSE_DIRT), Ingredient.fromItems(Blocks.DIRT), 120);

		stoneRecipes = new ArrayList<>();
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ANDESITE), Ingredient.fromItems(Blocks.STONE), 200));
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DIORITE), Ingredient.fromItems(Blocks.ANDESITE), 200));
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.GRANITE), Ingredient.fromItems(Blocks.DIORITE), 200));
		stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ANDESITE), Ingredient.fromItems(Blocks.GRANITE), 200));

		tallgrassRecipes = new ArrayList<>();
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DEAD_BUSH), Ingredient.fromItems(Blocks.FERN), 500));
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.GRASS), Ingredient.fromItems(Blocks.DEAD_BUSH), 500));
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.FERN), Ingredient.fromItems(Blocks.GRASS), 500));

		flowersRecipes = new ArrayList<>();
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.POPPY), Ingredient.fromItems(Blocks.DANDELION), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.BLUE_ORCHID), Ingredient.fromItems(Blocks.POPPY), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ALLIUM), Ingredient.fromItems(Blocks.BLUE_ORCHID), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.AZURE_BLUET), Ingredient.fromItems(Blocks.ALLIUM), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.RED_TULIP), Ingredient.fromItems(Blocks.AZURE_BLUET), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ORANGE_TULIP), Ingredient.fromItems(Blocks.RED_TULIP), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.WHITE_TULIP), Ingredient.fromItems(Blocks.ORANGE_TULIP), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.PINK_TULIP), Ingredient.fromItems(Blocks.WHITE_TULIP), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.OXEYE_DAISY), Ingredient.fromItems(Blocks.PINK_TULIP), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.SUNFLOWER), Ingredient.fromItems(Blocks.OXEYE_DAISY), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.LILAC), Ingredient.fromItems(Blocks.SUNFLOWER), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ROSE_BUSH), Ingredient.fromItems(Blocks.LILAC), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.PEONY), Ingredient.fromItems(Blocks.ROSE_BUSH), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.DANDELION), Ingredient.fromItems(Blocks.PEONY), 400));

		chorusRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.CHORUS_FLOWER), Ingredient.fromItems(Items.POPPED_CHORUS_FRUIT), 10000);
		
		if(Botania.gardenOfGlassLoaded) {
			prismarineRecipes = new ArrayList<>();
			prismarineRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.PRISMARINE_SHARD), Ingredient.fromItems(Items.QUARTZ), 1000));
			prismarineRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.PRISMARINE_CRYSTALS), Ingredient.fromItems(Items.PRISMARINE_SHARD), 500));
		}
	}
}
