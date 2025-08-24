/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public interface OrechidRecipe extends BlockStateRecipe {
	ResourceLocation TYPE_ID = botaniaRL("orechid");
	ResourceLocation IGNEM_TYPE_ID = botaniaRL("orechid_ignem");
	ResourceLocation MARIMORPHOSIS_TYPE_ID = botaniaRL("marimorphosis");

	@Override
	RecipeType<? extends OrechidRecipe> getType();

	/** Location-sensitive output, called with the position of the block to convert. */
	default StateIngredient getOutput(Level level, BlockPos pos) {
		return getOutput();
	}

	/**
	 * Default weight, used if no special weight logic is provided, and to display
	 * in recipes (the JEI/REI displayed output per 64 input depends on the sum of default weights).
	 */
	int getWeight();

	/** Location-sensitive weight, called with the position of the block to convert. */
	default int getWeight(Level level, BlockPos pos) {
		return getWeight();
	}

}
