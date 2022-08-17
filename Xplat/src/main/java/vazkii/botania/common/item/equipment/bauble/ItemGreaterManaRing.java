/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public class ItemGreaterManaRing extends ItemManaRing {

	private static final int MAX_MANA = ItemManaRing.MAX_MANA * 4;

	public ItemGreaterManaRing(Properties props) {
		super(props);
	}

	@Override
	public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> stacks) {
		if (allowedIn(tab)) {
			stacks.add(new ItemStack(this));

			ItemStack full = new ItemStack(this);
			setMana(full, MAX_MANA);
			stacks.add(full);
		}
	}

	public static class GreaterManaItem extends ItemManaRing.ManaItem {
		public GreaterManaItem(ItemStack stack) {
			super(stack);
		}

		@Override
		public int getMaxMana() {
			return MAX_MANA * stack.getCount();
		}
	}
}
