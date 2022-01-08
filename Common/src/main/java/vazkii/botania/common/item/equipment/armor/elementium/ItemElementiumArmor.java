/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.elementium;

import com.google.common.base.Suppliers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import java.util.List;
import java.util.function.Supplier;

public abstract class ItemElementiumArmor extends ItemManasteelArmor {

	public ItemElementiumArmor(EquipmentSlot type, Properties props) {
		super(type, BotaniaAPI.instance().getElementiumArmorMaterial(), props);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return LibResources.MODEL_ELEMENTIUM_NEW;
	}

	private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
			new ItemStack(ModItems.elementiumHelm),
			new ItemStack(ModItems.elementiumChest),
			new ItemStack(ModItems.elementiumLegs),
			new ItemStack(ModItems.elementiumBoots)
	});

	@Override
	public ItemStack[] getArmorSetStacks() {
		return armorSet.get();
	}

	@Override
	public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
		if (player == null) {
			return false;
		}

		ItemStack stack = player.getItemBySlot(slot);
		if (stack.isEmpty()) {
			return false;
		}

		return switch (slot) {
			case HEAD -> stack.is(ModItems.elementiumHelm);
			case CHEST -> stack.is(ModItems.elementiumChest);
			case LEGS -> stack.is(ModItems.elementiumLegs);
			case FEET -> stack.is(ModItems.elementiumBoots);
			default -> false;
		};

	}

	@Override
	public MutableComponent getArmorSetName() {
		return new TranslatableComponent("botania.armorset.elementium.name");
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		super.addArmorSetDescription(stack, list);
		list.add(new TranslatableComponent("botania.armorset.elementium.desc").withStyle(ChatFormatting.GRAY));
	}

}
