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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RunicAltarProvider extends BotaniaRecipeProvider {
	public RunicAltarProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "Botania runic altar recipes";
	}

	@Override
	public void registerRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;

		Ingredient manaSteel = Ingredient.of(BotaniaTags.Items.INGOTS_MANASTEEL);
		Ingredient manaDiamond = Ingredient.of(BotaniaTags.Items.GEMS_MANA_DIAMOND);
		Ingredient manaPowder = Ingredient.of(BotaniaTags.Items.DUSTS_MANA);
		consumer.accept(new FinishedRecipe(idFor("water"), new ItemStack(BotaniaItems.runeWater, 2), costTier1, manaPowder, manaSteel, Ingredient.of(Items.BONE_MEAL), Ingredient.of(Blocks.SUGAR_CANE), Ingredient.of(Items.FISHING_ROD)));
		consumer.accept(new FinishedRecipe(idFor("fire"), new ItemStack(BotaniaItems.runeFire, 2), costTier1, manaPowder, manaSteel, Ingredient.of(Items.NETHER_BRICK), Ingredient.of(Items.GUNPOWDER), Ingredient.of(Items.NETHER_WART)));

		Ingredient stone = Ingredient.of(Blocks.STONE);
		Ingredient coalBlock = Ingredient.of(Blocks.COAL_BLOCK);
		consumer.accept(new FinishedRecipe(idFor("earth"), new ItemStack(BotaniaItems.runeEarth, 2), costTier1, manaPowder, manaSteel, stone, coalBlock, Ingredient.of(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM)));

		consumer.accept(new FinishedRecipe(idFor("air"), new ItemStack(BotaniaItems.runeAir, 2), costTier1, manaPowder, manaSteel, Ingredient.of(ItemTags.WOOL_CARPETS), Ingredient.of(Items.FEATHER), Ingredient.of(Items.STRING)));

		Ingredient fire = Ingredient.of(BotaniaItems.runeFire);
		Ingredient water = Ingredient.of(BotaniaItems.runeWater);
		Ingredient earth = Ingredient.of(BotaniaItems.runeEarth);
		Ingredient air = Ingredient.of(BotaniaItems.runeAir);

		Ingredient sapling = Ingredient.of(ItemTags.SAPLINGS);
		Ingredient leaves = Ingredient.of(ItemTags.LEAVES);
		Ingredient sand = Ingredient.of(ItemTags.SAND);
		consumer.accept(new FinishedRecipe(idFor("spring"), new ItemStack(BotaniaItems.runeSpring), costTier2, water, fire, sapling, sapling, sapling, Ingredient.of(Items.WHEAT)));
		consumer.accept(new FinishedRecipe(idFor("summer"), new ItemStack(BotaniaItems.runeSummer), costTier2, earth, air, sand, sand, Ingredient.of(Items.SLIME_BALL), Ingredient.of(Items.MELON_SLICE)));
		consumer.accept(new FinishedRecipe(idFor("autumn"), new ItemStack(BotaniaItems.runeAutumn), costTier2, fire, air, leaves, leaves, leaves, Ingredient.of(Items.SPIDER_EYE)));

		consumer.accept(new FinishedRecipe(idFor("winter"), new ItemStack(BotaniaItems.runeWinter), costTier2, water, earth, Ingredient.of(Blocks.SNOW_BLOCK), Ingredient.of(Blocks.SNOW_BLOCK), Ingredient.of(ItemTags.WOOL), Ingredient.of(Blocks.CAKE)));

		Ingredient spring = Ingredient.of(BotaniaItems.runeSpring);
		Ingredient summer = Ingredient.of(BotaniaItems.runeSummer);
		Ingredient autumn = Ingredient.of(BotaniaItems.runeAutumn);
		Ingredient winter = Ingredient.of(BotaniaItems.runeWinter);

		consumer.accept(new FinishedRecipe(idFor("mana"), new ItemStack(BotaniaItems.runeMana), costTier2, manaSteel, manaSteel, manaSteel, manaSteel, manaSteel, Ingredient.of(BotaniaItems.manaPearl)));

		consumer.accept(new FinishedRecipe(idFor("lust"), new ItemStack(BotaniaItems.runeLust), costTier3, manaDiamond, manaDiamond, summer, air));
		consumer.accept(new FinishedRecipe(idFor("gluttony"), new ItemStack(BotaniaItems.runeGluttony), costTier3, manaDiamond, manaDiamond, winter, fire));
		consumer.accept(new FinishedRecipe(idFor("greed"), new ItemStack(BotaniaItems.runeGreed), costTier3, manaDiamond, manaDiamond, spring, water));
		consumer.accept(new FinishedRecipe(idFor("sloth"), new ItemStack(BotaniaItems.runeSloth), costTier3, manaDiamond, manaDiamond, autumn, air));
		consumer.accept(new FinishedRecipe(idFor("wrath"), new ItemStack(BotaniaItems.runeWrath), costTier3, manaDiamond, manaDiamond, winter, earth));
		consumer.accept(new FinishedRecipe(idFor("envy"), new ItemStack(BotaniaItems.runeEnvy), costTier3, manaDiamond, manaDiamond, winter, water));
		consumer.accept(new FinishedRecipe(idFor("pride"), new ItemStack(BotaniaItems.runePride), costTier3, manaDiamond, manaDiamond, summer, fire));

		consumer.accept(new FinishedHeadRecipe(idFor("head"), new ItemStack(Items.PLAYER_HEAD), 22500, Ingredient.of(Items.SKELETON_SKULL), Ingredient.of(BotaniaItems.pixieDust), Ingredient.of(Items.PRISMARINE_CRYSTALS), Ingredient.of(Items.NAME_TAG), Ingredient.of(Items.GOLDEN_APPLE)));
	}

	private static ResourceLocation idFor(String s) {
		return prefix("runic_altar/" + s);
	}

	protected static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
		private final ResourceLocation id;
		private final ItemStack output;
		private final int mana;
		private final Ingredient[] inputs;

		protected FinishedRecipe(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
			this.id = id;
			this.output = output;
			this.mana = mana;
			this.inputs = inputs;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("output", ItemNBTHelper.serializeStack(output));
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.toJson());
			}
			json.addProperty("mana", mana);
			json.add("ingredients", ingredients);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return BotaniaRecipeTypes.RUNE_SERIALIZER;
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

	private static class FinishedHeadRecipe extends FinishedRecipe {
		private FinishedHeadRecipe(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
			super(id, output, mana, inputs);
		}

		@Override
		public RecipeSerializer<?> getType() {
			return BotaniaRecipeTypes.RUNE_HEAD_SERIALIZER;
		}
	}
}
