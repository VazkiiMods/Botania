/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 22], 2014], 2:22:21 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import java.util.Arrays;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibOreDict;

public final class ModPetalRecipes {

	public static final String white = LibOreDict.PETAL[0], orange = LibOreDict.PETAL[1], magenta = LibOreDict.PETAL[2], lightBlue = LibOreDict.PETAL[3], yellow = LibOreDict.PETAL[4], lime = LibOreDict.PETAL[5], pink = LibOreDict.PETAL[6], gray = LibOreDict.PETAL[7], lightGray = LibOreDict.PETAL[8], cyan = LibOreDict.PETAL[9], purple = LibOreDict.PETAL[10], blue = LibOreDict.PETAL[11], brown = LibOreDict.PETAL[12], green = LibOreDict.PETAL[13], red = LibOreDict.PETAL[14], black = LibOreDict.PETAL[15];
	public static final String whiteMana = LibOreDict.MANA_PETAL[0], orangeMana = LibOreDict.MANA_PETAL[1], magentaMana = LibOreDict.MANA_PETAL[2], lightBlueMana = LibOreDict.MANA_PETAL[3], yellowMana = LibOreDict.MANA_PETAL[4], limeMana = LibOreDict.MANA_PETAL[5], pinkMana = LibOreDict.MANA_PETAL[6], grayMana = LibOreDict.MANA_PETAL[7], lightGrayMana = LibOreDict.MANA_PETAL[8], cyanMana = LibOreDict.MANA_PETAL[9], purpleMana = LibOreDict.MANA_PETAL[10], blueMana = LibOreDict.MANA_PETAL[11], brownMana = LibOreDict.MANA_PETAL[12], greenMana = LibOreDict.MANA_PETAL[13], redMana = LibOreDict.MANA_PETAL[14], blackMana = LibOreDict.MANA_PETAL[15];
	public static final String runeWater = LibOreDict.RUNE[0], runeFire = LibOreDict.RUNE[1], runeEarth = LibOreDict.RUNE[2], runeAir = LibOreDict.RUNE[3], runeSpring = LibOreDict.RUNE[4], runeSummer = LibOreDict.RUNE[5], runeAutumn = LibOreDict.RUNE[6], runeWinter = LibOreDict.RUNE[7], runeMana = LibOreDict.RUNE[8], runeLust = LibOreDict.RUNE[9], runeGluttony = LibOreDict.RUNE[10], runeGreed = LibOreDict.RUNE[11], runeSloth = LibOreDict.RUNE[12], runeWrath = LibOreDict.RUNE[13], runeEnvy = LibOreDict.RUNE[14], runePride = LibOreDict.RUNE[15];
	public static final String redstoneRoot = LibOreDict.REDSTONE_ROOT;
	public static final String pixieDust = LibOreDict.PIXIE_DUST;
	public static final String gaiaSpirit = LibOreDict.LIFE_ESSENCE;

	public static RecipePetals pureDaisyRecipe;
	public static RecipePetals manastarRecipe;

	public static RecipePetals daybloomRecipe;
	public static RecipePetals nightshadeRecipe;
	public static RecipePetals endoflameRecipe;
	public static RecipePetals hydroangeasRecipe;
	public static RecipePetals thermalilyRecipe;
	public static RecipePetals arcaneRoseRecipe;
	public static RecipePetals munchdewRecipe;
	public static RecipePetals entropinnyumRecipe;
	public static RecipePetals kekimurusRecipe;
	public static RecipePetals gourmaryllisRecipe;
	public static RecipePetals narslimmusRecipe;
	public static RecipePetals spectrolusRecipe;
	public static RecipePetals rafflowsiaRecipe;
	public static RecipePetals dandelifeonRecipe;

	public static RecipePetals jadedAmaranthusRecipe;
	public static RecipePetals bellethorneRecipe;
	public static RecipePetals dreadthorneRecipe;
	public static RecipePetals heiseiDreamRecipe;
	public static RecipePetals tigerseyeRecipe;
	public static RecipePetals orechidRecipe;
	public static RecipePetals orechidIgnemRecipe;
	public static RecipePetals fallenKanadeRecipe;
	public static RecipePetals exoflameRecipe;
	public static RecipePetals agricarnationRecipe;
	public static RecipePetals hopperhockRecipe;
	public static RecipePetals tangleberrieRecipe;
	public static RecipePetals jiyuuliaRecipe;
	public static RecipePetals rannuncarpusRecipe;
	public static RecipePetals hyacidusRecipe;
	public static RecipePetals pollidisiacRecipe;
	public static RecipePetals clayconiaRecipe;
	public static RecipePetals looniumRecipe;
	public static RecipePetals daffomillRecipe;
	public static RecipePetals vinculotusRecipe;
	public static RecipePetals spectranthemumRecipe;
	public static RecipePetals medumoneRecipe;
	public static RecipePetals marimorphosisRecipe;
	public static RecipePetals bubbellRecipe;
	public static RecipePetals solegnoliaRecipe;

