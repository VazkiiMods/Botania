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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import scala.runtime.RichDouble;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibOreDict;

import java.util.Arrays;

public final class ModPetalRecipes {

	public static final String white = LibOreDict.PETAL[0], orange = LibOreDict.PETAL[1], magenta = LibOreDict.PETAL[2], lightBlue = LibOreDict.PETAL[3], yellow = LibOreDict.PETAL[4], lime = LibOreDict.PETAL[5], pink = LibOreDict.PETAL[6], gray = LibOreDict.PETAL[7], lightGray = LibOreDict.PETAL[8], cyan = LibOreDict.PETAL[9], purple = LibOreDict.PETAL[10], blue = LibOreDict.PETAL[11], brown = LibOreDict.PETAL[12], green = LibOreDict.PETAL[13], red = LibOreDict.PETAL[14], black = LibOreDict.PETAL[15];
	public static final String runeWater = LibOreDict.RUNE[0], runeFire = LibOreDict.RUNE[1], runeEarth = LibOreDict.RUNE[2], runeAir = LibOreDict.RUNE[3], runeSpring = LibOreDict.RUNE[4], runeSummer = LibOreDict.RUNE[5], runeAutumn = LibOreDict.RUNE[6], runeWinter = LibOreDict.RUNE[7], runeMana = LibOreDict.RUNE[8], runeLust = LibOreDict.RUNE[9], runeGluttony = LibOreDict.RUNE[10], runeGreed = LibOreDict.RUNE[11], runeSloth = LibOreDict.RUNE[12], runeWrath = LibOreDict.RUNE[13], runeEnvy = LibOreDict.RUNE[14], runePride = LibOreDict.RUNE[15];
	public static final String redstoneRoot = LibOreDict.REDSTONE_ROOT;
	public static final String pixieDust = LibOreDict.PIXIE_DUST;
	public static final String gaiaSpirit = LibOreDict.LIFE_ESSENCE;
	public static final String manaPowder = LibOreDict.MANA_POWDER;

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
	public static RecipePetals shulkMeNotRecipe;
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
	public static RecipePetals bergamuteRecipe;

	public static void init() {
		pureDaisyRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY), white, white, white, white);
		manastarRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_MANASTAR), lightBlue, green, red, cyan);

		endoflameRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENDOFLAME), brown, brown, red, lightGray);
		hydroangeasRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYDROANGEAS), blue, blue, cyan, cyan);
		thermalilyRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_THERMALILY), red, orange, orange, runeEarth, runeFire);
		arcaneRoseRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ARCANE_ROSE), pink, pink, purple, purple, lime, runeMana);
		munchdewRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_MUNCHDEW), lime, lime, red, red, green, runeGluttony);
		entropinnyumRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENTROPINNYUM), red, red, gray, gray, white, white, runeWrath, runeFire);
		kekimurusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_KEKIMURUS), white, white, orange, orange, brown, brown,runeGluttony, pixieDust);
		gourmaryllisRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_GOURMARYLLIS), lightGray, lightGray, yellow, yellow, red, runeFire, runeSummer);
		narslimmusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_NARSLIMMUS), lime, lime, green, green, black, runeSummer, runeWater);
		spectrolusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SPECTROLUS), red, red, green, green, blue, blue, white, white, runeWinter, runeAir, pixieDust);
		rafflowsiaRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_RAFFLOWSIA), purple, purple, green, green, black, runeEarth, runePride, pixieDust);
		shulkMeNotRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SHULK_ME_NOT), purple, purple, magenta, magenta, lightGray, gaiaSpirit, runeEnvy, runeWrath);
		dandelifeonRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DANDELIFEON), purple, purple, lime, green, runeWater, runeFire, runeEarth, runeAir, gaiaSpirit);

		jadedAmaranthusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_JADED_AMARANTHUS), purple, lime, green, runeSpring, redstoneRoot);
		bellethorneRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_BELLETHORN), red, red, red, cyan, cyan, redstoneRoot);
		dreadthorneRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DREADTHORN), black, black, black, cyan, cyan, redstoneRoot);
		heiseiDreamRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HEISEI_DREAM), magenta, magenta, purple, pink, runeWrath, pixieDust);
		tigerseyeRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_TIGERSEYE), yellow, brown, orange, lime, runeAutumn);

		if(Botania.gardenOfGlassLoaded)
			orechidRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID), gray, gray, yellow, yellow, green, green, red, red);
		else orechidRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID), gray, gray, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust);

		orechidIgnemRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID_IGNEM), red, red, white, white, pink, runePride, runeGreed, redstoneRoot, pixieDust);
		if(ConfigHandler.fallenKanadeEnabled)
			fallenKanadeRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_FALLEN_KANADE), white, white, yellow, yellow, orange, runeSpring);
		exoflameRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_EXOFLAME), red, red, gray, lightGray, runeFire, runeSummer);
		agricarnationRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_AGRICARNATION), lime, lime, green, yellow, runeSpring, redstoneRoot);
		hopperhockRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HOPPERHOCK), gray, gray, lightGray, lightGray, runeAir, redstoneRoot);
		tangleberrieRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_TANGLEBERRIE), cyan, cyan, gray, lightGray, runeAir, runeEarth);
		jiyuuliaRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_JIYUULIA), pink, pink, purple, lightGray, runeWater, runeAir);
		rannuncarpusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_RANNUNCARPUS), orange, orange, yellow, runeEarth, redstoneRoot);
		hyacidusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYACIDUS), purple, purple, magenta, magenta, green, runeWater, runeAutumn, redstoneRoot);
		pollidisiacRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_POLLIDISIAC), red, red, pink, pink, orange, runeLust, runeFire);
		clayconiaRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_CLAYCONIA), lightGray, lightGray, gray, cyan, runeEarth);
		looniumRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_LOONIUM), green, green, green, green, gray, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust);
		daffomillRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DAFFOMILL), white, white, brown, yellow, runeAir, redstoneRoot);
		vinculotusRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_VINCULOTUS), black, black, purple, purple, green, runeWater, runeSloth, runeLust, redstoneRoot);
		spectranthemumRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SPECTRANTHEMUM), white, white, lightGray, lightGray, cyan, runeEnvy, runeWater, redstoneRoot, pixieDust);
		medumoneRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_MEDUMONE), brown, brown, gray, gray, runeEarth, redstoneRoot);
		marimorphosisRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_MARIMORPHOSIS), gray, yellow, green, red, runeEarth, runeFire, redstoneRoot);
		bubbellRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_BUBBELL), cyan, cyan, lightBlue, lightBlue, blue, blue, runeWater, runeSummer, pixieDust);
		solegnoliaRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SOLEGNOLIA), brown, brown, red, blue, redstoneRoot);
		bergamuteRecipe = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_BERGAMUTE), orange, green, green, redstoneRoot);

		ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
		ItemNBTHelper.setString(stack, "SkullOwner", "Vazkii");
		Object[] inputs = new Object[16];
		Arrays.fill(inputs, pink);
		BotaniaAPI.registerPetalRecipe(stack, inputs);
	}
}
