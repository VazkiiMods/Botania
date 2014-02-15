/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 22], 2014], 2:22:21 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibOreDict;

public final class ModPetalRecipes {

	public static final String white = LibOreDict.PETAL[0], orange = LibOreDict.PETAL[1], magenta = LibOreDict.PETAL[2], lightBlue = LibOreDict.PETAL[3], yellow = LibOreDict.PETAL[4], lime = LibOreDict.PETAL[5], pink = LibOreDict.PETAL[6], gray = LibOreDict.PETAL[7], lightGray = LibOreDict.PETAL[8], cyan = LibOreDict.PETAL[9], purple = LibOreDict.PETAL[10], blue = LibOreDict.PETAL[11], brown = LibOreDict.PETAL[12], green = LibOreDict.PETAL[13], red = LibOreDict.PETAL[14], black = LibOreDict.PETAL[15];
	public static final String whiteMana = LibOreDict.MANA_PETAL[0], orangeMana = LibOreDict.MANA_PETAL[1], magentaMana = LibOreDict.MANA_PETAL[2], lightBlueMana = LibOreDict.MANA_PETAL[3], yellowMana = LibOreDict.MANA_PETAL[4], limeMana = LibOreDict.MANA_PETAL[5], pinkMana = LibOreDict.MANA_PETAL[6], grayMana = LibOreDict.MANA_PETAL[7], lightGrayMana = LibOreDict.MANA_PETAL[8], cyanMana = LibOreDict.MANA_PETAL[9], purpleMana = LibOreDict.MANA_PETAL[10], blueMana = LibOreDict.MANA_PETAL[11], brownMana = LibOreDict.MANA_PETAL[12], greenMana = LibOreDict.MANA_PETAL[13], redMana = LibOreDict.MANA_PETAL[14], blackMana = LibOreDict.MANA_PETAL[15];

	public static RecipePetals pureDaisyRecipe;

	public static RecipePetals daybloomRecipe;

	public static RecipePetals bellethorneRecipe;

	public static void init() {
		pureDaisyRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY), white, white, white, white);

		daybloomRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DAYBLOOM), yellow, yellow, orange, orange, lightBlue);

		bellethorneRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_BELLETHORN), redMana, redMana, cyan, cyan, red);
	}
}
