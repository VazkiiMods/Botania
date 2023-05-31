/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.Arrays;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PetalApothecaryProvider extends BotaniaRecipeProvider {
	private static final Ingredient DEFAULT_REAGENT = Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT);

	public PetalApothecaryProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	public String getName() {
		return "Botania petal apothecary recipes";
	}

	@Override
	public void buildRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
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
		Ingredient runeWater = Ingredient.of(BotaniaItems.runeWater);
		Ingredient runeFire = Ingredient.of(BotaniaItems.runeFire);
		Ingredient runeEarth = Ingredient.of(BotaniaItems.runeEarth);
		Ingredient runeAir = Ingredient.of(BotaniaItems.runeAir);
		Ingredient runeSpring = Ingredient.of(BotaniaItems.runeSpring);
		Ingredient runeSummer = Ingredient.of(BotaniaItems.runeSummer);
		Ingredient runeAutumn = Ingredient.of(BotaniaItems.runeAutumn);
		Ingredient runeWinter = Ingredient.of(BotaniaItems.runeWinter);
		Ingredient runeMana = Ingredient.of(BotaniaItems.runeMana);
		Ingredient runeLust = Ingredient.of(BotaniaItems.runeLust);
		Ingredient runeGluttony = Ingredient.of(BotaniaItems.runeGluttony);
		Ingredient runeGreed = Ingredient.of(BotaniaItems.runeGreed);
		Ingredient runeSloth = Ingredient.of(BotaniaItems.runeSloth);
		Ingredient runeWrath = Ingredient.of(BotaniaItems.runeWrath);
		Ingredient runeEnvy = Ingredient.of(BotaniaItems.runeEnvy);
		Ingredient runePride = Ingredient.of(BotaniaItems.runePride);

		Ingredient redstoneRoot = Ingredient.of(BotaniaItems.redstoneRoot);
		Ingredient pixieDust = Ingredient.of(BotaniaItems.pixieDust);
		Ingredient gaiaSpirit = Ingredient.of(BotaniaItems.lifeEssence);

		consumer.accept(make(BotaniaFlowerBlocks.pureDaisy, white, white, white, white));
		consumer.accept(make(BotaniaFlowerBlocks.manastar, lightBlue, green, red, cyan));

		consumer.accept(make(BotaniaFlowerBlocks.endoflame, brown, brown, red, lightGray));
		consumer.accept(make(BotaniaFlowerBlocks.hydroangeas, blue, blue, cyan, cyan));
		consumer.accept(make(BotaniaFlowerBlocks.thermalily, red, orange, orange, runeEarth, runeFire));
		consumer.accept(make(BotaniaFlowerBlocks.rosaArcana, pink, pink, purple, purple, lime, runeMana));
		consumer.accept(make(BotaniaFlowerBlocks.munchdew, lime, lime, red, red, green, runeGluttony));
		consumer.accept(make(BotaniaFlowerBlocks.entropinnyum, red, red, gray, gray, white, white, runeWrath, runeFire));
		consumer.accept(make(BotaniaFlowerBlocks.kekimurus, white, white, orange, orange, brown, brown, runeGluttony, pixieDust));
		consumer.accept(make(BotaniaFlowerBlocks.gourmaryllis, lightGray, lightGray, yellow, yellow, red, runeFire, runeSummer));
		consumer.accept(make(BotaniaFlowerBlocks.narslimmus, lime, lime, green, green, black, runeSummer, runeWater));
		consumer.accept(make(BotaniaFlowerBlocks.spectrolus, red, red, green, green, blue, blue, white, white, runeWinter, runeAir, pixieDust));
		consumer.accept(make(BotaniaFlowerBlocks.rafflowsia, purple, purple, green, green, black, runeEarth, runePride, pixieDust));
		consumer.accept(make(BotaniaFlowerBlocks.shulkMeNot, purple, purple, magenta, magenta, lightGray, gaiaSpirit, runeEnvy, runeWrath));
		consumer.accept(make(BotaniaFlowerBlocks.dandelifeon, purple, purple, lime, green, runeWater, runeFire, runeEarth, runeAir, gaiaSpirit));

		consumer.accept(make(BotaniaFlowerBlocks.jadedAmaranthus, purple, lime, green, runeSpring, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.bellethorn, red, red, red, cyan, cyan, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.dreadthorn, black, black, black, cyan, cyan, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.heiseiDream, magenta, magenta, purple, pink, runeWrath, pixieDust));
		consumer.accept(make(BotaniaFlowerBlocks.tigerseye, yellow, brown, orange, lime, runeAutumn));

		net.minecraft.data.recipes.FinishedRecipe base = make(BotaniaFlowerBlocks.orechid, gray, gray, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust);
		net.minecraft.data.recipes.FinishedRecipe gog = make(BotaniaFlowerBlocks.orechid, gray, gray, yellow, yellow, green, green, red, red);
		consumer.accept(new GogAlternationResult(gog, base));

		consumer.accept(make(BotaniaFlowerBlocks.orechidIgnem, red, red, white, white, pink, runePride, runeGreed, redstoneRoot, pixieDust));
		consumer.accept(make(BotaniaFlowerBlocks.fallenKanade, white, white, yellow, yellow, orange, runeSpring));
		consumer.accept(make(BotaniaFlowerBlocks.exoflame, red, red, gray, lightGray, runeFire, runeSummer));
		consumer.accept(make(BotaniaFlowerBlocks.agricarnation, lime, lime, green, yellow, runeSpring, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.hopperhock, gray, gray, lightGray, lightGray, runeAir, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.tangleberrie, cyan, cyan, gray, lightGray, runeAir, runeEarth));
		consumer.accept(make(BotaniaFlowerBlocks.jiyuulia, pink, pink, purple, lightGray, runeWater, runeAir));
		consumer.accept(make(BotaniaFlowerBlocks.rannuncarpus, orange, orange, yellow, runeEarth, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.hyacidus, purple, purple, magenta, magenta, green, runeWater, runeAutumn, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.pollidisiac, red, red, pink, pink, orange, runeLust, runeFire));
		consumer.accept(make(BotaniaFlowerBlocks.clayconia, lightGray, lightGray, gray, cyan, runeEarth));
		consumer.accept(make(BotaniaFlowerBlocks.loonium, green, green, green, green, gray, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust));
		consumer.accept(make(BotaniaFlowerBlocks.daffomill, white, white, brown, yellow, runeAir, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.vinculotus, black, black, purple, purple, green, runeWater, runeSloth, runeLust, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.spectranthemum, white, white, lightGray, lightGray, cyan, runeEnvy, runeWater, redstoneRoot, pixieDust));
		consumer.accept(make(BotaniaFlowerBlocks.medumone, brown, brown, gray, gray, runeEarth, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.marimorphosis, gray, yellow, green, red, runeEarth, runeFire, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.bubbell, cyan, cyan, lightBlue, lightBlue, blue, blue, runeWater, runeSummer, pixieDust));
		consumer.accept(make(BotaniaFlowerBlocks.solegnolia, brown, brown, red, blue, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.bergamute, orange, green, green, redstoneRoot));
		consumer.accept(make(BotaniaFlowerBlocks.labellia, yellow, yellow, blue, white, black, runeAutumn, redstoneRoot, pixieDust));

		consumer.accept(make(BotaniaBlocks.motifDaybloom, yellow, yellow, orange, lightBlue));
		consumer.accept(make(BotaniaBlocks.motifNightshade, black, black, purple, gray));

		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		ItemNBTHelper.setString(stack, "SkullOwner", "Vazkii");
		Ingredient[] inputs = new Ingredient[16];
		Arrays.fill(inputs, pink);
		consumer.accept(new NbtOutputResult(
				new FinishedRecipe(idFor(prefix("vazkii_head")), stack, DEFAULT_REAGENT, inputs),
				stack.getTag()));
	}

	protected static Ingredient tagIngr(String tag) {
		return Ingredient.of(TagKey.create(Registries.ITEM, prefix(tag)));
	}

	protected static FinishedRecipe make(ItemLike item, Ingredient... ingredients) {
		return new FinishedRecipe(idFor(BuiltInRegistries.ITEM.getKey(item.asItem())),
				new ItemStack(item), DEFAULT_REAGENT, ingredients);
	}

	protected static ResourceLocation idFor(ResourceLocation name) {
		return new ResourceLocation(name.getNamespace(), "petal_apothecary/" + name.getPath());
	}

	protected static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
		private final ResourceLocation id;
		private final ItemStack output;
		private final Ingredient reagent;
		private final Ingredient[] inputs;

		private FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient reagent, Ingredient... inputs) {
			this.id = id;
			this.output = output;
			this.reagent = reagent;
			this.inputs = inputs;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("output", ItemNBTHelper.serializeStack(output));
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.toJson());
			}
			json.add("reagent", reagent.toJson());
			json.add("ingredients", ingredients);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return BotaniaRecipeTypes.PETAL_SERIALIZER;
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
