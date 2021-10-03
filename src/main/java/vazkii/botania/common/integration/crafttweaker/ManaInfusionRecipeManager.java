/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.api.recipes.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipes.IReplacementRule;
import com.blamejared.crafttweaker.api.util.StringUtils;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.item.MCItemStack;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeManaInfusion;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @docParam this <recipetype:botania:mana_infusion>
 */
@Document("mods/Botania/ManaInfusion")
@ZenRegister
@IRecipeHandler.For(RecipeManaInfusion.class)
@ZenCodeType.Name("mods.botania.ManaInfusion")
public class ManaInfusionRecipeManager implements IRecipeManager, IRecipeHandler<RecipeManaInfusion> {

	/**
	 * Adds a mana infusion recipe.
	 *
	 * @param name     Name of the recipe to add
	 * @param output   Output item
	 * @param input    Input item
	 * @param mana     Required mana
	 * @param catalyst Optional catalyst required under the mana pool for this recipe to craft
	 * @param group    Optional recipe group, use if you want to replace a grouped recipe in the lexicon
	 * @param function Optional function modifying the output
	 *
	 * @docParam name "mana_infusion_test_catalyst"
	 * @docParam output <item:minecraft:diamond>
	 * @docParam input <item:minecraft:apple>
	 * @docParam mana 200
	 * @docParam catalyst <block:botania:alchemy_catalyst>
	 * @docParam group null
	 * @docParam function (usualOut as IItemStack, input as IItemStack) => { return usualOut.withTag(input.tag); }
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient input, int mana, @ZenCodeType.Optional StateIngredient catalyst, @ZenCodeType.OptionalString String group, @ZenCodeType.Optional RecipeFunctionSingle function) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		if (group == null) {
			group = "";
		}
		RecipeManaInfusion recipe;
		if (function == null) {
			recipe = new RecipeManaInfusion(resourceLocation, output.getInternal(), input.asVanillaIngredient(), mana, group, catalyst);
		} else {
			recipe = new InfusionWithFunction(resourceLocation, output.getInternal(), input.asVanillaIngredient(), mana, group, catalyst, function);
		}
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
	}

	@Override
	public IRecipeType<IManaInfusionRecipe> getRecipeType() {
		return ModRecipeTypes.MANA_INFUSION_TYPE;
	}

	@Override
	public String dumpToCommandString(IRecipeManager manager, RecipeManaInfusion recipe) {
		List<String> s = new ArrayList<>();
		s.add(StringUtils.quoteAndEscape(recipe.getId()));
		s.add(new MCItemStackMutable(recipe.getRecipeOutput()).getCommandString());
		IIngredient input = IIngredient.fromIngredient(recipe.getIngredients().get(0));
		s.add(input.getCommandString());
		s.add(String.valueOf(recipe.getManaToConsume()));

		s.add(recipe.getRecipeCatalyst() != null ? CTIntegration.ingredientToCommandString(recipe.getRecipeCatalyst()) : null);
		s.add(!recipe.getGroup().equals("") ? recipe.getGroup() : null);
		if (recipe instanceof InfusionWithFunction) {
			s.add("(usualOut, input) => { ... }");
		}

		ListIterator<String> iterator = s.listIterator(s.size()); // Strip optional arguments, which will be null
		while (iterator.hasPrevious() && iterator.previous() == null) {
			iterator.remove();
		}
		return s.stream().collect(Collectors.joining(", ", manager.getCommandString() + ".addRecipe(", ");"));
	}

	@Override
	public Optional<Function<ResourceLocation, RecipeManaInfusion>> replaceIngredients(IRecipeManager manager, RecipeManaInfusion recipe, List<IReplacementRule> rules) {
		return IRecipeHandler.attemptReplacing(recipe.getIngredients().get(0), Ingredient.class, recipe, rules)
				.map(ingr -> {
					if (recipe instanceof InfusionWithFunction) {
						return id -> new ManaInfusionRecipeManager.InfusionWithFunction(id,
								recipe.getRecipeOutput(), ingr, recipe.getManaToConsume(),
								recipe.getGroup(), recipe.getRecipeCatalyst(), ((InfusionWithFunction) recipe).getFunction());
					}
					return id -> new RecipeManaInfusion(id,
							recipe.getRecipeOutput(), ingr, recipe.getManaToConsume(),
							recipe.getGroup(), recipe.getRecipeCatalyst());
				});
	}

	private static class InfusionWithFunction extends RecipeManaInfusion {
		private final RecipeFunctionSingle function;

		public InfusionWithFunction(ResourceLocation resourceLocation, ItemStack internal, Ingredient input1, int mana, String group, StateIngredient catalyst, RecipeFunctionSingle function) {
			super(resourceLocation, internal, input1, mana, group, catalyst);
			this.function = function;
		}

		@Nonnull
		@Override
		public ItemStack getRecipeOutput(@Nonnull ItemStack input) {
			return function.process(new MCItemStack(getRecipeOutput()), new MCItemStack(input)).getInternal().copy();
		}

		private RecipeFunctionSingle getFunction() {
			return function;
		}
	}
}
