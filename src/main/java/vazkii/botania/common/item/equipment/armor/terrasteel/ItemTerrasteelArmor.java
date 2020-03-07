/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorTerrasteel;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.UUID;

public class ItemTerrasteelArmor extends ItemManasteelArmor {

	public ItemTerrasteelArmor(EquipmentSlotType type, Properties props) {
		super(type, BotaniaAPI.TERRASTEEL_ARMOR_MAT, props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel provideArmorModelForSlot(EquipmentSlotType slot) {
		return new ModelArmorTerrasteel(slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlotType slot) {
		return ConfigHandler.CLIENT.enableArmorModels.get() ? LibResources.MODEL_TERRASTEEL_NEW : slot == EquipmentSlotType.CHEST ? LibResources.MODEL_TERRASTEEL_1 : LibResources.MODEL_TERRASTEEL_0;
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		UUID uuid = new UUID((getTranslationKey(stack) + slot.toString()).hashCode(), 0);
		if (slot == getEquipmentSlot()) {
			int reduction = material.getDamageReductionAmount(slot);
			multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(),
					new AttributeModifier(uuid, "Terrasteel modifier " + type, (double) reduction / 20, AttributeModifier.Operation.ADDITION));
		}
		return multimap;
	}

	private static final LazyValue<ItemStack[]> armorSet = new LazyValue<>(() -> new ItemStack[] {
			new ItemStack(ModItems.terrasteelHelm),
			new ItemStack(ModItems.terrasteelChest),
			new ItemStack(ModItems.terrasteelLegs),
			new ItemStack(ModItems.terrasteelBoots)
	});

	@Override
	public ItemStack[] getArmorSetStacks() {
		return armorSet.getValue();
	}

	@Override
	public boolean hasArmorSetItem(PlayerEntity player, EquipmentSlotType slot) {
		if (player == null) {
			return false;
		}

		ItemStack stack = player.getItemStackFromSlot(slot);
		if (stack.isEmpty()) {
			return false;
		}

		switch (slot) {
		case HEAD:
			return stack.getItem() == ModItems.terrasteelHelm || stack.getItem() == ModItems.terrasteelHelmRevealing;
		case CHEST:
			return stack.getItem() == ModItems.terrasteelChest;
		case LEGS:
			return stack.getItem() == ModItems.terrasteelLegs;
		case FEET:
			return stack.getItem() == ModItems.terrasteelBoots;
		}

		return false;
	}

	@Override
	public ITextComponent getArmorSetName() {
		return new TranslationTextComponent("botania.armorset.terrasteel.name");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
		list.add(new TranslationTextComponent("botania.armorset.terrasteel.desc0").applyTextStyle(TextFormatting.GRAY));
		list.add(new TranslationTextComponent("botania.armorset.terrasteel.desc1").applyTextStyle(TextFormatting.GRAY));
		list.add(new TranslationTextComponent("botania.armorset.terrasteel.desc2").applyTextStyle(TextFormatting.GRAY));
	}

}
