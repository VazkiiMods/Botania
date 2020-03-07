/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Implementation detail, you shouldn't need to use this.
 */
public interface IModRecipe {
	List<Ingredient> getInputs();
	ItemStack getOutput();
	ResourceLocation getId();
}
