/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorTerrasteel;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.UUID;

public class ItemTerrasteelArmor extends ItemManasteelArmor {

	public ItemTerrasteelArmor(EquipmentSlot type, Settings props) {
		super(type, BotaniaAPI.instance().getTerrasteelArmorMaterial(), props);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected BipedEntityModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
		return new ModelArmorTerrasteel(slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return LibResources.MODEL_TERRASTEEL_NEW;
	}

	@Nonnull
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlot slot) {
		Multimap<EntityAttribute, EntityAttributeModifier> ret = super.getAttributeModifiers(slot);
		UUID uuid = new UUID(Registry.ITEM.getId(this).hashCode() + slot.toString().hashCode(), 0);
		if (slot == getSlotType()) {
			ret = HashMultimap.create(ret);
			int reduction = getMaterial().getProtectionAmount(slot);
			ret.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
					new EntityAttributeModifier(uuid, "Terrasteel modifier " + type, (double) reduction / 20, EntityAttributeModifier.Operation.ADDITION));
		}
		return ret;
	}

	private static final Lazy<ItemStack[]> armorSet = new Lazy<>(() -> new ItemStack[] {
			new ItemStack(ModItems.terrasteelHelm),
			new ItemStack(ModItems.terrasteelChest),
			new ItemStack(ModItems.terrasteelLegs),
			new ItemStack(ModItems.terrasteelBoots)
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
			return stack.getItem() == ModItems.terrasteelHelm;
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
	public MutableText getArmorSetName() {
		return new TranslatableText("botania.armorset.terrasteel.name");
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<Text> list) {
		list.add(new TranslatableText("botania.armorset.terrasteel.desc0").formatted(Formatting.GRAY));
		list.add(new TranslatableText("botania.armorset.terrasteel.desc1").formatted(Formatting.GRAY));
		list.add(new TranslatableText("botania.armorset.terrasteel.desc2").formatted(Formatting.GRAY));
	}
}
