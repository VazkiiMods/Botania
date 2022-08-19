/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 17, 2015, 4:46:36 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.botania.common.crafting.recipe.PhantomInkRecipe;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemPhantomInk extends ItemMod {

	public ItemPhantomInk() {
		setUnlocalizedName(LibItemNames.PHANTOM_INK);
		GameRegistry.addRecipe(new PhantomInkRecipe());
		RecipeSorter.register("botania:phantomInk", PhantomInkRecipe.class, Category.SHAPELESS, "");
	}

}
