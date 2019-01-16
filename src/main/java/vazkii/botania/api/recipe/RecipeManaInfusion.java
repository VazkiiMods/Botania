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
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

public class RecipeManaInfusion {

	private final ItemStack output;
	private final Object input;
	private final int mana;
	private @Nullable IBlockState catalystState;

	public static IBlockState alchemyState;
	public static IBlockState conjurationState;

	public RecipeManaInfusion(ItemStack output, Object input, int mana) {
		this.output = output;
		this.input = input;
		this.mana = mana;
	}

	public boolean matches(ItemStack stack) {
		if(input instanceof ItemStack) {
			return OreDictionary.itemMatches((ItemStack) input, stack, false);
		}

		if(input instanceof String) {
			for(ItemStack ostack : OreDictionary.getOres((String) input, false)) {
				if(OreDictionary.itemMatches(ostack, stack, false))
					return true;
			}
		}

		return false;
	}

	public IBlockState getCatalyst() {
		return catalystState;
	}

	public void setCatalyst(IBlockState catalyst) {
		catalystState = catalyst;
	}

	public Object getInput() {
		return input;
	}

	public ItemStack getOutput() {
		return output;
	}

	public int getManaToConsume() {
		return mana;
	}

	/**
	 * @deprecated Use {@link RecipeManaInfusion#setCatalyst(IBlockState)} instead
	 */
	@Deprecated
	public void setAlchemy(boolean alchemy) {
		catalystState = alchemy ? alchemyState : null;
	}

	/**
	 * @deprecated Use {@link RecipeManaInfusion#getCatalyst()} instead
	 */
	@Deprecated
	public boolean isAlchemy() {
		return catalystState == alchemyState;
	}

	/**
	 * @deprecated Use {@link RecipeManaInfusion#setCatalyst(IBlockState)} instead
	 */
	@Deprecated
	public void setConjuration(boolean conjuration) {
		catalystState = conjuration ? conjurationState : null;
	}

	/**
	 * @deprecated Use {@link RecipeManaInfusion#getCatalyst()} instead
	 */
	@Deprecated
	public boolean isConjuration() {
		return catalystState == conjurationState;
	}
}

