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
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.StateIngredientHelper;

@ZenRegister
@ZenCodeType.Expansion("crafttweaker.api.blocks.MCBlock[]")
public class ExpandBlockArray {
	@ZenCodeType.Caster(implicit = true)
	public static StateIngredient asStateIngredient(Block[] internal) {
		return StateIngredientHelper.of(ImmutableSet.copyOf(internal));
	}
}
