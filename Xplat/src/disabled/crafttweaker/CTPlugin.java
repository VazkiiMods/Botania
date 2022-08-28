package vazkii.botania.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.block.CTBlockIngredient;
import com.blamejared.crafttweaker.api.plugin.CraftTweakerPlugin;
import com.blamejared.crafttweaker.api.plugin.ICraftTweakerPlugin;
import com.blamejared.crafttweaker.api.plugin.IRecipeHandlerRegistrationHandler;
import com.blamejared.crafttweaker.api.tag.CraftTweakerTagRegistry;
import com.blamejared.crafttweaker.impl.registry.CraftTweakerRegistry;
import com.blamejared.crafttweaker.natives.block.ExpandBlock;
import com.blamejared.crafttweaker.natives.block.ExpandBlockState;
import com.google.common.base.Suppliers;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.*;
import vazkii.botania.common.crafting.recipe.*;
import vazkii.botania.common.integration.crafttweaker.recipe.handler.DelegatingCraftingRecipeHandler;
import vazkii.botania.common.lib.LibMisc;

import java.util.stream.Collectors;

@CraftTweakerPlugin(LibMisc.MOD_ID + ":default")
public class CTPlugin implements ICraftTweakerPlugin {

	@Override
	public void registerRecipeHandlers(IRecipeHandlerRegistrationHandler handler) {
		handler.registerRecipeHandler(ArmorUpgradeRecipe.class, new DelegatingCraftingRecipeHandler<>(RecipeType.CRAFTING, Suppliers.memoize(() -> CraftTweakerRegistry.get().getRecipeHandlerFor(ShapedRecipe.class)), ArmorUpgradeRecipe::new));
		handler.registerRecipeHandler(ManaUpgradeRecipe.class, new DelegatingCraftingRecipeHandler<>(RecipeType.CRAFTING, Suppliers.memoize(() -> CraftTweakerRegistry.get().getRecipeHandlerFor(ShapedRecipe.class)), ManaUpgradeRecipe::new));
		handler.registerRecipeHandler(ShapelessManaUpgradeRecipe.class, new DelegatingCraftingRecipeHandler<>(RecipeType.CRAFTING, Suppliers.memoize(() -> CraftTweakerRegistry.get().getRecipeHandlerFor(ShapelessRecipe.class)), ShapelessManaUpgradeRecipe::new));
		handler.registerRecipeHandler(TwigWandRecipe.class, new DelegatingCraftingRecipeHandler<>(RecipeType.CRAFTING, Suppliers.memoize(() -> CraftTweakerRegistry.get().getRecipeHandlerFor(ShapedRecipe.class)), TwigWandRecipe::new));
		handler.registerRecipeHandler(WaterBottleMatchingRecipe.class, new DelegatingCraftingRecipeHandler<>(RecipeType.CRAFTING, Suppliers.memoize(() -> CraftTweakerRegistry.get().getRecipeHandlerFor(ShapedRecipe.class)), WaterBottleMatchingRecipe::new));
	}

	public static StateIngredient blockIngredientToStateIngredient(CTBlockIngredient ingredient) {
		if (ingredient == null) {
			return null;
		}
		return ingredient.mapTo(StateIngredientHelper::of, StateIngredientHelper::of, (blockTagKey, integer) -> StateIngredientHelper.of(blockTagKey), stream -> StateIngredientHelper.compound(stream.toList()));
	}

	public static String ingredientToCommandString(StateIngredient ingr) {
		if (ingr instanceof StateIngredientTag sit) {
			return CraftTweakerTagRegistry.INSTANCE.knownTagManager(Registry.BLOCK_REGISTRY).tag(sit.getTagId()).getCommandString();
		}
		if (ingr instanceof StateIngredientBlock sib) {
			return ExpandBlock.getCommandString(sib.getBlock());
		}
		if (ingr instanceof StateIngredientBlockState sibs) {
			return ExpandBlockState.getCommandString(sibs.getState());
		}
		if (ingr instanceof StateIngredientBlocks) {
			return ingr.getDisplayed().stream().map(BlockState::getBlock)
					.map(ExpandBlock::getCommandString)
					.collect(Collectors.joining(", ", "[", "]"));
		}
		if (ingr instanceof StateIngredientCompound sic) {
			return sic.getIngredients().stream().map(CTPlugin::ingredientToCommandString).collect(Collectors.joining(" | "));
		}
		return ingr.toString();
	}
}
