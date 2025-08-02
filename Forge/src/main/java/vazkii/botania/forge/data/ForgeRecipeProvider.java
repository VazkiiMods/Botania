package vazkii.botania.forge.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import org.apache.commons.lang3.mutable.MutableObject;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;
import vazkii.botania.data.recipes.GogAlternationResult;

import java.util.List;
import java.util.function.Consumer;

import static vazkii.botania.data.recipes.CraftingRecipeProvider.*;

public class ForgeRecipeProvider extends BotaniaRecipeProvider {
	public ForgeRecipeProvider(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.azulejo0)
				.requires(Items.BLUE_DYE)
				.requires(Tags.Items.STORAGE_BLOCKS_QUARTZ)
				.unlockedBy("has_item", conditionsFromItem(Items.BLUE_DYE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.baubleBox)
				.define('C', Tags.Items.CHESTS_WOODEN)
				.define('G', Items.GOLD_INGOT)
				.define('M', BotaniaTags.Items.INGOTS_MANASTEEL)
				.pattern(" M ")
				.pattern("MCM")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.INGOTS_MANASTEEL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.glassPick)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('G', Tags.Items.GLASS_COLORLESS)
				.define('I', BotaniaTags.Items.INGOTS_MANASTEEL)
				.pattern("GIG")
				.pattern(" T ")
				.pattern(" T ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.INGOTS_MANASTEEL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.prism)
				.define('P', Tags.Items.GEMS_PRISMARINE)
				.define('S', BotaniaBlocks.spectralPlatform)
				.define('G', Tags.Items.GLASS_COLORLESS)
				.pattern("GPG")
				.pattern("GSG")
				.pattern("GPG")
				.unlockedBy("has_item", conditionsFromTag(Tags.Items.GEMS_PRISMARINE))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaBlocks.spectralPlatform))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.lensNormal)
				.define('S', BotaniaTags.Items.INGOTS_MANASTEEL)
				.define('G', Ingredient.merge(List.of(Ingredient.of(Tags.Items.GLASS_COLORLESS), Ingredient.of(Tags.Items.GLASS_PANES_COLORLESS))))
				.pattern(" S ")
				.pattern("SGS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.INGOTS_MANASTEEL))
				.save(consumer);

		registerRedStringBlock(consumer, BotaniaBlocks.redStringContainer, Ingredient.of(Tags.Items.CHESTS_WOODEN), conditionsFromTag(Tags.Items.CHESTS_WOODEN));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.corporeaRetainer)
				.requires(Tags.Items.CHESTS_WOODEN)
				.requires(BotaniaItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.corporeaSpark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.phantomInk, 4)
				.requires(BotaniaItems.manaPearl)
				.requires(Tags.Items.DYES)
				.requires(Tags.Items.GLASS)
				.requires(Items.GLASS_BOTTLE, 4)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.save(consumer);

		MutableObject<FinishedRecipe> base = new MutableObject<>();
		MutableObject<FinishedRecipe> gog = new MutableObject<>();
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.fertilizer)
				.requires(Items.BONE_MEAL)
				.requires(Ingredient.of(Tags.Items.DYES), 4)
				.unlockedBy("has_item", conditionsFromTag(Tags.Items.DYES))
				.save(base::setValue, "botania:fertilizer_dye");
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.fertilizer, 3)
				.requires(Items.BONE_MEAL)
				.requires(Ingredient.of(Tags.Items.DYES), 4)
				.unlockedBy("has_item", conditionsFromTag(Tags.Items.DYES))
				.save(gog::setValue, "botania:fertilizer_dye");
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
	}

	@Override
	public String getName() {
		return "Botania crafting recipes (Forge-specific)";
	}
}
