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
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;

import net.minecraft.block.BlockState;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.Random;

@ZenRegister
@NativeTypeRegistration(value = StateIngredient.class, zenCodeName = "mods.botania.StateIngredient")
public class ExpandStateIngredient {

	@ZenCodeType.Method
	public static boolean matches(StateIngredient internal, BlockState state) {
		return internal.test(state);
	}

	@ZenCodeType.Method
	public static BlockState pick(StateIngredient internal, Random random) {
		return internal.pick(random);
	}

	@ZenCodeType.Caster
	public static String asString(StateIngredient internal) {
		return internal.toString();
	}
}
