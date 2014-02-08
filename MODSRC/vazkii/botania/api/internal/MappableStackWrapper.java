/**
 * This class was created by <cpw>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.api.internal;

import net.minecraft.item.ItemStack;

public class MappableStackWrapper {

	private ItemStack wrap;

	public MappableStackWrapper(ItemStack toWrap) {
		wrap = toWrap;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MappableStackWrapper)) 
			return false;

		MappableStackWrapper isw = (MappableStackWrapper) obj;
		if (wrap.getHasSubtypes())
			return isw.wrap.isItemEqual(wrap);

		else return isw.wrap == wrap;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(wrap);
	}
}
