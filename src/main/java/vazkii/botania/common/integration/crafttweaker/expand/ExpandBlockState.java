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

import net.minecraft.block.BlockState;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.StateIngredientHelper;

@ZenRegister
@ZenCodeType.Expansion("crafttweaker.api.blocks.MCBlockState")
public class ExpandBlockState {
	@ZenCodeType.Caster(implicit = true)
	public static StateIngredient asStateIngredient(BlockState internal) {
		return StateIngredientHelper.of(internal);
	}
}
