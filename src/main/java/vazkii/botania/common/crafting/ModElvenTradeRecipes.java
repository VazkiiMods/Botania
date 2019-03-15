package vazkii.botania.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

import java.util.ArrayList;
import java.util.List;

public class ModElvenTradeRecipes {

	public static RecipeElvenTrade dreamwoodRecipe;
	public static List<RecipeElvenTrade> elementiumRecipes;
	public static RecipeElvenTrade pixieDustRecipe;
	public static List<RecipeElvenTrade> dragonstoneRecipes;
	public static RecipeElvenTrade elvenQuartzRecipe;
	public static RecipeElvenTrade alfglassRecipe;

	public static void init() {
		dreamwoodRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.dreamwood), LibOreDict.LIVING_WOOD);

		elementiumRecipes = new ArrayList<>();
		elementiumRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.elementium), LibOreDict.MANA_STEEL, LibOreDict.MANA_STEEL));
		elementiumRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.elementiumBlock), new ItemStack(ModBlocks.manasteelBlock), new ItemStack(ModBlocks.manasteelBlock)));

		pixieDustRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.pixieDust), LibOreDict.MANA_PEARL);
		dragonstoneRecipes = new ArrayList<>();
		dragonstoneRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.dragonstone), LibOreDict.MANA_DIAMOND));
		dragonstoneRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.dragonstoneBlock), new ItemStack(ModBlocks.manaDiamondBlock)));

		elvenQuartzRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.elfQuartz), new ItemStack(Items.QUARTZ));
		alfglassRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.elfGlass), new ItemStack(ModBlocks.manaGlass));

		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Items.IRON_INGOT), Ingredient.fromItems(Items.IRON_INGOT));
		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Blocks.IRON_BLOCK), Ingredient.fromItems(Blocks.IRON_BLOCK));
		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Items.ENDER_PEARL), Ingredient.fromItems(Items.ENDER_PEARL));
		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Items.DIAMOND), Ingredient.fromItems(Items.DIAMOND));
		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Blocks.DIAMOND_BLOCK), Ingredient.fromItems(Blocks.DIAMOND_BLOCK));
	}

}
