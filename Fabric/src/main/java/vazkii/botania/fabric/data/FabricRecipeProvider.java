package vazkii.botania.fabric.data;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import org.apache.commons.lang3.mutable.MutableObject;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;
import vazkii.botania.data.recipes.GogAlternationResult;

import java.util.function.Consumer;

import static vazkii.botania.data.recipes.CraftingRecipeProvider.*;

public class FabricRecipeProvider extends BotaniaRecipeProvider {
	public FabricRecipeProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		// Quartz tag
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.azulejo0)
				.requires(Items.BLUE_DYE)
				.requires(FabricItemTagProvider.QUARTZ_BLOCKS)
				.unlockedBy("has_item", conditionsFromItem(Items.BLUE_DYE))
				.save(consumer);

		// Chest tag
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.baubleBox)
				.define('C', FabricItemTagProvider.WOODEN_CHESTS)
				.define('G', Items.GOLD_INGOT)
				.define('M', BotaniaTags.Items.INGOTS_MANASTEEL)
				.pattern(" M ")
				.pattern("MCM")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.INGOTS_MANASTEEL))
				.save(consumer);

		// uses colorless glass tags on Forge
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.glassPick)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('G', Items.GLASS)
				.define('I', BotaniaTags.Items.INGOTS_MANASTEEL)
				.pattern("GIG")
				.pattern(" T ")
				.pattern(" T ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.lensNormal)
				.define('S', BotaniaTags.Items.INGOTS_MANASTEEL)
				.define('G', Ingredient.of(Items.GLASS, Items.GLASS_PANE))
				.pattern(" S ")
				.pattern("SGS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.prism)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('S', BotaniaBlocks.spectralPlatform)
				.define('G', Items.GLASS)
				.pattern("GPG")
				.pattern("GSG")
				.pattern("GPG")
				.unlockedBy("has_item", conditionsFromItem(Items.PRISMARINE_CRYSTALS))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaBlocks.spectralPlatform))
				.save(consumer);

		registerRedStringBlock(consumer, BotaniaBlocks.redStringContainer, Ingredient.of(FabricItemTagProvider.WOODEN_CHESTS), conditionsFromTag(FabricItemTagProvider.WOODEN_CHESTS));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.corporeaRetainer)
				.requires(FabricItemTagProvider.WOODEN_CHESTS)
				.requires(BotaniaItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.corporeaSpark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.phantomInk, 4)
				.requires(BotaniaItems.manaPearl)
				.requires(ConventionalItemTags.DYES)
				.requires(ConventionalItemTags.GLASS_BLOCKS)
				.requires(Items.GLASS_BOTTLE, 4)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.save(consumer);

		MutableObject<FinishedRecipe> base = new MutableObject<>();
		MutableObject<FinishedRecipe> gog = new MutableObject<>();
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.fertilizer)
				.requires(Items.BONE_MEAL)
				.requires(Ingredient.of(ConventionalItemTags.DYES), 4)
				.unlockedBy("has_item", conditionsFromTag(ConventionalItemTags.DYES))
				.save(base::setValue, "botania:fertilizer_dye");
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.fertilizer, 3)
				.requires(Items.BONE_MEAL)
				.requires(Ingredient.of(ConventionalItemTags.DYES), 4)
				.unlockedBy("has_item", conditionsFromTag(ConventionalItemTags.DYES))
				.save(gog::setValue, "botania:fertilizer_dye");
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
	}

	@Override
	public String getName() {
		return "Botania recipes (Fabric-specific)";
	}
}
