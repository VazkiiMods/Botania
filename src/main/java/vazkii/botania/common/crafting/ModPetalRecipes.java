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

import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.Arrays;

public final class ModPetalRecipes {
	public static RecipePetals pureDaisyRecipe;
	public static RecipePetals manastarRecipe;

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
		Ingredient white = tagIngr("petals/white");
		Ingredient orange = tagIngr("petals/orange");
		Ingredient magenta = tagIngr("petals/magenta");
		Ingredient lightBlue = tagIngr("petals/light_blue");
		Ingredient yellow = tagIngr("petals/yellow");
		Ingredient lime = tagIngr("petals/lime");
		Ingredient pink = tagIngr("petals/pink");
		Ingredient gray = tagIngr("petals/gray");
		Ingredient lightGray = tagIngr("petals/light_gray");
		Ingredient cyan = tagIngr("petals/cyan");
		Ingredient purple = tagIngr("petals/purple");
		Ingredient blue = tagIngr("petals/blue");
		Ingredient brown = tagIngr("petals/brown");
		Ingredient green = tagIngr("petals/green");
		Ingredient red = tagIngr("petals/red");
		Ingredient black = tagIngr("petals/black");
		Ingredient runeWater = tagIngr("runes/water");
		Ingredient runeFire = tagIngr("runes/fire");
		Ingredient runeEarth = tagIngr("runes/earth");
		Ingredient runeAir = tagIngr("runes/air");
		Ingredient runeSpring = tagIngr("runes/spring");
		Ingredient runeSummer = tagIngr("runes/summer");
		Ingredient runeAutumn = tagIngr("runes/autumn");
		Ingredient runeWinter = tagIngr("runes/winter");
		Ingredient runeMana = tagIngr("runes/mana");
		Ingredient runeLust = tagIngr("runes/lust");
		Ingredient runeGluttony = tagIngr("runes/gluttony");
		Ingredient runeGreed = tagIngr("runes/greed");
		Ingredient runeSloth = tagIngr("runes/sloth");
		Ingredient runeWrath = tagIngr("runes/wrath");
		Ingredient runeEnvy = tagIngr("runes/envy");
		Ingredient runePride = tagIngr("runes/pride");

		Ingredient redstoneRoot = Ingredient.fromItems(ModItems.redstoneRoot);
		Ingredient pixieDust = Ingredient.fromItems(ModItems.pixieDust);
		Ingredient gaiaSpirit = Ingredient.fromItems(ModItems.lifeEssence);

		pureDaisyRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.pureDaisy), white, white, white, white);
		manastarRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.manastar), lightBlue, green, red, cyan);

		endoflameRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.endoflame), brown, brown, red, lightGray);
		hydroangeasRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.hydroangeas), blue, blue, cyan, cyan);
		thermalilyRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.thermalily), red, orange, orange, runeEarth, runeFire);
		arcaneRoseRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.rosaArcana), pink, pink, purple, purple, lime, runeMana);
		munchdewRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.munchdew), lime, lime, red, red, green, runeGluttony);
		entropinnyumRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.entropinnyum), red, red, gray, gray, white, white, runeWrath, runeFire);
		kekimurusRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.kekimurus), white, white, orange, orange, brown, brown,runeGluttony, pixieDust);
		gourmaryllisRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.gourmaryllis), lightGray, lightGray, yellow, yellow, red, runeFire, runeSummer);
		narslimmusRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.narslimmus), lime, lime, green, green, black, runeSummer, runeWater);
		spectrolusRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.spectrolus), red, red, green, green, blue, blue, white, white, runeWinter, runeAir, pixieDust);
		rafflowsiaRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.rafflowsia), purple, purple, green, green, black, runeEarth, runePride, pixieDust);
		shulkMeNotRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.shulkMeNot), purple, purple, magenta, magenta, lightGray, gaiaSpirit, runeEnvy, runeWrath);
		dandelifeonRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.dandelifeon), purple, purple, lime, green, runeWater, runeFire, runeEarth, runeAir, gaiaSpirit);

		jadedAmaranthusRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.jadedAmaranthus), purple, lime, green, runeSpring, redstoneRoot);
		bellethorneRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.bellethorn), red, red, red, cyan, cyan, redstoneRoot);
		dreadthorneRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.dreadthorn), black, black, black, cyan, cyan, redstoneRoot);
		heiseiDreamRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.heiseiDream), magenta, magenta, purple, pink, runeWrath, pixieDust);
		tigerseyeRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.tigerseye), yellow, brown, orange, lime, runeAutumn);

		if(Botania.gardenOfGlassLoaded)
			orechidRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.orechid), gray, gray, yellow, yellow, green, green, red, red);
		else orechidRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.orechid), gray, gray, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust);

		orechidIgnemRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.orechidIgnem), red, red, white, white, pink, runePride, runeGreed, redstoneRoot, pixieDust);
		if(ConfigHandler.COMMON.fallenKanadeEnabled.get())
			fallenKanadeRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.fallenKanade), white, white, yellow, yellow, orange, runeSpring);
		exoflameRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.exoflame), red, red, gray, lightGray, runeFire, runeSummer);
		agricarnationRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.agricarnation), lime, lime, green, yellow, runeSpring, redstoneRoot);
		hopperhockRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.hopperhock), gray, gray, lightGray, lightGray, runeAir, redstoneRoot);
		tangleberrieRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.tangleberrie), cyan, cyan, gray, lightGray, runeAir, runeEarth);
		jiyuuliaRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.jiyuulia), pink, pink, purple, lightGray, runeWater, runeAir);
		rannuncarpusRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.rannuncarpus), orange, orange, yellow, runeEarth, redstoneRoot);
		hyacidusRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.hyacidus), purple, purple, magenta, magenta, green, runeWater, runeAutumn, redstoneRoot);
		pollidisiacRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.pollidisiac), red, red, pink, pink, orange, runeLust, runeFire);
		clayconiaRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.clayconia), lightGray, lightGray, gray, cyan, runeEarth);
		looniumRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.loonium), green, green, green, green, gray, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust);
		daffomillRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.daffomill), white, white, brown, yellow, runeAir, redstoneRoot);
		vinculotusRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.vinculotus), black, black, purple, purple, green, runeWater, runeSloth, runeLust, redstoneRoot);
		spectranthemumRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.spectranthemum), white, white, lightGray, lightGray, cyan, runeEnvy, runeWater, redstoneRoot, pixieDust);
		medumoneRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.medumone), brown, brown, gray, gray, runeEarth, redstoneRoot);
		marimorphosisRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.marimorphosis), gray, yellow, green, red, runeEarth, runeFire, redstoneRoot);
		bubbellRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.bubbell), cyan, cyan, lightBlue, lightBlue, blue, blue, runeWater, runeSummer, pixieDust);
		solegnoliaRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.solegnolia), brown, brown, red, blue, redstoneRoot);
		bergamuteRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(ModSubtiles.bergamute), orange, green, green, redstoneRoot);

		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		ItemNBTHelper.setString(stack, "SkullOwner", "Vazkii");
		Ingredient[] inputs = new Ingredient[16];
		Arrays.fill(inputs, pink);
		BotaniaAPI.registerPetalRecipe(stack, inputs);
	}

	private static Ingredient tagIngr(String tag) {
		return Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, tag)));
	}
}