	public static void init() {
		pureDaisyRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY), white, white, white, white);
		manastarRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_MANASTAR), lightBlueMana, greenMana, redMana, cyanMana);

		daybloomRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DAYBLOOM), yellow, yellow, orange, lightBlue);
		nightshadeRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_NIGHTSHADE), black, black, purple, gray);
		endoflameRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENDOFLAME), brown, red, redMana, brownMana, lightGray);
		hydroangeasRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYDROANGEAS), blueMana, blue, pink, cyan, cyan, cyanMana);
		thermalilyRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_THERMALILY), red, orangeMana, orange, runeEarth, runeFire);
		arcaneRoseRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ARCANE_ROSE), pink, pinkMana, pinkMana, lime, purple, purpleMana, runeMana);
		munchdewRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_MUNCHDEW), lime, limeMana, green, red, redMana, white, whiteMana, runeGluttony);
		entropinnyumRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENTROPINNYUM), red, redMana, gray, grayMana, white, whiteMana, runeWrath, runeFire);
		kekimurusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_KEKIMURUS), white, whiteMana, orangeMana, brownMana, brownMana, orange, runeGluttony, pixieDust);
		gourmaryllisRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_GOURMARYLLIS), lightGray, lightGrayMana, yellowMana, yellow, yellowMana, red, runeFire, runeSummer);
		narslimmusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_NARSLIMMUS), lime, limeMana, green, greenMana, black, runeSummer, runeWater);
		spectrolusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SPECTROLUS), redMana, orangeMana, yellowMana, greenMana, blueMana, cyanMana, purpleMana, white, runeWinter, runeAir, pixieDust);
		rafflowsiaRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_RAFFLOWSIA), purpleMana, purpleMana, blackMana, green, greenMana, runeEarth, runePride, pixieDust);
		dandelifeonRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DANDELIFEON), purpleMana, purpleMana, limeMana, greenMana, runeWater, runeFire, runeEarth, runeAir, gaiaSpirit);

		jadedAmaranthusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_JADED_AMARANTHUS), purpleMana, purpleMana, lime, purple, green, runeSpring, redstoneRoot);
		bellethorneRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_BELLETHORN), redMana, redMana, cyan, cyan, red, redstoneRoot);
		dreadthorneRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DREADTHORN), blackMana, purpleMana, cyan, cyan, black, purple, redstoneRoot);
		heiseiDreamRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HEISEI_DREAM), purpleMana, pinkMana, magentaMana, purple, pink, magenta, runeWrath, pixieDust);
		tigerseyeRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_TIGERSEYE), yellow, brownMana, orangeMana, lime, runeAutumn);

		if(Botania.gardenOfGlassLoaded)
			orechidRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID), gray, grayMana, lightGray, lightGrayMana, lightBlue, yellow, green, red);
		else orechidRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID), gray, grayMana, lightGray, lightGrayMana, lightBlue, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust);

		orechidIgnemRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID_IGNEM), red, redMana, black, blackMana, white, white, blue, yellow, runePride, runeGreed, redstoneRoot, pixieDust);
		if(ConfigHandler.fallenKanadeEnabled)
			fallenKanadeRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_FALLEN_KANADE), white, whiteMana, whiteMana, yellowMana, orangeMana, brownMana, runeSpring);
		exoflameRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_EXOFLAME), red, redMana, redMana, gray, lightGray, runeFire, runeSummer);
		agricarnationRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_AGRICARNATION), redMana, yellow, greenMana, lime, lime, limeMana, runeSpring, redstoneRoot);
		hopperhockRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HOPPERHOCK), gray, grayMana, lightGray, lightGrayMana, runeAir, redstoneRoot);
		tangleberrieRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_TANGLEBERRIE), grayMana, lightGrayMana, cyanMana, cyan, runeAir, runeEarth);
		jiyuuliaRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_JIYUULIA), pinkMana, purpleMana, lightGrayMana, pink, runeWater, runeAir);
		rannuncarpusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_RANNUNCARPUS), orangeMana, orangeMana, orange, yellow, runeEarth, red, redMana, redstoneRoot);
		hyacidusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYACIDUS), purple, purpleMana, magenta, magentaMana, brown, green, greenMana, runeWater, runeAutumn, redstoneRoot);
		pollidisiacRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_POLLIDISIAC), orange, orangeMana, red, redMana, pink, pinkMana, runeLust, runeFire);
		clayconiaRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_CLAYCONIA), gray, lightGray, lightGrayMana, lightGrayMana, grayMana, cyan, runeEarth);
		looniumRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_LOONIUM), green, green, greenMana, greenMana, gray, grayMana, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust);
		daffomillRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DAFFOMILL), white, whiteMana, brown, yellowMana, runeAir, redstoneRoot);
		vinculotusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_VINCULOTUS), purpleMana, purpleMana, lime, black, blackMana, black, runeWater, runeSloth, runeLust, redstoneRoot);
		spectranthemumRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SPECTRANTHEMUM), whiteMana, whiteMana, white, white, cyan, runeEnvy, runeWater, redstoneRoot, pixieDust);
		medumoneRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_MEDUMONE), brown, brown, brownMana, gray, grayMana, runeEarth, redstoneRoot);
		marimorphosisRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_MARIMORPHOSIS), grayMana, yellow, green, lime, lightBlue, red, runeEarth, runeFire, redstoneRoot);
		bubbellRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_BUBBELL), cyanMana, cyan, blue, lightBlue, lightBlueMana, runeWater, runeSummer, pixieDust);
		solegnoliaRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SOLEGNOLIA), brown, red, brownMana, red, blue, redstoneRoot);

		ItemStack stack = new ItemStack(Items.skull, 1, 3);
		ItemNBTHelper.setString(stack, "SkullOwner", "Vazkii");
		Object[] inputs = new Object[16];
		Arrays.fill(inputs, pink);
		BotaniaAPI.registerPetalRecipe(stack, inputs);
	}
}
