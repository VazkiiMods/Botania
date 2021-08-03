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

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BrewProvider extends BotaniaRecipeProvider {
	public BrewProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "Botania Brew recipes";
	}

	@Override
	public void registerRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
		consumer.accept(new FinishedRecipe(idFor("speed"), ModBrews.speed, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.SUGAR), Ingredient.of(Items.REDSTONE)));
		consumer.accept(new FinishedRecipe(idFor("strength"), ModBrews.strength, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("haste"), ModBrews.haste, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.SUGAR), Ingredient.of(Items.GOLD_NUGGET)));
		consumer.accept(new FinishedRecipe(idFor("healing"), ModBrews.healing, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GLISTERING_MELON_SLICE), Ingredient.of(Items.POTATO)));
		consumer.accept(new FinishedRecipe(idFor("jump_boost"), ModBrews.jumpBoost, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.FEATHER), Ingredient.of(Items.CARROT)));
		consumer.accept(new FinishedRecipe(idFor("regeneration"), ModBrews.regen, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GHAST_TEAR), Ingredient.of(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("weak_regeneration"), ModBrews.regenWeak, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GHAST_TEAR), Ingredient.of(Items.REDSTONE)));
		consumer.accept(new FinishedRecipe(idFor("resistance"), ModBrews.resistance, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.LEATHER)));
		consumer.accept(new FinishedRecipe(idFor("fire_resistance"), ModBrews.fireResistance, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.MAGMA_CREAM), Ingredient.of(Blocks.NETHERRACK)));
		consumer.accept(new FinishedRecipe(idFor("water_breathing"), ModBrews.waterBreathing, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.PRISMARINE_CRYSTALS), Ingredient.of(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("invisibility"), ModBrews.invisibility, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.SNOWBALL), Ingredient.of(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("night_vision"), ModBrews.nightVision, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.SPIDER_EYE), Ingredient.of(Items.GOLDEN_CARROT)));
		consumer.accept(new FinishedRecipe(idFor("absorption"), ModBrews.absorption, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GOLDEN_APPLE), Ingredient.of(Items.POTATO)));

		consumer.accept(new FinishedRecipe(idFor("overload"), ModBrews.overload, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.SUGAR), Ingredient.of(Items.GLOWSTONE_DUST), Ingredient.of(ModItems.manaSteel), Ingredient.of(Items.SPIDER_EYE)));
		consumer.accept(new FinishedRecipe(idFor("soul_cross"), ModBrews.soulCross, Ingredient.of(Items.NETHER_WART), Ingredient.of(Blocks.SOUL_SAND), Ingredient.of(Items.PAPER), Ingredient.of(Items.APPLE), Ingredient.of(Items.BONE)));
		consumer.accept(new FinishedRecipe(idFor("feather_feet"), ModBrews.featherfeet, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.FEATHER), Ingredient.of(Items.LEATHER), Ingredient.of(ItemTags.WOOL)));
		consumer.accept(new FinishedRecipe(idFor("emptiness"), ModBrews.emptiness, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GUNPOWDER), Ingredient.of(Items.ROTTEN_FLESH), Ingredient.of(Items.BONE), Ingredient.of(Items.STRING), Ingredient.of(Items.ENDER_PEARL)));
		consumer.accept(new FinishedRecipe(idFor("bloodthirst"), ModBrews.bloodthirst, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.FERMENTED_SPIDER_EYE), Ingredient.of(Items.LAPIS_LAZULI), Ingredient.of(Items.FIRE_CHARGE), Ingredient.of(Items.IRON_INGOT)));
		consumer.accept(new FinishedRecipe(idFor("allure"), ModBrews.allure, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.COD), Ingredient.of(Items.QUARTZ), Ingredient.of(Items.GOLDEN_CARROT)));
		consumer.accept(new FinishedRecipe(idFor("clear"), ModBrews.clear, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.QUARTZ), Ingredient.of(Items.EMERALD), Ingredient.of(Items.MELON_SLICE)));
	}

	private static ResourceLocation idFor(String s) {
		return prefix("brew/" + s);
	}

	private static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
		private final ResourceLocation id;
		private final Brew brew;
		private final Ingredient[] inputs;

		private FinishedRecipe(ResourceLocation id, Brew brew, Ingredient... inputs) {
			this.id = id;
			this.brew = brew;
			this.inputs = inputs;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty("brew", BotaniaAPI.instance().getBrewRegistry().getKey(brew).toString());
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.toJson());
			}
			json.add("ingredients", ingredients);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ModRecipeTypes.BREW_SERIALIZER;
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
