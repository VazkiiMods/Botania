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

import net.minecraft.item.crafting.IRecipe;
import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.api.page.LexiconPage;
import vazkii.botania.common.lexicon.page.PageCraftingRecipes;
import vazkii.botania.common.lexicon.page.PageImage;
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
	public LexiconPage recipesPage(String key, List<IRecipe> recipes) {
		return new PageCraftingRecipes(key, recipes);
	}

	@Override
	public LexiconPage recipePage(String key, IRecipe recipe) {
		return new PageCraftingRecipes(key, recipe);
	}

}
