/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2014, 5:57:07 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class RecipeManaInfusion {

	private final ItemStack output;
	private final Ingredient input;
	private final int mana;
	private @Nullable IBlockState catalystState;

	public static IBlockState alchemyState;
	public static IBlockState conjurationState;

	public RecipeManaInfusion(ItemStack output, Ingredient input, int mana) {
		this.output = output;
		this.input = input;
		this.mana = mana;
	}

	public boolean matches(ItemStack stack) {
		return input.test(stack);
	}

	public IBlockState getCatalyst() {
		return catalystState;
	}

	public void setCatalyst(IBlockState catalyst) {
		catalystState = catalyst;
	}

	public Ingredient getInput() {
		return input;
	}

	public ItemStack getOutput() {
		return output;
	}

	public int getManaToConsume() {
		return mana;
	}
}

