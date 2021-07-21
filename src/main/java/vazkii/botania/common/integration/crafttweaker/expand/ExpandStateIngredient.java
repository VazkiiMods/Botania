/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker.expand;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;

import net.minecraft.block.BlockState;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.integration.crafttweaker.CTIntegration;

import java.util.Random;

/**
 * Wrapper around blocks, blockstates or block tags used to match recipe inputs like blocks to convert
 * with the Pure Daisy, or catalysts for mana infusion.
 *
 * To create, simply cast an MCBlock, MCBlockState, an array of MCBlocks, or an MCTag&lt;MCBlock&gt; to StateIngredient.
 * This is done automatically when using them as a parameter for a method.
 *
 * @docParam this ingredient
 */
@Document("mods/Botania/StateIngredient")
@ZenRegister
@NativeTypeRegistration(value = StateIngredient.class, zenCodeName = "mods.botania.StateIngredient")
public class ExpandStateIngredient {

	/**
	 * Tests if the provided block state matches the recipe.
	 *
	 * @param state Block state to test against this ingredient
	 *
	 * @docParam state <blockstate:minecraft:dirt>
	 */
	@ZenCodeType.Method
	public static boolean matches(StateIngredient internal, BlockState state) {
		return internal.test(state);
	}

	/**
	 * Returns a random state matching the ingredient.
	 *
	 * @param random a Random instance, usually obtained from a world
	 *
	 * @docParam random world.random
	 */
	@ZenCodeType.Method
	public static BlockState pick(StateIngredient internal, Random random) {
		return internal.pick(random);
	}

	@ZenCodeType.Caster
	public static String asString(StateIngredient internal) {
		return CTIntegration.ingredientToCommandString(internal);
	}
}
