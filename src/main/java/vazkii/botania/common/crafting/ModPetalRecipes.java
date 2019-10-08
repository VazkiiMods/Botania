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

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import java.util.Arrays;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModPetalRecipes {
	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
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

		evt.apothecary().accept(make(ModSubtiles.pureDaisy, white, white, white, white));
		evt.apothecary().accept(make(ModSubtiles.manastar, lightBlue, green, red, cyan));

		evt.apothecary().accept(make(ModSubtiles.endoflame, brown, brown, red, lightGray));
		evt.apothecary().accept(make(ModSubtiles.hydroangeas, blue, blue, cyan, cyan));
		evt.apothecary().accept(make(ModSubtiles.thermalily, red, orange, orange, runeEarth, runeFire));
		evt.apothecary().accept(make(ModSubtiles.rosaArcana, pink, pink, purple, purple, lime, runeMana));
		evt.apothecary().accept(make(ModSubtiles.munchdew, lime, lime, red, red, green, runeGluttony));
		evt.apothecary().accept(make(ModSubtiles.entropinnyum, red, red, gray, gray, white, white, runeWrath, runeFire));
		evt.apothecary().accept(make(ModSubtiles.kekimurus, white, white, orange, orange, brown, brown,runeGluttony, pixieDust));
		evt.apothecary().accept(make(ModSubtiles.gourmaryllis, lightGray, lightGray, yellow, yellow, red, runeFire, runeSummer));
		evt.apothecary().accept(make(ModSubtiles.narslimmus, lime, lime, green, green, black, runeSummer, runeWater));
		evt.apothecary().accept(make(ModSubtiles.spectrolus, red, red, green, green, blue, blue, white, white, runeWinter, runeAir, pixieDust));
		evt.apothecary().accept(make(ModSubtiles.rafflowsia, purple, purple, green, green, black, runeEarth, runePride, pixieDust));
		evt.apothecary().accept(make(ModSubtiles.shulkMeNot, purple, purple, magenta, magenta, lightGray, gaiaSpirit, runeEnvy, runeWrath));
		evt.apothecary().accept(make(ModSubtiles.dandelifeon, purple, purple, lime, green, runeWater, runeFire, runeEarth, runeAir, gaiaSpirit));

		evt.apothecary().accept(make(ModSubtiles.jadedAmaranthus, purple, lime, green, runeSpring, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.bellethorn, red, red, red, cyan, cyan, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.dreadthorn, black, black, black, cyan, cyan, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.heiseiDream, magenta, magenta, purple, pink, runeWrath, pixieDust));
		evt.apothecary().accept(make(ModSubtiles.tigerseye, yellow, brown, orange, lime, runeAutumn));

		if(Botania.gardenOfGlassLoaded)
			evt.apothecary().accept(make(ModSubtiles.orechid, gray, gray, yellow, yellow, green, green, red, red));
		else evt.apothecary().accept(make(ModSubtiles.orechid, gray, gray, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust));

		evt.apothecary().accept(make(ModSubtiles.orechidIgnem, red, red, white, white, pink, runePride, runeGreed, redstoneRoot, pixieDust));
		if(ConfigHandler.COMMON.fallenKanadeEnabled.get())
			evt.apothecary().accept(make(ModSubtiles.fallenKanade, white, white, yellow, yellow, orange, runeSpring));
		evt.apothecary().accept(make(ModSubtiles.exoflame, red, red, gray, lightGray, runeFire, runeSummer));
		evt.apothecary().accept(make(ModSubtiles.agricarnation, lime, lime, green, yellow, runeSpring, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.hopperhock, gray, gray, lightGray, lightGray, runeAir, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.tangleberrie, cyan, cyan, gray, lightGray, runeAir, runeEarth));
		evt.apothecary().accept(make(ModSubtiles.jiyuulia, pink, pink, purple, lightGray, runeWater, runeAir));
		evt.apothecary().accept(make(ModSubtiles.rannuncarpus, orange, orange, yellow, runeEarth, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.hyacidus, purple, purple, magenta, magenta, green, runeWater, runeAutumn, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.pollidisiac, red, red, pink, pink, orange, runeLust, runeFire));
		evt.apothecary().accept(make(ModSubtiles.clayconia, lightGray, lightGray, gray, cyan, runeEarth));
		evt.apothecary().accept(make(ModSubtiles.loonium, green, green, green, green, gray, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust));
		evt.apothecary().accept(make(ModSubtiles.daffomill, white, white, brown, yellow, runeAir, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.vinculotus, black, black, purple, purple, green, runeWater, runeSloth, runeLust, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.spectranthemum, white, white, lightGray, lightGray, cyan, runeEnvy, runeWater, redstoneRoot, pixieDust));
		evt.apothecary().accept(make(ModSubtiles.medumone, brown, brown, gray, gray, runeEarth, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.marimorphosis, gray, yellow, green, red, runeEarth, runeFire, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.bubbell, cyan, cyan, lightBlue, lightBlue, blue, blue, runeWater, runeSummer, pixieDust));
		evt.apothecary().accept(make(ModSubtiles.solegnolia, brown, brown, red, blue, redstoneRoot));
		evt.apothecary().accept(make(ModSubtiles.bergamute, orange, green, green, redstoneRoot));

		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		ItemNBTHelper.setString(stack, "SkullOwner", "Vazkii");
		Ingredient[] inputs = new Ingredient[16];
		Arrays.fill(inputs, pink);
		evt.apothecary().accept(new RecipePetals(prefix("vazkii_head"), stack, inputs));
	}

	private static Ingredient tagIngr(String tag) {
		return Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, tag)));
	}

	private static RecipePetals make(IItemProvider item, Ingredient... ingredients) {
		return new RecipePetals(item.asItem().getRegistryName(), new ItemStack(item), ingredients);
	}
}
