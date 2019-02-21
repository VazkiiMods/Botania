package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nullable;

public class ItemElementiumHelm extends ItemElementiumArmor implements IManaDiscountArmor {
	public ItemElementiumHelm(Properties props) {
		super(EntityEquipmentSlot.HEAD, props);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.11F;
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, EntityPlayer player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.1F : 0F;
	}

}
