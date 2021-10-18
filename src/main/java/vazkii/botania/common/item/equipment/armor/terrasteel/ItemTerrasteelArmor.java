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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModModelLayers;
import vazkii.botania.client.model.armor.ModelArmor;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ItemTerrasteelArmor extends ItemManasteelArmor {

	public ItemTerrasteelArmor(EquipmentSlot type, Properties props) {
		super(type, BotaniaAPI.instance().getTerrasteelArmorMaterial(), props);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected HumanoidModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
		var entityModels = Minecraft.getInstance().getEntityModels();
		var root = entityModels.bakeLayer(slot == EquipmentSlot.LEGS
				? ModModelLayers.TERRASTEEL_INNER_ARMOR
				: ModModelLayers.TERRASTEEL_OUTER_ARMOR);
		return new ModelArmor(root, slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return LibResources.MODEL_TERRASTEEL_NEW;
	}

	@Nonnull
	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot slot) {
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
		return new TranslatableComponent("botania.armorset.terrasteel.name");
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		list.add(new TranslatableComponent("botania.armorset.terrasteel.desc0").withStyle(ChatFormatting.GRAY));
		list.add(new TranslatableComponent("botania.armorset.terrasteel.desc1").withStyle(ChatFormatting.GRAY));
		list.add(new TranslatableComponent("botania.armorset.terrasteel.desc2").withStyle(ChatFormatting.GRAY));
	}
}
