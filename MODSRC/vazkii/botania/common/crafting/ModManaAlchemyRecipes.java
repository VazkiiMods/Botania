/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 2, 2014, 7:50:07 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

public final class ModManaAlchemyRecipes {

	public static RecipeManaInfusion leatherRecipe;
	public static List<RecipeManaInfusion> woodRecipes;
	public static List<RecipeManaInfusion> saplingRecipes;
	public static RecipeManaInfusion glowstoneDustRecipe;
	public static RecipeManaInfusion netherQuartzRecipe;
	public static RecipeManaInfusion chiseledBrickRecipe;
	public static RecipeManaInfusion iceRecipe;
	public static List<RecipeManaInfusion> swampFolliageRecipes;
	public static List<RecipeManaInfusion> fishRecipes;
	public static List<RecipeManaInfusion> cropRecipes;
	public static RecipeManaInfusion potatoRecipe;
	public static RecipeManaInfusion netherWartRecipe;
	public static List<RecipeManaInfusion> gunpowderAndFlintRecipes;
	public static RecipeManaInfusion nameTagRecipe;
	public static RecipeManaInfusion enderPearlRecipe;
	public static RecipeManaInfusion sandRecipe;

	public static void init() {
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
		netherQuartzRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.quartz, 4), new ItemStack(Blocks.quartz_block), 25);
		chiseledBrickRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.stonebrick, 1, 3), new ItemStack(Blocks.stonebrick), 150);
		iceRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ice), new ItemStack(Blocks.snow), 2250);

		swampFolliageRecipes = new ArrayList();
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.waterlily), new ItemStack(Blocks.vine), 320));
		swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.vine), new ItemStack(Blocks.waterlily), 320));

		fishRecipes = new ArrayList();
		for(int i = 0; i < 4; i++)
			fishRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.fish, 1, i == 3 ? 0 : i + 1), new ItemStack(Items.fish, 1, i), 200));

		cropRecipes = new ArrayList();
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.wheat_seeds), new ItemStack(Items.pumpkin_seeds), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.potato), new ItemStack(Items.wheat), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.carrot), new ItemStack(Items.potato), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.melon_seeds), new ItemStack(Items.carrot), 6000));
		cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.pumpkin_seeds), new ItemStack(Items.melon_seeds), 6000));

		potatoRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.potato), new ItemStack(Items.poisonous_potato), 1200);
		netherWartRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.nether_wart), new ItemStack(Items.blaze_rod), 4000);

		gunpowderAndFlintRecipes = new ArrayList();
		gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.flint), new ItemStack(Items.gunpowder), 200));
		gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.gunpowder), new ItemStack(Items.flint), 4000));

		nameTagRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.name_tag), new ItemStack(Items.writable_book), 16000);
		enderPearlRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.ender_pearl, 2), new ItemStack(Items.ghast_tear), 28000);
		sandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.sand), new ItemStack(Blocks.cobblestone), 50);
	}
}
