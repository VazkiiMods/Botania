/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker.recipe.manager;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.block.CTBlockIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.function.RecipeFunctionSingle;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IReplacementRule;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.blamejared.crafttweaker.platform.Services;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeManaInfusion;
import vazkii.botania.common.integration.crafttweaker.CTPlugin;

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
@Document("mods/Botania/recipe/manager/ManaInfusionRecipeManager")
@ZenRegister
@IRecipeHandler.For(IManaInfusionRecipe.class)
@ZenCodeType.Name("mods.botania.recipe.manager.ManaInfusionRecipeManager")
public class ManaInfusionRecipeManager implements IRecipeManager<IManaInfusionRecipe>, IRecipeHandler<IManaInfusionRecipe> {

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
	 * @docParam name "mana_infusion_test_catalyst"
	 * @docParam output <item:minecraft:diamond>
	 * @docParam input <item:minecraft:apple>
	 * @docParam mana 200
	 * @docParam catalyst <block:botania:alchemy_catalyst>
	 * @docParam group ""
	 * @docParam function (usualOut as IItemStack, input as IItemStack) => { return usualOut.withTag(input.tag); }
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient input, int mana, @ZenCodeType.Optional CTBlockIngredient catalyst, @ZenCodeType.OptionalString("") String group, @ZenCodeType.Optional RecipeFunctionSingle function) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = CraftTweakerConstants.rl(name);
		RecipeManaInfusion recipe;
		if (function == null) {
			recipe = new RecipeManaInfusion(resourceLocation, output.getInternal(), input.asVanillaIngredient(), mana, group, CTPlugin.blockIngredientToStateIngredient(catalyst));
		} else {
			recipe = new InfusionWithFunction(resourceLocation, output.getInternal(), input.asVanillaIngredient(), mana, group, CTPlugin.blockIngredientToStateIngredient(catalyst), function);
		}
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this, recipe));
	}

	@Override
	public RecipeType<IManaInfusionRecipe> getRecipeType() {
		return ModRecipeTypes.MANA_INFUSION_TYPE;
	}

	@Override
	public String dumpToCommandString(@SuppressWarnings("rawtypes") IRecipeManager manager, IManaInfusionRecipe recipe) {
		List<String> s = new ArrayList<>();
		s.add(StringUtil.quoteAndEscape(recipe.getId()));
		s.add(Services.PLATFORM.createMCItemStack(recipe.getResultItem()).getCommandString());
		IIngredient input = IIngredient.fromIngredient(recipe.getIngredients().get(0));
		s.add(input.getCommandString());
		s.add(String.valueOf(recipe.getManaToConsume()));

		s.add(recipe.getRecipeCatalyst() != null ? CTPlugin.ingredientToCommandString(recipe.getRecipeCatalyst()) : null);
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
	public Optional<Function<ResourceLocation, IManaInfusionRecipe>> replaceIngredients(@SuppressWarnings("rawtypes") IRecipeManager manager, IManaInfusionRecipe recipe, List<IReplacementRule> rules) {
		return IRecipeHandler.attemptReplacing(recipe.getIngredients().get(0), Ingredient.class, recipe, rules)
				.map(ingr -> {
					if (recipe instanceof InfusionWithFunction) {
						return id -> new ManaInfusionRecipeManager.InfusionWithFunction(id,
								recipe.getResultItem(), ingr, recipe.getManaToConsume(),
								recipe.getGroup(), recipe.getRecipeCatalyst(), ((InfusionWithFunction) recipe).getFunction());
					}
					return id -> new RecipeManaInfusion(id,
							recipe.getResultItem(), ingr, recipe.getManaToConsume(),
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
			return function.process(Services.PLATFORM.createMCItemStack(getResultItem()), Services.PLATFORM.createMCItemStack(input)).getInternal().copy();
		}

		private RecipeFunctionSingle getFunction() {
			return function;
		}
	}
}
