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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModModelLayers;
import vazkii.botania.client.model.armor.ModelArmorElementium;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import java.util.List;

public abstract class ItemElementiumArmor extends ItemManasteelArmor {

	public ItemElementiumArmor(EquipmentSlot type, Properties props) {
		super(type, BotaniaAPI.instance().getElementiumArmorMaterial(), props);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected HumanoidModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
		var entityModels = Minecraft.getInstance().getEntityModels();
		var root = entityModels.bakeLayer(slot == EquipmentSlot.LEGS
				? ModModelLayers.ELEMENTIUM_INNER_ARMOR
				: ModModelLayers.ELEMENTIUM_OUTER_ARMOR);
		return new ModelArmorElementium(root, slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return LibResources.MODEL_ELEMENTIUM_NEW;
	}

	private static final LazyLoadedValue<ItemStack[]> armorSet = new LazyLoadedValue<>(() -> new ItemStack[] {
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
	public MutableComponent getArmorSetName() {
		return new TranslatableComponent("botania.armorset.elementium.name");
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		super.addArmorSetDescription(stack, list);
		list.add(new TranslatableComponent("botania.armorset.elementium.desc").withStyle(ChatFormatting.GRAY));
	}

}
