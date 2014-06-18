package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

public class ItemElementiumHelm extends ItemElementiumArmor {

	public ItemElementiumHelm() {
		super(0, LibItemNames.ELEMENTIUM_HELM);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.052083333333333333333333333333335F;
	}
	
}
