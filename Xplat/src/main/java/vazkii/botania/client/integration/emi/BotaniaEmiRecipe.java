/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public abstract class BotaniaEmiRecipe implements EmiRecipe {
	private final EmiRecipeCategory category;
	private final ResourceLocation id;
	protected List<EmiIngredient> input = List.of();
	protected List<EmiStack> output = List.of();
	private final String group;

	public BotaniaEmiRecipe(EmiRecipeCategory category, RecipeHolder<?> recipe) {
		this.category = category;
		this.id = recipe.id();
		this.group = recipe.value().getGroup();
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return category;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return input;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return output;
	}

	public String getGroup() {
		return group;
	}

	public static RegistryAccess getRegistryAccess() {
		var level = Minecraft.getInstance().level;
		return level != null ? level.registryAccess() : RegistryAccess.EMPTY;
	}
}
