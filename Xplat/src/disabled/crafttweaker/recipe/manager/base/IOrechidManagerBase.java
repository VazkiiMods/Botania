package vazkii.botania.common.integration.crafttweaker.recipe.manager.base;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.block.CTBlockIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IReplacementRule;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.blamejared.crafttweaker.natives.block.ExpandBlock;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.integration.crafttweaker.CTPlugin;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * @docParam this <recipetype:botania:orechid>
 */
@Document("mods/Botania/recipe/manager/base/IOrechidManagerBase")
@ZenRegister
@ZenCodeType.Name("mods.botania.recipe.manager.base.IOrechidManagerBase")
public interface IOrechidManagerBase<T extends IOrechidRecipe> extends IRecipeManager<T>, IRecipeHandler<T> {

	/**
	 * Registers a new ore weight.
	 *
	 * @param name   The name of the weight.
	 * @param output The blocks to output
	 * @param input  The input block
	 * @param weight The weight
	 * @docParam name "orechid_test"
	 * @docParam output <blockstate:minecraft:dirt>
	 * @docParam input <block:minecraft:diamond_ore>
	 * @docParam weight 50
	 */
	@ZenCodeType.Method
	default void registerOreWeight(String name, CTBlockIngredient output, Block input, int weight) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = CraftTweakerConstants.rl(name);
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
				makeRecipe(resourceLocation,
						input, CTPlugin.blockIngredientToStateIngredient(output), weight)));
	}

	T makeRecipe(ResourceLocation name, Block input, StateIngredient output, int weight);

	/**
	 * Removes orechid weights that output the given blockstate.
	 *
	 * @param output The blockstate output to remove.
	 * @docParam output <blockstate:minecraft:dirt>
	 */
	@ZenCodeType.Method
	default void remove(BlockState output) {
		CraftTweakerAPI.apply(new ActionRemoveRecipe<>(this, recipe -> recipe.getOutput().test(output)));
	}

	@Override
	default void remove(IIngredient output) {
		throw new UnsupportedOperationException(
				"Orechid does not output IItemStacks, use remove(BlockState)!");
	}

	@Override
	default String dumpToCommandString(@SuppressWarnings("rawtypes") IRecipeManager manager, T recipe) {
		StringJoiner s = new StringJoiner(", ", manager.getCommandString() + ".registerOreWeight(", ");");

		s.add(StringUtil.quoteAndEscape(recipe.getId()));
		s.add(CTPlugin.ingredientToCommandString(recipe.getOutput()));
		s.add(ExpandBlock.getCommandString(recipe.getInput()));
		s.add(String.valueOf(recipe.getWeight()));
		return s.toString();
	}

	@Override
	default Optional<Function<ResourceLocation, T>> replaceIngredients(@SuppressWarnings("rawtypes") IRecipeManager manager, T recipe, List<IReplacementRule> rules) {
		return Optional.empty();
	}
}
