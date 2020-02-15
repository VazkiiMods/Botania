package vazkii.botania.common.item.equipment.armor.elementium;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.common.core.handler.PixieHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemElementiumHelm extends ItemElementiumArmor implements IManaDiscountArmor {
	public ItemElementiumHelm(Properties props) {
		super(EquipmentSlotType.HEAD, props);
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot) {
		Multimap<String, AttributeModifier> ret = super.getAttributeModifiers(slot);
		if (slot == getEquipmentSlot()) {
			ret.put(PixieHandler.PIXIE_SPAWN_CHANCE.getName(), PixieHandler.makeModifier(slot, "Armor modifier", 0.11));
		}
		return ret;
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, PlayerEntity player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.1F : 0F;
	}

}
