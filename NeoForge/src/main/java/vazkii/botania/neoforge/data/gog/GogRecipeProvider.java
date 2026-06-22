/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.data.gog;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.flower.functional.OrechidBlockEntity;
import vazkii.botania.common.crafting.ManaInfusionRecipe;
import vazkii.botania.common.crafting.OrechidRecipe;
import vazkii.botania.common.crafting.PetalApothecaryRecipe;
import vazkii.botania.common.crafting.StateIngredients;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.ConventionalBotaniaTags;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.gogRL;

public class GogRecipeProvider extends RecipeProvider {

	public static final vazkii.botania.api.recipe.StateIngredient ALCHEMY_CATALYST =
			StateIngredients.of(BotaniaBlocks.ALCHEMY_CATALYST);

	public GogRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		// modifications of standard Botania recipes:
		nineBlockStorageRecipes(recipeOutput,
				RecipeCategory.BREWING, Items.BLAZE_POWDER,
				RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.BLAZE_MESH,
				"gardenofglass:blaze_mesh", null, "gardenofglass:blaze_powder_from_blaze_mesh", null);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.FLORAL_FERTILIZER, 3)
				.requires(Items.BONE_MEAL)
				.requires(Ingredient.of(Tags.Items.DYES), 4)
				.group("botania:floral_fertilizer")
				.unlockedBy(getHasName(Items.BONE_MEAL), has(Items.BONE_MEAL))
				.unlockedBy("has_any_dye", has(Tags.Items.DYES))
				.save(recipeOutput, gogRL("floral_fertilizer_from_dye"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaBlocks.COCOON_OF_CAPRICE)
				.define('S', Items.STRING)
				.define('P', BotaniaBlocks.FEL_PUMPKIN)
				.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SIS")
				.unlockedBy("has_item", has(BotaniaBlocks.FEL_PUMPKIN))
				.save(recipeOutput, gogRL(getItemName(BotaniaBlocks.COCOON_OF_CAPRICE)));
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.MANA_SPREADER)
				.define('P', BotaniaTags.Items.PETALS)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern("WWW")
				.pattern("WP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_item", has(BotaniaTags.Items.LIVINGWOOD_LOGS))
				.save(recipeOutput, gogRL(getItemName(BotaniaBlocks.MANA_SPREADER)));

		Ingredient yellow = Ingredient.of(BotaniaTags.Items.PETALS_YELLOW);
		Ingredient gray = Ingredient.of(BotaniaTags.Items.PETALS_GRAY);
		Ingredient green = Ingredient.of(BotaniaTags.Items.PETALS_GREEN);
		Ingredient red = Ingredient.of(BotaniaTags.Items.PETALS_RED);
		recipeOutput.accept(
				gogRL("petal_apothecary/" + getItemName(BotaniaBlocks.ORECHID.asItem())),
				new PetalApothecaryRecipe(new ItemStack(BotaniaBlocks.ORECHID),
						Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT),
						gray, gray, yellow, yellow, green, green, red, red),
				null);

		// GoG-specific recipes
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.END_PORTAL_FRAME)
				.define('G', BotaniaItems.GAIA_SPIRIT)
				.define('O', Blocks.OBSIDIAN)
				.pattern("OGO")
				.unlockedBy("has_gaia_spirit", has(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput, gogRL(getItemName(Blocks.END_PORTAL_FRAME)));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SLIME_BALL)
				.requires(Items.MAGMA_CREAM)
				.requires(Items.WATER_BUCKET)
				.unlockedBy(getHasName(Items.MAGMA_CREAM), has(Items.MAGMA_CREAM))
				.save(recipeOutput, gogRL(getConversionRecipeName(Items.SLIME_BALL, Items.MAGMA_CREAM)));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE)
				.define('#', BotaniaItems.PEBBLE)
				.pattern("##")
				.pattern("##")
				.unlockedBy(getHasName(BotaniaItems.PEBBLE), has(BotaniaItems.PEBBLE))
				.save(recipeOutput, gogRL(getConversionRecipeName(Blocks.COBBLESTONE, BotaniaItems.PEBBLE)));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.FLORAL_FERTILIZER, 1)
				.requires(BotaniaItems.LIVING_ROOT)
				.group("botania:floral_fertilizer")
				.unlockedBy(getHasName(BotaniaItems.LIVING_ROOT), has(BotaniaItems.LIVING_ROOT))
				.save(recipeOutput, gogRL(getConversionRecipeName(BotaniaItems.FLORAL_FERTILIZER, BotaniaItems.LIVING_ROOT)));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.OAK_SAPLING)
				.define('#', BotaniaItems.LIVING_ROOT)
				.pattern("##")
				.pattern("##")
				.unlockedBy(getHasName(BotaniaItems.LIVING_ROOT), has(BotaniaItems.LIVING_ROOT))
				.save(recipeOutput, gogRL(getConversionRecipeName(Blocks.OAK_SAPLING, BotaniaItems.LIVING_ROOT)));

		manaInfusion(recipeOutput, Items.HEART_OF_THE_SEA, Items.NAUTILUS_SHELL, 20000, ALCHEMY_CATALYST);
		manaInfusion(recipeOutput, Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_SHARD, 500, ALCHEMY_CATALYST);
		manaInfusion(recipeOutput, Items.PRISMARINE_SHARD, Items.QUARTZ, 1000, ALCHEMY_CATALYST);
		manaInfusion(recipeOutput, Blocks.SUGAR_CANE, Blocks.HAY_BLOCK, 2000, null);

		// no biome bonus, but weights shifted towards useful materials
		stone(recipeOutput, Blocks.COAL_ORE, 5748);
		stone(recipeOutput, Blocks.IRON_ORE, 1568);
		stone(recipeOutput, Blocks.REDSTONE_ORE, 271);
		stone(recipeOutput, Blocks.COPPER_ORE, 2030);
		stone(recipeOutput, Blocks.GOLD_ORE, 106);
		stone(recipeOutput, Blocks.EMERALD_ORE, 10);
		stone(recipeOutput, Blocks.LAPIS_ORE, 81);
		stone(recipeOutput, Blocks.DIAMOND_ORE, 75);
	}

	private static void manaInfusion(RecipeOutput recipeOutput, ItemLike resultItem, ItemLike ingredient, int mana,
			@Nullable StateIngredient catalyst) {
		recipeOutput.accept(gogRL("mana_infusion/" + getItemName(resultItem)),
				new ManaInfusionRecipe(new ItemStack(resultItem), Ingredient.of(ingredient), mana, null, catalyst),
				null);
	}

	protected static StateIngredient forBlock(Block block) {
		return StateIngredients.of(block);
	}

	protected ResourceLocation orechidId(Block b) {
		return gogRL("orechid/" + BuiltInRegistries.BLOCK.getKey(b).getPath());
	}

	protected void stone(RecipeOutput consumer, Block output, int weight) {
		consumer.accept(orechidId(output), new OrechidRecipe(forBlock(Blocks.STONE), forBlock(output),
				OrechidBlockEntity.DELAY_GOG, OrechidBlockEntity.DEFAULT_COST_GOG, weight), null);
	}
}
