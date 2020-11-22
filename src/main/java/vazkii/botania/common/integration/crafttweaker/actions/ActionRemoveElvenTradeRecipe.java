package vazkii.botania.common.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.exceptions.ScriptException;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.logger.ILogger;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import vazkii.botania.api.recipe.IElvenTradeRecipe;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionRemoveElvenTradeRecipe extends ActionRecipeBase {
	
	private final IItemStack[] outputs;
	
	public ActionRemoveElvenTradeRecipe(IRecipeManager manager, IItemStack[] outputs) {
		super(manager);
		this.outputs = outputs;
	}
	
	@Override
	public void apply() {
		Iterator<Map.Entry<ResourceLocation, IRecipe<?>>> iter = getManager().getRecipes().entrySet().iterator();
		while (iter.hasNext()) {
			IElvenTradeRecipe recipe = (IElvenTradeRecipe) iter.next().getValue();
			if (recipe.getOutputs().size() != outputs.length) {
				continue;
			}
			List<MCItemStackMutable> collected =
					recipe.getOutputs().stream().map(MCItemStackMutable::new).collect(Collectors.toList());
			
			boolean valid = true;
			for (int i = 0; i < outputs.length; i++) {
				if (!outputs[i].matches(collected.get(i))) {
					valid = false;
				}
			}
			if (valid) {
				iter.remove();
			}
		}
	}
	
	@Override
	public String describe() {
		return "Removing \"" + Registry.RECIPE_TYPE
				.getKey(getManager().getRecipeType()) + "\" recipes with output: " + Arrays.toString(outputs) + "\"";
	}
	
	@Override
	public boolean validate(ILogger logger) {
		if (outputs == null) {
			logger.throwingWarn("output cannot be null!", new ScriptException("output MCBlockState cannot be null!"));
			return false;
		}
		return true;
	}
}
