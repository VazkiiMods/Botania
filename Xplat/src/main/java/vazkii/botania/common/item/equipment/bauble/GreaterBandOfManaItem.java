/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.item.CustomCreativeTabContents;

public class GreaterBandOfManaItem extends BandOfManaItem implements CustomCreativeTabContents {

	private static final int MAX_MANA = BandOfManaItem.MAX_MANA * 4;

	public GreaterBandOfManaItem(Properties props) {
		super(props);
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(this);

		ItemStack full = new ItemStack(this);
		setMana(full, MAX_MANA);
		output.accept(full);
	}

	public static class GreaterManaItemImpl extends ManaItemImpl {
		public GreaterManaItemImpl(ItemStack stack) {
			super(stack);
		}

		@Override
		public int getMaxMana() {
			return MAX_MANA * stack.getCount();
		}
	}
}
