package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

public class ItemElementiumChest extends ItemElementiumArmor {

	public ItemElementiumChest() {
		super(1, LibItemNames.ELEMENTIUM_CHEST);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.17F;
	}

}
