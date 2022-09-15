package vazkii.botania.fabric.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.function.Consumer;

import static vazkii.botania.data.recipes.RecipeProvider.*;

public class FabricRecipeProvider extends BotaniaRecipeProvider {
	public FabricRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<FinishedRecipe> consumer) {
		// Quartz tag
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.azulejo0)
				.requires(Items.BLUE_DYE)
				.requires(FabricItemTagProvider.QUARTZ_BLOCKS)
				.unlockedBy("has_item", conditionsFromItem(Items.BLUE_DYE))
				.save(consumer);

		// Chest tag
		ShapedRecipeBuilder.shaped(ModItems.baubleBox)
				.define('C', FabricItemTagProvider.WOODEN_CHESTS)
				.define('G', Items.GOLD_INGOT)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" M ")
				.pattern("MCG")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);

		registerRedStringBlock(consumer, BotaniaBlocks.redStringContainer, Ingredient.of(FabricItemTagProvider.WOODEN_CHESTS), conditionsFromTag(FabricItemTagProvider.WOODEN_CHESTS));
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.corporeaRetainer)
				.requires(FabricItemTagProvider.WOODEN_CHESTS)
				.requires(ModItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.save(consumer);
	}

	@Override
	public String getName() {
		return "Botania recipes (Fabric-specific)";
	}
}
