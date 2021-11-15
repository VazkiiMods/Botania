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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nullable;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RuneProvider extends RecipeProvider {
	public RuneProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "Botania runic altar recipes";
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;

		Ingredient manaSteel = Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL);
		Ingredient manaDiamond = Ingredient.fromTag(ModTags.Items.GEMS_MANA_DIAMOND);
		Ingredient manaPowder = Ingredient.fromTag(ModTags.Items.DUSTS_MANA);
		consumer.accept(new FinishedRecipe(idFor("water"), new ItemStack(ModItems.runeWater, 2), costTier1, manaPowder, manaSteel, Ingredient.fromItems(Items.BONE_MEAL), Ingredient.fromItems(Blocks.SUGAR_CANE), Ingredient.fromItems(Items.FISHING_ROD)));
		consumer.accept(new FinishedRecipe(idFor("fire"), new ItemStack(ModItems.runeFire, 2), costTier1, manaPowder, manaSteel, Ingredient.fromItems(Items.NETHER_BRICK), Ingredient.fromItems(Items.GUNPOWDER), Ingredient.fromItems(Items.NETHER_WART)));

		Ingredient stone = Ingredient.fromTag(Tags.Items.STONE);
		Ingredient coalBlock = Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_COAL);
		consumer.accept(new FinishedRecipe(idFor("earth"), new ItemStack(ModItems.runeEarth, 2), costTier1, manaPowder, manaSteel, stone, coalBlock, Ingredient.fromItems(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM)));

		consumer.accept(new FinishedRecipe(idFor("air"), new ItemStack(ModItems.runeAir, 2), costTier1, manaPowder, manaSteel, Ingredient.fromTag(ItemTags.CARPETS), Ingredient.fromItems(Items.FEATHER), Ingredient.fromItems(Items.STRING)));

		Ingredient fire = Ingredient.fromTag(ModTags.Items.RUNES_FIRE);
		Ingredient water = Ingredient.fromTag(ModTags.Items.RUNES_WATER);
		Ingredient earth = Ingredient.fromTag(ModTags.Items.RUNES_EARTH);
		Ingredient air = Ingredient.fromTag(ModTags.Items.RUNES_AIR);

		Ingredient sapling = Ingredient.fromTag(ItemTags.SAPLINGS);
		Ingredient leaves = Ingredient.fromTag(ItemTags.LEAVES);
		Ingredient sand = Ingredient.fromTag(ItemTags.SAND);
		consumer.accept(new FinishedRecipe(idFor("spring"), new ItemStack(ModItems.runeSpring), costTier2, water, fire, sapling, sapling, sapling, Ingredient.fromItems(Items.WHEAT)));
		consumer.accept(new FinishedRecipe(idFor("summer"), new ItemStack(ModItems.runeSummer), costTier2, earth, air, sand, sand, Ingredient.fromItems(Items.SLIME_BALL), Ingredient.fromItems(Items.MELON_SLICE)));
		consumer.accept(new FinishedRecipe(idFor("autumn"), new ItemStack(ModItems.runeAutumn), costTier2, fire, air, leaves, leaves, leaves, Ingredient.fromItems(Items.SPIDER_EYE)));

		consumer.accept(new FinishedRecipe(idFor("winter"), new ItemStack(ModItems.runeWinter), costTier2, water, earth, Ingredient.fromItems(Blocks.SNOW_BLOCK), Ingredient.fromItems(Blocks.SNOW_BLOCK), Ingredient.fromTag(ItemTags.WOOL), Ingredient.fromItems(Blocks.CAKE)));

		Ingredient spring = Ingredient.fromTag(ModTags.Items.RUNES_SPRING);
		Ingredient summer = Ingredient.fromTag(ModTags.Items.RUNES_SUMMER);
		Ingredient autumn = Ingredient.fromTag(ModTags.Items.RUNES_AUTUMN);
		Ingredient winter = Ingredient.fromTag(ModTags.Items.RUNES_WINTER);

		consumer.accept(new FinishedRecipe(idFor("mana"), new ItemStack(ModItems.runeMana), costTier2, manaSteel, manaSteel, manaSteel, manaSteel, manaSteel, Ingredient.fromItems(ModItems.manaPearl)));

		consumer.accept(new FinishedRecipe(idFor("lust"), new ItemStack(ModItems.runeLust), costTier3, manaDiamond, manaDiamond, summer, air));
		consumer.accept(new FinishedRecipe(idFor("gluttony"), new ItemStack(ModItems.runeGluttony), costTier3, manaDiamond, manaDiamond, winter, fire));
		consumer.accept(new FinishedRecipe(idFor("greed"), new ItemStack(ModItems.runeGreed), costTier3, manaDiamond, manaDiamond, spring, water));
		consumer.accept(new FinishedRecipe(idFor("sloth"), new ItemStack(ModItems.runeSloth), costTier3, manaDiamond, manaDiamond, autumn, air));
		consumer.accept(new FinishedRecipe(idFor("wrath"), new ItemStack(ModItems.runeWrath), costTier3, manaDiamond, manaDiamond, winter, earth));
		consumer.accept(new FinishedRecipe(idFor("envy"), new ItemStack(ModItems.runeEnvy), costTier3, manaDiamond, manaDiamond, winter, water));
		consumer.accept(new FinishedRecipe(idFor("pride"), new ItemStack(ModItems.runePride), costTier3, manaDiamond, manaDiamond, summer, fire));

		consumer.accept(new FinishedHeadRecipe(idFor("head"), new ItemStack(Items.PLAYER_HEAD), 22500, Ingredient.fromItems(Items.SKELETON_SKULL), Ingredient.fromItems(ModItems.pixieDust), Ingredient.fromItems(Items.PRISMARINE_CRYSTALS), Ingredient.fromItems(Items.NAME_TAG), Ingredient.fromItems(Items.GOLDEN_APPLE)));
	}

	private static ResourceLocation idFor(String s) {
		return prefix("runic_altar/" + s);
	}

	protected static class FinishedRecipe implements IFinishedRecipe {
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
		public void serialize(JsonObject json) {
			json.add("output", ItemNBTHelper.serializeStack(output));
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.serialize());
			}
			json.addProperty("mana", mana);
			json.add("ingredients", ingredients);
		}

		@Override
		public ResourceLocation getID() {
			return id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ModRecipeTypes.RUNE_SERIALIZER;
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

	private static class FinishedHeadRecipe extends FinishedRecipe {
		private FinishedHeadRecipe(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
			super(id, output, mana, inputs);
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ModRecipeTypes.RUNE_HEAD_SERIALIZER;
		}
	}
}
