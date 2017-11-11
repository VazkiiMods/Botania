/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2015, 6:14:13 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemSpellCloth extends ItemMod {

	public ItemSpellCloth() {
		super(LibItemNames.SPELL_CLOTH);
		setMaxDamage(35);
		setMaxStackSize(1);
		setNoRepair();
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		ItemStack stack = itemStack.copy();
		stack.setItemDamage(stack.getItemDamage() + 1);
		return stack;
	}

}
