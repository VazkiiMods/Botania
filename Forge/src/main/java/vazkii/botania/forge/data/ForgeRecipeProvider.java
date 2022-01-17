package vazkii.botania.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.function.Consumer;

import static vazkii.botania.data.recipes.RecipeProvider.conditionsFromItem;

public class ForgeRecipeProvider extends BotaniaRecipeProvider {
	public ForgeRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(ModBlocks.azulejo0)
				.requires(Items.BLUE_DYE)
				.requires(Tags.Items.STORAGE_BLOCKS_QUARTZ)
				.unlockedBy("has_item", conditionsFromItem(Items.BLUE_DYE))
				.save(consumer);
	}

	@Override
	public String getName() {
		return "Botania crafting recipes (Forge-specific)";
	}
}
