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
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.Botania;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BrewProvider extends RecipeProvider {
	public BrewProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "Botania Brew recipes";
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		consumer.accept(new FinishedRecipe(idFor("speed"), ModBrews.speed, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.REDSTONE)));
		consumer.accept(new FinishedRecipe(idFor("strength"), ModBrews.strength, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.BLAZE_POWDER), Ingredient.fromItems(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("haste"), ModBrews.haste, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.GOLD_NUGGET)));
		consumer.accept(new FinishedRecipe(idFor("healing"), ModBrews.healing, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GLISTERING_MELON_SLICE), Ingredient.fromItems(Items.POTATO)));
		consumer.accept(new FinishedRecipe(idFor("jump_boost"), ModBrews.jumpBoost, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.FEATHER), Ingredient.fromItems(Items.CARROT)));
		consumer.accept(new FinishedRecipe(idFor("regeneration"), ModBrews.regen, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GHAST_TEAR), Ingredient.fromItems(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("weak_regeneration"), ModBrews.regenWeak, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GHAST_TEAR), Ingredient.fromItems(Items.REDSTONE)));
		consumer.accept(new FinishedRecipe(idFor("resistance"), ModBrews.resistance, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.IRON_INGOT), Ingredient.fromItems(Items.LEATHER)));
		consumer.accept(new FinishedRecipe(idFor("fire_resistance"), ModBrews.fireResistance, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.MAGMA_CREAM), Ingredient.fromItems(Blocks.NETHERRACK)));
		consumer.accept(new FinishedRecipe(idFor("water_breathing"), ModBrews.waterBreathing, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.PRISMARINE_CRYSTALS), Ingredient.fromItems(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("invisibility"), ModBrews.invisibility, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SNOWBALL), Ingredient.fromItems(Items.GLOWSTONE_DUST)));
		consumer.accept(new FinishedRecipe(idFor("night_vision"), ModBrews.nightVision, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SPIDER_EYE), Ingredient.fromItems(Items.GOLDEN_CARROT)));
		consumer.accept(new FinishedRecipe(idFor("absorption"), ModBrews.absorption, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GOLDEN_APPLE), Ingredient.fromItems(Items.POTATO)));

		consumer.accept(new FinishedRecipe(idFor("overload"), ModBrews.overload, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.BLAZE_POWDER), Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.GLOWSTONE_DUST), Ingredient.fromItems(ModItems.manaSteel), Ingredient.fromItems(Items.SPIDER_EYE)));
		consumer.accept(new FinishedRecipe(idFor("soul_cross"), ModBrews.soulCross, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Blocks.SOUL_SAND), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.APPLE), Ingredient.fromItems(Items.BONE)));
		consumer.accept(new FinishedRecipe(idFor("feather_feet"), ModBrews.featherfeet, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.FEATHER), Ingredient.fromItems(Items.LEATHER), Ingredient.fromTag(ItemTags.WOOL)));
		consumer.accept(new FinishedRecipe(idFor("emptiness"), ModBrews.emptiness, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GUNPOWDER), Ingredient.fromItems(Items.ROTTEN_FLESH), Ingredient.fromItems(Items.BONE), Ingredient.fromItems(Items.STRING), Ingredient.fromItems(Items.ENDER_PEARL)));
		consumer.accept(new FinishedRecipe(idFor("bloodthirst"), ModBrews.bloodthirst, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE), Ingredient.fromItems(Items.LAPIS_LAZULI), Ingredient.fromItems(Items.FIRE_CHARGE), Ingredient.fromItems(Items.IRON_INGOT)));
		consumer.accept(new FinishedRecipe(idFor("allure"), ModBrews.allure, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.COD), Ingredient.fromItems(Items.QUARTZ), Ingredient.fromItems(Items.GOLDEN_CARROT)));
		consumer.accept(new FinishedRecipe(idFor("clear"), ModBrews.clear, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.QUARTZ), Ingredient.fromItems(Items.EMERALD), Ingredient.fromItems(Items.MELON_SLICE)));

		// todo 1.15 conditional recipe or remove
		if (Botania.thaumcraftLoaded) {
			Item salisMundus = Registry.ITEM.getValue(new ResourceLocation("thaumcraft", "salis_mundus")).get();
			Item bathSalts = Registry.ITEM.getValue(new ResourceLocation("thaumcraft", "bath_salts")).get();
			Item amber = Registry.ITEM.getValue(new ResourceLocation("thaumcraft", "amber")).get();

			consumer.accept(new FinishedRecipe(idFor("warp_ward"), ModBrews.warpWard, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(salisMundus), Ingredient.fromItems(bathSalts), Ingredient.fromItems(amber)));
		}
	}

	private static ResourceLocation idFor(String s) {
		return prefix("brew/" + s);
	}

	private static class FinishedRecipe implements IFinishedRecipe {
		private final ResourceLocation id;
		private final Brew brew;
		private final Ingredient[] inputs;

		private FinishedRecipe(ResourceLocation id, Brew brew, Ingredient... inputs) {
			this.id = id;
			this.brew = brew;
			this.inputs = inputs;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty("brew", brew.getRegistryName().toString());
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.serialize());
			}
			json.add("ingredients", ingredients);
		}

		@Override
		public ResourceLocation getID() {
			return id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ModRecipeTypes.BREW_SERIALIZER;
		}

		@Nullable
		@Override
		public JsonObject getAdvancementJson() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementID() {
			return null;
		}
	}
}
