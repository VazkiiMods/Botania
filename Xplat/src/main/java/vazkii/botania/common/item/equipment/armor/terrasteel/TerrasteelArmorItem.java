/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import com.google.common.base.Suppliers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class TerrasteelArmorItem extends ManasteelArmorItem {

	public TerrasteelArmorItem(EquipmentSlot type, Properties props) {
		super(type, BotaniaAPI.instance().getTerrasteelArmorMaterial(), props);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return ResourcesLib.MODEL_TERRASTEEL_NEW;
	}

	@NotNull
	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
		Multimap<Attribute, AttributeModifier> ret = super.getDefaultAttributeModifiers(slot);
		UUID uuid = new UUID(Registry.ITEM.getKey(this).hashCode() + slot.toString().hashCode(), 0);
		if (slot == getSlot()) {
			ret = HashMultimap.create(ret);
			int reduction = getMaterial().getDefenseForSlot(slot);
			ret.put(Attributes.KNOCKBACK_RESISTANCE,
					new AttributeModifier(uuid, "Terrasteel modifier " + type, (double) reduction / 20, AttributeModifier.Operation.ADDITION));
		}
		return ret;
	}

	private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
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
	public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
		if (player == null) {
			return false;
		}

		ItemStack stack = player.getItemBySlot(slot);
		if (stack.isEmpty()) {
			return false;
		}

		return switch (slot) {
			case HEAD -> stack.is(ModItems.terrasteelHelm);
			case CHEST -> stack.is(ModItems.terrasteelChest);
			case LEGS -> stack.is(ModItems.terrasteelLegs);
			case FEET -> stack.is(ModItems.terrasteelBoots);
			default -> false;
		};

	}

	@Override
	public MutableComponent getArmorSetName() {
		return Component.translatable("botania.armorset.terrasteel.name");
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		list.add(Component.translatable("botania.armorset.terrasteel.desc0").withStyle(ChatFormatting.GRAY));
		list.add(Component.translatable("botania.armorset.terrasteel.desc1").withStyle(ChatFormatting.GRAY));
		list.add(Component.translatable("botania.armorset.terrasteel.desc2").withStyle(ChatFormatting.GRAY));
	}

	@SoftImplement("IForgeItem")
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return true;
	}
}
