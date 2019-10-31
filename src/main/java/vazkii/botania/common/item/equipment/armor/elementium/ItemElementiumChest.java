package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class ItemElementiumChest extends ItemElementiumArmor {

	public ItemElementiumChest(Properties props) {
		super(EquipmentSlotType.CHEST, props);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.17F;
	}

}
