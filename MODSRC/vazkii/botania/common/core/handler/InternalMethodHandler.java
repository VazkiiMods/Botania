/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:44:59 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.Icon;
import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class InternalMethodHandler implements IInternalMethodHandler {

	@Override
	public LexiconPage textPage(String key) {
		return new PageText(key);
	}

	@Override
	public LexiconPage imagePage(String key, String resource) {
		return new PageImage(key, resource);
	}

	@Override
	public LexiconPage craftingRecipesPage(String key, List<IRecipe> recipes) {
		return new PageCraftingRecipe(key, recipes);
	}

	@Override
	public LexiconPage craftingRecipePage(String key, IRecipe recipe) {
		return new PageCraftingRecipe(key, recipe);
	}

	@Override
	public Icon getSubTileIconForName(String name) {
		Icon icon = BlockSpecialFlower.icons.get(name);
		return icon == null ? Block.plantRed.getIcon(0, 0) : icon;
	}

	@Override
	public LexiconPage petalRecipesPage(String key, List<RecipePetals> recipes) {
		return new PagePetalRecipe(key, recipes);
	}

	@Override
	public LexiconPage petalRecipePage(String key, RecipePetals recipe) {
		return new PagePetalRecipe(key, recipe);
	}

	@Override
	public LexiconPage runeRecipesPage(String key, List<RecipeRuneAltar> recipes) {
		return new PageRuneRecipe(key, recipes);
	}

	@Override
	public LexiconPage runeRecipePage(String key, RecipeRuneAltar recipe) {
		return new PageRuneRecipe(key, recipe);
	}

	@Override
	public LexiconPage manaInfusionRecipesPage(String key, List<RecipeManaInfusion> recipes) {
		return new PageManaInfusionRecipe(key, recipes);
	}

	@Override
	public LexiconPage manaInfusionRecipePage(String key, RecipeManaInfusion recipe) {
		return new PageManaInfusionRecipe(key, recipe);
	}


	@Override
	public ItemStack getSubTileAsStack(String subTile) {
		return ItemBlockSpecialFlower.ofType(subTile);
	}

}
