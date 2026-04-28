package vazkii.botania.neoforge.data.gog;

import net.minecraft.core.HolderLookup;
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
import net.neoforged.neoforge.common.Tags;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.PetalApothecaryRecipe;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.ConventionalBotaniaTags;

import java.util.concurrent.CompletableFuture;

public class GogRecipeProvider extends RecipeProvider {

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
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.fertilizer)
				.requires(Items.BONE_MEAL)
				.requires(Ingredient.of(Tags.Items.DYES), 4)
				.group("botania:fertilizer")
				.unlockedBy("has_bonemeal", has(Items.BONE_MEAL))
				.unlockedBy("has_any_dye", has(Tags.Items.DYES))
				.save(recipeOutput, BotaniaAPI.gogRL("fertilizer_from_dye"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaBlocks.cocoon)
				.define('S', Items.STRING)
				.define('P', BotaniaBlocks.felPumpkin)
				.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SIS")
				.unlockedBy("has_item", has(BotaniaBlocks.felPumpkin))
				.save(recipeOutput, BotaniaAPI.gogRL(getItemName(BotaniaBlocks.cocoon)));
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.manaSpreader)
				.define('P', BotaniaTags.Items.PETALS)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern("WWW")
				.pattern("WP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_item", has(BotaniaTags.Items.LIVINGWOOD_LOGS))
				.save(recipeOutput, BotaniaAPI.gogRL(getItemName(BotaniaBlocks.manaSpreader)));

		Ingredient yellow = Ingredient.of(BotaniaTags.Items.PETALS_YELLOW);
		Ingredient gray = Ingredient.of(BotaniaTags.Items.PETALS_GRAY);
		Ingredient green = Ingredient.of(BotaniaTags.Items.PETALS_GREEN);
		Ingredient red = Ingredient.of(BotaniaTags.Items.PETALS_RED);
		recipeOutput.accept(
				ResourceLocation.fromNamespaceAndPath(BotaniaAPI.GOG_MODID,
						"petal_apothecary/" + getItemName(BotaniaBlocks.orechid.asItem())),
				new PetalApothecaryRecipe(new ItemStack(BotaniaBlocks.orechid),
						Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT),
						gray, gray, yellow, yellow, green, green, red, red),
				null);

	}
}
