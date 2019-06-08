package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

public class ItemElementiumBoots extends ItemElementiumArmor {

	public ItemElementiumBoots(Properties props) {
		super(EquipmentSlotType.FEET, props);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.09F;
	}

}
