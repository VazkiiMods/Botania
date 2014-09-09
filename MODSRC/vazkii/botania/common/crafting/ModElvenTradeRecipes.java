package vazkii.botania.common.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class ModElvenTradeRecipes {

	public static RecipeElvenTrade dreamwoodRecipe;
	public static List<RecipeElvenTrade> elementiumRecipes;
	public static RecipeElvenTrade pixieDustRecipe;
	public static RecipeElvenTrade dragonstoneRecipe;
	public static RecipeElvenTrade elvenQuartzRecipe;
	public static RecipeElvenTrade alfglassRecipe;

	public static void init() {
		dreamwoodRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.dreamwood), LibOreDict.LIVING_WOOD);

		elementiumRecipes = new ArrayList();
		elementiumRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource, 1, 7), LibOreDict.MANA_STEEL, LibOreDict.MANA_STEEL));
		elementiumRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.storage, 1, 2), new ItemStack(ModBlocks.storage), new ItemStack(ModBlocks.storage)));

		pixieDustRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource, 1, 8), LibOreDict.MANA_PEARL);
		dragonstoneRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource, 1, 9), LibOreDict.MANA_DIAMOND);
		elvenQuartzRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.quartz, 1, 5), new ItemStack(Items.quartz));
		alfglassRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.elfGlass), new ItemStack(Blocks.glass));
	}

}
