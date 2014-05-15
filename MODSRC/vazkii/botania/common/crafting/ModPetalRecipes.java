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
	public static final String runeWater = LibOreDict.RUNE[0], runeFire = LibOreDict.RUNE[1], runeEarth = LibOreDict.RUNE[2], runeAir = LibOreDict.RUNE[3], runeSpring = LibOreDict.RUNE[4], runeSummer = LibOreDict.RUNE[5], runeAutumn = LibOreDict.RUNE[6], runeWinter = LibOreDict.RUNE[7], runeMana = LibOreDict.RUNE[8], runeLust = LibOreDict.RUNE[9], runeGluttony = LibOreDict.RUNE[10], runeGreed = LibOreDict.RUNE[11], runeSloth = LibOreDict.RUNE[12], runeWrath = LibOreDict.RUNE[13], runeEnvy = LibOreDict.RUNE[14], runePride = LibOreDict.RUNE[15];
	public static final String redstoneRoot = LibOreDict.REDSTONE_ROOT;
	
	public static RecipePetals pureDaisyRecipe;

	public static RecipePetals daybloomRecipe;
	public static RecipePetals nightshadeRecipe;
	public static RecipePetals endoflameRecipe;
	public static RecipePetals hydroangeasRecipe;
	public static RecipePetals thermalilyRecipe;
	public static RecipePetals arcaneRoseRecipe;

	public static RecipePetals jadedAmaranthusRecipe;
	public static RecipePetals bellethorneRecipe;
	public static RecipePetals dreadthorneRecipe;
	public static RecipePetals heiseiDreamRecipe;
	public static RecipePetals tigerseyeRecipe;
	public static RecipePetals orechidRecipe;
	public static RecipePetals fallenKanadeRecipe;
	public static RecipePetals exoflameRecipe;
	public static RecipePetals agricarnationRecipe;
	public static RecipePetals hopperhockRecipe;
	public static RecipePetals tangleberrieRecipe;
	public static RecipePetals jiyuuliaRecipe;
	public static RecipePetals rannuncarpusRecipe;
	public static RecipePetals hyacidusRecipe;

	public static void init() {
		pureDaisyRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY), white, white, white, white);

		daybloomRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DAYBLOOM), yellow, yellow, orange, lightBlue);
		nightshadeRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_NIGHTSHADE), black, black, purple, gray);
		endoflameRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENDOFLAME), brown, red, redMana, brownMana, lightGray);
		hydroangeasRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYDROANGEAS), blueMana, blue, pink, lightBlue);
		thermalilyRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_THERMALILY), red, orangeMana, orange, runeEarth, runeFire);
		arcaneRoseRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ARCANE_ROSE), pink, pinkMana, pinkMana, lime, purple, purpleMana, runeMana);

		jadedAmaranthusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_JADED_AMARANTHUS), purpleMana, purpleMana, lime, purple, green, runeSpring, redstoneRoot);
		bellethorneRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_BELLETHORN), redMana, redMana, cyan, cyan, red, redstoneRoot);
		dreadthorneRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DREADTHORN), blackMana, purpleMana, cyan, cyan, black, purple, redstoneRoot);
		heiseiDreamRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HEISEI_DREAM), purpleMana, pinkMana, magentaMana, purple, pink, magenta, runeWrath);
		tigerseyeRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_TIGERSEYE), yellow, brownMana, orangeMana, lime, runeAutumn);
		orechidRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID), gray, grayMana, lightGray, lightGrayMana, lightBlue, yellow, green, red, runePride, runeSloth, redstoneRoot);
		fallenKanadeRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_FALLEN_KANADE), white, whiteMana, whiteMana, yellowMana, orangeMana, brownMana, runeSpring);
		exoflameRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_EXOFLAME), red, redMana, redMana, gray, lightGray, runeFire, runeSummer);
		agricarnationRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_AGRICARNATION), redMana, yellow, greenMana, lime, lime, limeMana, runeSpring);
		hopperhockRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HOPPERHOCK), gray, grayMana, lightGray, lightGrayMana, runeAir, redstoneRoot);
		tangleberrieRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_TANGLEBERRIE), grayMana, lightGrayMana, cyanMana, cyan, runeAir, runeEarth);
		jiyuuliaRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_JIYUULIA), pinkMana, purpleMana, lightGrayMana, pink, runeWater, runeAir);
		rannuncarpusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_RANNUNCARPUS), orangeMana, orangeMana, orange, yellow, runeEarth, red, redMana, redstoneRoot);
		hyacidusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYACIDUS), purple, purpleMana, magenta, magentaMana, brown, green, greenMana, runeWater, runeAutumn, redstoneRoot);
	}
}
