/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 2, 2014, 12:57:22 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.common.item.Item16Colors;
import vazkii.botania.common.lib.LibItemNames;

public class ItemManaPetal extends Item16Colors implements IFlowerComponent {

	public ItemManaPetal() {
		super(LibItemNames.MANA_PETAL);
	}

	@Override
	public boolean canFit(ItemStack stack, IPetalApothecary apothecary) {
		return true;
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return EnumDyeColor.byMetadata(stack.getItemDamage()).getMapColor().colorValue;
	}
}
