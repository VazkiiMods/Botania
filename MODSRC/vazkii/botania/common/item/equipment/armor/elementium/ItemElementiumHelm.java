package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

public class ItemElementiumHelm extends ItemElementiumArmor {

	public ItemElementiumHelm() {
		super(0, LibItemNames.ELEMENTIUM_HELM);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 5F / (25F / 24F) * 1.5F;
	}
	
}
