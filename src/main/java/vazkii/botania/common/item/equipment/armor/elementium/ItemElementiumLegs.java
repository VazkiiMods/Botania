package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class ItemElementiumLegs extends ItemElementiumArmor {

	public ItemElementiumLegs(Properties props) {
		super(EquipmentSlotType.LEGS, props);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.15F;
	}

}
