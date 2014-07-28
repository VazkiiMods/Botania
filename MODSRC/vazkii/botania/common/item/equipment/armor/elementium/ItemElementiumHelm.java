package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

public class ItemElementiumHelm extends ItemElementiumArmor {

	public ItemElementiumHelm() {
		this(LibItemNames.ELEMENTIUM_HELM);
	}

	public ItemElementiumHelm(String name) {
		super(0, name);
	}
	
	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.055F;
	}

}
