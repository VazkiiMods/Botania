package vazkii.botania.common.crafting;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class ModElvenTradeRecipes {

	public static RecipeElvenTrade dreamwoodRecipe;
	public static RecipeElvenTrade elementiumRecipe;
	public static RecipeElvenTrade pixieDustRecipe;
	public static RecipeElvenTrade dragonstoneRecipe;

	public static void init() {
		dreamwoodRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.dreamwood), LibOreDict.LIVING_WOOD);
		elementiumRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource, 1, 7), LibOreDict.MANA_STEEL, LibOreDict.MANA_STEEL);
		pixieDustRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource, 1, 8), LibOreDict.MANA_PEARL);
		dragonstoneRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource, 1, 9), LibOreDict.MANA_DIAMOND);
	}

}
