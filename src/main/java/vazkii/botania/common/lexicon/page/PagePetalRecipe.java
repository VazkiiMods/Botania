/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 8, 2014, 1:11:35 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PagePetalRecipe extends PageModRecipe<RecipePetals> {

	private static final List<RecipePetals> DUMMY = Collections.singletonList(new RecipePetals(new ResourceLocation(LibMisc.MOD_ID, "dummy"), ItemStack.EMPTY));

	public PagePetalRecipe(String unlocalizedName, Predicate<RecipePetals> filter, Item... outputs) {
		super(unlocalizedName, filter, outputs);
	}

	public PagePetalRecipe(String unlocalizedName, Item... outputs) {
		this(unlocalizedName, r -> true, outputs);
	}

	@Override
	ItemStack getMiddleStack() {
		return new ItemStack(ModBlocks.defaultAltar);
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		ArrayList<ItemStack> list = new ArrayList<>();
		for(RecipePetals r : getRecipes())
			list.add(r.getOutput());

		return list;
	}


	@Override
	protected List<RecipePetals> findRecipes() {
		List<RecipePetals> recipes = findRecipes(BotaniaAPI.petalRecipes.values());
		if(recipes.isEmpty()) {			
			Botania.LOGGER.warn("Could not find petal apothecary recipes for items {}, using dummy", (Object) outputItems);
			return DUMMY;
		}
		return recipes;
	}

}
