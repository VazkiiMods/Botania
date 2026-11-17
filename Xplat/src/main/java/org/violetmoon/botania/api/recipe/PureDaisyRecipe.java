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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public interface PureDaisyRecipe extends BlockStateRecipe {
	ResourceLocation TYPE_ID = botaniaRL("pure_daisy");

	/**
	 * This gets called every tick, please be careful with your checks.
	 */
	boolean matches(Level world, BlockPos pos, BlockState state);

	/**
	 * Returns whether any relevant block state properties of the matched block will be copied over to the
	 * converted block as the conversion takes place. (Used to e.g. keep the rotation of converted logs.)
	 */
	boolean isCopyInputProperties();

	/**
	 * Returns the number of times a source block must be ticked by the flower before it converts.
	 * Note that the Pure Daisy ticks its surrounding blocks in a round-robin way, one at a time.
	 */
	int getTime();

	@Override
	default RecipeType<?> getType() {
		return Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID));
	}
}
