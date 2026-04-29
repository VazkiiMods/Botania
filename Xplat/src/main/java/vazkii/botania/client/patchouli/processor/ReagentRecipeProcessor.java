package vazkii.botania.client.patchouli.processor;

import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.recipe.RecipeWithReagent;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;

public abstract class ReagentRecipeProcessor<T extends RecipeWithReagent> implements IComponentProcessor {
	@Nullable
	protected RecipeHolder<T> recipe;

	@Override
	public abstract void setup(Level level, IVariableProvider variables);

	@UnknownNullability
	@Override
	public IVariable process(Level level, String key) {
		//noinspection ConstantValue
		if (recipe == null || recipe.value() == null) {
			return null;
		}
		return switch (key) {
			case "recipe" -> IVariable.wrap(recipe.id().toString(), level.registryAccess());
			case "reagent" -> PatchouliUtils.interweaveIngredients(List.of(recipe.value().getReagent()), level);
			case "output" -> IVariable.from(recipe.value().getResultItem(level.registryAccess()), level.registryAccess());
			case "heading" -> IVariable.from(recipe.value().getResultItem(level.registryAccess()).getHoverName(), level.registryAccess());
			default -> null;
		};
	}
}
