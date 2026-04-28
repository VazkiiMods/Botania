package vazkii.botania.neoforge.data.gog;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.ManaInfusionRecipe;
import vazkii.botania.common.crafting.PetalApothecaryRecipe;
import vazkii.botania.common.crafting.StateIngredients;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.ConventionalBotaniaTags;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.gogRL;

public class GogRecipeProvider extends RecipeProvider {

	public static final vazkii.botania.api.recipe.StateIngredient ALCHEMY_CATALYST =
			StateIngredients.of(BotaniaBlocks.alchemyCatalyst);

	public GogRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		// modifications of standard Botania recipes:
		nineBlockStorageRecipes(recipeOutput,
				RecipeCategory.BREWING, Items.BLAZE_POWDER,
				RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.blazeBlock,
				"gardenofglass:blaze_block", null, "gardenofglass:blaze_powder_from_blaze_block", null);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.fertilizer, 3)
				.requires(Items.BONE_MEAL)
				.requires(Ingredient.of(Tags.Items.DYES), 4)
				.group("botania:fertilizer")
				.unlockedBy(getHasName(Items.BONE_MEAL), has(Items.BONE_MEAL))
				.unlockedBy("has_any_dye", has(Tags.Items.DYES))
				.save(recipeOutput, gogRL("fertilizer_from_dye"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaBlocks.cocoon)
				.define('S', Items.STRING)
				.define('P', BotaniaBlocks.felPumpkin)
				.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SIS")
				.unlockedBy("has_item", has(BotaniaBlocks.felPumpkin))
				.save(recipeOutput, gogRL(getItemName(BotaniaBlocks.cocoon)));
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.manaSpreader)
				.define('P', BotaniaTags.Items.PETALS)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern("WWW")
				.pattern("WP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_item", has(BotaniaTags.Items.LIVINGWOOD_LOGS))
				.save(recipeOutput, gogRL(getItemName(BotaniaBlocks.manaSpreader)));

		Ingredient yellow = Ingredient.of(BotaniaTags.Items.PETALS_YELLOW);
		Ingredient gray = Ingredient.of(BotaniaTags.Items.PETALS_GRAY);
		Ingredient green = Ingredient.of(BotaniaTags.Items.PETALS_GREEN);
		Ingredient red = Ingredient.of(BotaniaTags.Items.PETALS_RED);
		recipeOutput.accept(
				gogRL("petal_apothecary/" + getItemName(BotaniaBlocks.orechid.asItem())),
				new PetalApothecaryRecipe(new ItemStack(BotaniaBlocks.orechid),
						Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT),
						gray, gray, yellow, yellow, green, green, red, red),
				null);

		// GoG-specific recipes
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.END_PORTAL_FRAME)
				.define('G', BotaniaItems.lifeEssence)
				.define('O', Blocks.OBSIDIAN)
				.pattern("OGO")
				.unlockedBy("has_gaia_spirit", has(BotaniaItems.lifeEssence))
				.save(recipeOutput, gogRL(getItemName(Blocks.END_PORTAL_FRAME)));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SLIME_BALL)
				.requires(Items.MAGMA_CREAM)
				.requires(Items.WATER_BUCKET)
				.unlockedBy(getHasName(Items.MAGMA_CREAM), has(Items.MAGMA_CREAM))
				.save(recipeOutput, gogRL(getConversionRecipeName(Items.SLIME_BALL, Items.MAGMA_CREAM)));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE)
				.define('#', BotaniaItems.pebble)
				.pattern("##")
				.pattern("##")
				.unlockedBy(getHasName(BotaniaItems.pebble), has(BotaniaItems.pebble))
				.save(recipeOutput, gogRL(getConversionRecipeName(Blocks.COBBLESTONE, BotaniaItems.pebble)));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.fertilizer, 1)
				.requires(BotaniaItems.livingroot)
				.group("botania:fertilizer")
				.unlockedBy(getHasName(BotaniaItems.livingroot), has(BotaniaItems.livingroot))
				.save(recipeOutput, gogRL(getConversionRecipeName(BotaniaItems.fertilizer, BotaniaItems.livingroot)));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.OAK_SAPLING)
				.define('#', BotaniaItems.livingroot)
				.pattern("##")
				.pattern("##")
				.unlockedBy(getHasName(BotaniaItems.livingroot), has(BotaniaItems.livingroot))
				.save(recipeOutput, gogRL(getConversionRecipeName(Blocks.OAK_SAPLING, BotaniaItems.livingroot)));

		manaInfusion(recipeOutput, Items.HEART_OF_THE_SEA, Items.NAUTILUS_SHELL, 20000, ALCHEMY_CATALYST);
		manaInfusion(recipeOutput, Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_SHARD, 500, ALCHEMY_CATALYST);
		manaInfusion(recipeOutput, Items.PRISMARINE_SHARD, Items.QUARTZ, 1000, ALCHEMY_CATALYST);
		manaInfusion(recipeOutput, Blocks.SUGAR_CANE, Blocks.HAY_BLOCK, 2000, null);
	}

	private static void manaInfusion(RecipeOutput recipeOutput, ItemLike resultItem, ItemLike ingredient, int mana,
			@Nullable StateIngredient catalyst) {
		recipeOutput.accept(gogRL("mana_infusion/" + getItemName(resultItem)),
				new ManaInfusionRecipe(new ItemStack(resultItem), Ingredient.of(ingredient), mana, null, catalyst),
				null);
	}
}
