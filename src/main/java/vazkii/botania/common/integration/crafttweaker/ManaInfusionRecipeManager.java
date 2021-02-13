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
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.item.MCItemStack;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeManaInfusion;

import javax.annotation.Nonnull;

@ZenRegister
@ZenCodeType.Name("mods.botania.ManaInfusion")
public class ManaInfusionRecipeManager implements IRecipeManager {

	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient input, int mana, @ZenCodeType.Optional BlockState catalystState, @ZenCodeType.Optional RecipeFunctionSingle function) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		RecipeManaInfusion recipe;
		if (function == null) {
			recipe = new RecipeManaInfusion(resourceLocation, output.getInternal(), input.asVanillaIngredient(), mana, "", catalystState);
		} else {
			recipe = new RecipeManaInfusion(resourceLocation, output.getInternal(), input.asVanillaIngredient(), mana, "", catalystState) {
				@Nonnull
				@Override
				public ItemStack getRecipeOutput(@Nonnull ItemStack input) {
					return function.process(new MCItemStack(getRecipeOutput()), new MCItemStack(input)).getInternal().copy();
				}
			};
		}
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
	}

	@Override
	public IRecipeType<IManaInfusionRecipe> getRecipeType() {
		return ModRecipeTypes.MANA_INFUSION_TYPE;
	}
}
