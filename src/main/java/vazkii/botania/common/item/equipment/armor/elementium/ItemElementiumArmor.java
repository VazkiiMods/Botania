/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.elementium;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Lazy;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorElementium;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import java.util.List;

public abstract class ItemElementiumArmor extends ItemManasteelArmor {

	public ItemElementiumArmor(EquipmentSlot type, Settings props) {
		super(type, BotaniaAPI.instance().getElementiumArmorMaterial(), props);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected BipedEntityModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
		return new ModelArmorElementium(slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return ConfigHandler.CLIENT.enableArmorModels.getValue() ? LibResources.MODEL_ELEMENTIUM_NEW : slot == EquipmentSlot.LEGS ? LibResources.MODEL_ELEMENTIUM_1 : LibResources.MODEL_ELEMENTIUM_0;
	}

	private static final Lazy<ItemStack[]> armorSet = new Lazy<>(() -> new ItemStack[] {
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
	public boolean hasArmorSetItem(PlayerEntity player, EquipmentSlot slot) {
		if (player == null) {
			return false;
		}

		ItemStack stack = player.getEquippedStack(slot);
		if (stack.isEmpty()) {
			return false;
		}

		switch (slot) {
		case HEAD:
			return stack.getItem() == ModItems.elementiumHelm;
		case CHEST:
			return stack.getItem() == ModItems.elementiumChest;
		case LEGS:
			return stack.getItem() == ModItems.elementiumLegs;
		case FEET:
			return stack.getItem() == ModItems.elementiumBoots;
		}

		return false;
	}

	@Override
	public MutableText getArmorSetName() {
		return new TranslatableText("botania.armorset.elementium.name");
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<Text> list) {
		super.addArmorSetDescription(stack, list);
		list.add(new TranslatableText("botania.armorset.elementium.desc").formatted(Formatting.GRAY));
	}

}
