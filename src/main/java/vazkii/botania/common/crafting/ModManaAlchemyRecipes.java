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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

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
	public static RecipeManaInfusion prismarineRecipe;
	public static List<RecipeManaInfusion> stoneRecipes;
	public static List<RecipeManaInfusion> tallgrassRecipes;
	public static List<RecipeManaInfusion> flowersRecipes;

	public static void init() {
		if (!ConfigHandler.enableDefaultRecipes) return;

		leatherRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.leather), new ItemStack(Items.rotten_flesh), 600);

		woodRecipes = new ArrayList();
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log, 1, 0), new ItemStack(Blocks.log2, 1, 1), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log, 1, 1), new ItemStack(Blocks.log, 1, 0), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log, 1, 2), new ItemStack(Blocks.log, 1, 1), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log, 1, 3), new ItemStack(Blocks.log, 1, 2), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log2, 1, 0), new ItemStack(Blocks.log, 1, 3), 40));
		woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log2, 1, 1), new ItemStack(Blocks.log2, 1, 0), 40));

		saplingRecipes = new ArrayList();
		for(int i = 0; i < 6; i++)
			saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.sapling, 1, i == 5 ? 0 : i + 1), new ItemStack(Blocks.sapling, 1, i), 120));

		glowstoneDustRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.glowstone_dust, 4), new ItemStack(Blocks.glowstone), 25);
		quartzRecipes = new ArrayList();
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.quartz, 4), new ItemStack(Blocks.quartz_block, 1, Short.MAX_VALUE), 25));
		if(ConfigHandler.darkQuartzEnabled)
			quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 0), new ItemStack(ModFluffBlocks.darkQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 1), new ItemStack(ModFluffBlocks.manaQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 2), new ItemStack(ModFluffBlocks.blazeQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 3), new ItemStack(ModFluffBlocks.lavenderQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 4), new ItemStack(ModFluffBlocks.redQuartz, 1, Short.MAX_VALUE), 25));
		quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 5), new ItemStack(ModFluffBlocks.elfQuartz, 1, Short.MAX_VALUE), 25));

		chiseledBrickRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.stonebrick, 1, 3), new ItemStack(Blocks.stonebrick), 150);
		iceRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ice), new ItemStack(Blocks.snow), 2250);

		swampFolliageRecipes = new ArrayList();
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.waterlily), new ItemStack(Blocks.vine), 320));
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.vine), new ItemStack(Blocks.waterlily), 320));

		fishRecipes = new ArrayList();
		for(int i = 0; i < 4; i++)
			fishRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.fish, 1, i == 3 ? 0 : i + 1), new ItemStack(Items.fish, 1, i), 200));

		cropRecipes = new ArrayList();
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.wheat_seeds), new ItemStack(Items.dye, 1, 3), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.potato), new ItemStack(Items.wheat), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.carrot), new ItemStack(Items.potato), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.melon_seeds), new ItemStack(Items.carrot), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.pumpkin_seeds), new ItemStack(Items.melon_seeds), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.dye, 1, 3), new ItemStack(Items.pumpkin_seeds), 6000));

		potatoRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.potato), new ItemStack(Items.poisonous_potato), 1200);
		netherWartRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.nether_wart), new ItemStack(Items.blaze_rod), 4000);

		gunpowderAndFlintRecipes = new ArrayList();
		gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.flint), new ItemStack(Items.gunpowder), 200));
		gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.gunpowder), new ItemStack(Items.flint), 4000));

		nameTagRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.name_tag), new ItemStack(Items.writable_book), 16000);

		stringRecipes = new ArrayList();
		for(int i = 0; i < 16; i++)
			stringRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.string, 3), new ItemStack(Blocks.wool, 1, i), 100));

		slimeballCactusRecipes = new ArrayList();
		slimeballCactusRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.slime_ball), new ItemStack(Blocks.cactus), 1200));
		slimeballCactusRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.cactus), new ItemStack(Items.slime_ball), 1200));

		enderPearlRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.ender_pearl), new ItemStack(Items.ghast_tear), 28000);

		redstoneToGlowstoneRecipes = new ArrayList();
		redstoneToGlowstoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.redstone), new ItemStack(Items.glowstone_dust), 300));
		redstoneToGlowstoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.glowstone_dust), new ItemStack(Items.redstone), 300));

		sandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Block.getBlockFromName("sand")), new ItemStack(Blocks.cobblestone), 50);
		redSandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Block.getBlockFromName("sand"), 1, 1), new ItemStack(Blocks.hardened_clay), 50);

		clayBreakdownRecipes = new ArrayList();
		clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.clay_ball, 4), new ItemStack(Blocks.clay), 25));
		clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.brick, 4), new ItemStack(Blocks.brick_block), 25));

		coarseDirtRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.dirt, 1, 1), new ItemStack(Blocks.dirt), 120);

		prismarineRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.manaResource, 1, 10), new ItemStack(Items.quartz), 200);

		if(ConfigHandler.stones18Enabled) {
			stoneRecipes = new ArrayList();
			stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModFluffBlocks.stone), "stone", 200));
			for(int i = 0; i < 4; i++)
				stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModFluffBlocks.stone, 1, i), new ItemStack(ModFluffBlocks.stone, 1, i == 0 ? 3 : i - 1), 200));
		}

		tallgrassRecipes = new ArrayList();
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.deadbush), new ItemStack(Blocks.tallgrass, 1, 2), 500));
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.tallgrass, 1, 1), new ItemStack(Blocks.deadbush), 500));
		tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.tallgrass, 1, 2), new ItemStack(Blocks.tallgrass, 1, 1), 500));

		flowersRecipes = new ArrayList();
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower), new ItemStack(Blocks.yellow_flower), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 1), new ItemStack(Blocks.red_flower), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 2), new ItemStack(Blocks.red_flower, 1, 1), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 3), new ItemStack(Blocks.red_flower, 1, 2), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 4), new ItemStack(Blocks.red_flower, 1, 3), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 5), new ItemStack(Blocks.red_flower, 1, 4), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 6), new ItemStack(Blocks.red_flower, 1, 5), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 7), new ItemStack(Blocks.red_flower, 1, 6), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 8), new ItemStack(Blocks.red_flower, 1, 7), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.double_plant), new ItemStack(Blocks.red_flower, 1, 8), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.double_plant, 1, 1), new ItemStack(Blocks.double_plant), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.double_plant, 1, 4), new ItemStack(Blocks.double_plant, 1, 1), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.double_plant, 1, 5), new ItemStack(Blocks.double_plant, 1, 4), 400));
		flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.yellow_flower), new ItemStack(Blocks.double_plant, 1, 5), 400));
	}
}
