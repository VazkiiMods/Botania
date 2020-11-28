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

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BrewProvider extends RecipesProvider implements BotaniaRecipeProvider {
	public BrewProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "Botania Brew recipes";
	}

	@Override
	public void registerRecipes(Consumer<RecipeJsonProvider> consumer) {
		consumer.accept(new FinishedRecipe(idFor("speed"), ModBrews.speed, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.SUGAR), Ingredient.ofItems(Items.REDSTONE)));
		consumer.accept(new FinishedRecipe(idFor("strength"), ModBrews.strength, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.BLAZE_POWDER), Ingredient.ofItems(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("haste"), ModBrews.haste, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.SUGAR), Ingredient.ofItems(Items.GOLD_NUGGET)));
		consumer.accept(new FinishedRecipe(idFor("healing"), ModBrews.healing, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.GLISTERING_MELON_SLICE), Ingredient.ofItems(Items.POTATO)));
		consumer.accept(new FinishedRecipe(idFor("jump_boost"), ModBrews.jumpBoost, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.FEATHER), Ingredient.ofItems(Items.CARROT)));
		consumer.accept(new FinishedRecipe(idFor("regeneration"), ModBrews.regen, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.GHAST_TEAR), Ingredient.ofItems(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("weak_regeneration"), ModBrews.regenWeak, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.GHAST_TEAR), Ingredient.ofItems(Items.REDSTONE)));
		consumer.accept(new FinishedRecipe(idFor("resistance"), ModBrews.resistance, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.IRON_INGOT), Ingredient.ofItems(Items.LEATHER)));
		consumer.accept(new FinishedRecipe(idFor("fire_resistance"), ModBrews.fireResistance, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.MAGMA_CREAM), Ingredient.ofItems(Blocks.NETHERRACK)));
		consumer.accept(new FinishedRecipe(idFor("water_breathing"), ModBrews.waterBreathing, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.PRISMARINE_CRYSTALS), Ingredient.ofItems(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("invisibility"), ModBrews.invisibility, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.SNOWBALL), Ingredient.ofItems(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("night_vision"), ModBrews.nightVision, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.SPIDER_EYE), Ingredient.ofItems(Items.GOLDEN_CARROT)));
		consumer.accept(new FinishedRecipe(idFor("absorption"), ModBrews.absorption, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.GOLDEN_APPLE), Ingredient.ofItems(Items.POTATO)));

		consumer.accept(new FinishedRecipe(idFor("overload"), ModBrews.overload, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.BLAZE_POWDER), Ingredient.ofItems(Items.SUGAR), Ingredient.ofItems(Items.GLOWSTONE_DUST), Ingredient.ofItems(ModItems.manaSteel), Ingredient.ofItems(Items.SPIDER_EYE)));
		consumer.accept(new FinishedRecipe(idFor("soul_cross"), ModBrews.soulCross, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Blocks.SOUL_SAND), Ingredient.ofItems(Items.PAPER), Ingredient.ofItems(Items.APPLE), Ingredient.ofItems(Items.BONE)));
		consumer.accept(new FinishedRecipe(idFor("feather_feet"), ModBrews.featherfeet, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.FEATHER), Ingredient.ofItems(Items.LEATHER), Ingredient.fromTag(ItemTags.WOOL)));
		consumer.accept(new FinishedRecipe(idFor("emptiness"), ModBrews.emptiness, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.GUNPOWDER), Ingredient.ofItems(Items.ROTTEN_FLESH), Ingredient.ofItems(Items.BONE), Ingredient.ofItems(Items.STRING), Ingredient.ofItems(Items.ENDER_PEARL)));
		consumer.accept(new FinishedRecipe(idFor("bloodthirst"), ModBrews.bloodthirst, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.FERMENTED_SPIDER_EYE), Ingredient.ofItems(Items.LAPIS_LAZULI), Ingredient.ofItems(Items.FIRE_CHARGE), Ingredient.ofItems(Items.IRON_INGOT)));
		consumer.accept(new FinishedRecipe(idFor("allure"), ModBrews.allure, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.COD), Ingredient.ofItems(Items.QUARTZ), Ingredient.ofItems(Items.GOLDEN_CARROT)));
		consumer.accept(new FinishedRecipe(idFor("clear"), ModBrews.clear, Ingredient.ofItems(Items.NETHER_WART), Ingredient.ofItems(Items.QUARTZ), Ingredient.ofItems(Items.EMERALD), Ingredient.ofItems(Items.MELON_SLICE)));
	}

	private static Identifier idFor(String s) {
		return prefix("brew/" + s);
	}

	private static class FinishedRecipe implements RecipeJsonProvider {
		private final Identifier id;
		private final Brew brew;
		private final Ingredient[] inputs;

		private FinishedRecipe(Identifier id, Brew brew, Ingredient... inputs) {
			this.id = id;
			this.brew = brew;
			this.inputs = inputs;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty("brew", BotaniaAPI.instance().getBrewRegistry().getId(brew).toString());
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.toJson());
			}
			json.add("ingredients", ingredients);
		}

		@Override
		public Identifier getRecipeId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getSerializer() {
			return ModRecipeTypes.BREW_SERIALIZER;
		}

		@Nullable
		@Override
		public JsonObject toAdvancementJson() {
			return null;
		}

		@Nullable
		@Override
		public Identifier getAdvancementId() {
			return null;
		}
	}
}
