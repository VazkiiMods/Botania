package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

public class ItemElementiumLegs extends ItemElementiumArmor {

	public ItemElementiumLegs(Properties props) {
		super(EntityEquipmentSlot.LEGS, props);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.15F;
	}

}
