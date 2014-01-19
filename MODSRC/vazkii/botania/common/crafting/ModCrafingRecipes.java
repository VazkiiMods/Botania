/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 19, 2014, 3:54:48 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModCrafingRecipes {

	public static IRecipe lexiconRecipe;
	public static List<IRecipe> recipesPetals;
	public static List<IRecipe> recipesDyes;
	public static IRecipe recipePestleAndMortar;

	public static void init() {
		// Lexicon Recipe
		addShapelessOreDictRecipe(new ItemStack(ModItems.lexicon), "treeSapling", Item.book);
		lexiconRecipe = BotaniaAPI.getLatestAddedRecipe();
		
		// Petal/Dye Recipes
		for(int i = 0; i < 16; i++) 
			addShapelessOreDictRecipe(new ItemStack(ModItems.petal, 2, i), LibOreDict.FLOWER[i]); 
		recipesPetals = BotaniaAPI.getLatestAddedRecipes(16);
		
		for(int i = 0; i < 16; i++)
			addShapelessOreDictRecipe(new ItemStack(ModItems.dye, 1, i), LibOreDict.PETAL[i], LibOreDict.PESTLE_AND_MORTAR); 
		recipesDyes = BotaniaAPI.getLatestAddedRecipes(16);
		
		// Pestle and Mortar Recipe
		addOreDictRecipe(new ItemStack(ModItems.pestleAndMortar), 
			" S", "W ", "B ",
			'S', "stickWood",
			'W', "plankWood",
			'B', Item.bowlEmpty);
		recipePestleAndMortar = BotaniaAPI.getLatestAddedRecipe();

	}
	
	private static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}
	
	private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
	}
}
