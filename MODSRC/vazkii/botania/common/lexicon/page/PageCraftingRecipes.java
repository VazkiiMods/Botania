/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 19, 2014, 4:58:19 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.crafting.IRecipe;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.page.LexiconPage;

public class PageCraftingRecipes extends LexiconPage {

	List<IRecipe> recipes;
	
	public PageCraftingRecipes(String unlocalizedName, List<IRecipe> recipes) {
		super(unlocalizedName);
		this.recipes = recipes;
	}

	public PageCraftingRecipes(String unlocalizedName, IRecipe recipe) {
		this(unlocalizedName, Arrays.asList(recipe));
	}
	
	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		// TODO
	}

}
