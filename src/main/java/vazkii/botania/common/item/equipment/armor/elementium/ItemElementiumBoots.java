package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class ItemElementiumBoots extends ItemElementiumArmor {

	public ItemElementiumBoots(Properties props) {
		super(EquipmentSlotType.FEET, props);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.09F;
	}

}
