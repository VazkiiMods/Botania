package vazkii.botania.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.function.Consumer;

import static vazkii.botania.data.recipes.RecipeProvider.*;
import static vazkii.botania.data.recipes.RecipeProvider.conditionsFromTag;

public class ForgeRecipeProvider extends BotaniaRecipeProvider {
	public ForgeRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.azulejo0)
				.requires(Items.BLUE_DYE)
				.requires(Tags.Items.STORAGE_BLOCKS_QUARTZ)
				.unlockedBy("has_item", conditionsFromItem(Items.BLUE_DYE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.baubleBox)
				.define('C', Tags.Items.CHESTS_WOODEN)
				.define('G', Items.GOLD_INGOT)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" M ")
				.pattern("MCG")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);

		registerRedStringBlock(consumer, BotaniaBlocks.redStringContainer, Ingredient.of(Tags.Items.CHESTS_WOODEN), conditionsFromTag(Tags.Items.CHESTS_WOODEN));
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.corporeaRetainer)
				.requires(Tags.Items.CHESTS_WOODEN)
				.requires(ModItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.save(consumer);
	}

	@Override
	public String getName() {
		return "Botania crafting recipes (Forge-specific)";
	}
}
