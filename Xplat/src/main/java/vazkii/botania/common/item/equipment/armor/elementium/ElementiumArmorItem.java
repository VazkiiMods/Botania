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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.handler.PixieHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

import java.util.List;
import java.util.function.Supplier;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ElementiumArmorItem extends ManasteelArmorItem {
	private final double pixieChance;

	public ElementiumArmorItem(Type type, double pixieChance, Properties properties) {
		super(type, BotaniaAPI.instance().getElementiumArmorMaterial(), properties);
		this.pixieChance = pixieChance;
	}

	@Override
	public ResourceLocation getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return ResourceLocation.parse(ResourcesLib.MODEL_ELEMENTIUM_NEW);
	}

	private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
			new ItemStack(BotaniaItems.ELEMENTIUM_HELMET),
			new ItemStack(BotaniaItems.ELEMENTIUM_CHESTPLATE),
			new ItemStack(BotaniaItems.ELEMENTIUM_LEGGINGS),
			new ItemStack(BotaniaItems.ELEMENTIUM_BOOTS)
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
			case HEAD -> stack.is(BotaniaItems.ELEMENTIUM_HELMET);
			case CHEST -> stack.is(BotaniaItems.ELEMENTIUM_CHESTPLATE);
			case LEGS -> stack.is(BotaniaItems.ELEMENTIUM_LEGGINGS);
			case FEET -> stack.is(BotaniaItems.ELEMENTIUM_BOOTS);
			default -> false;
		};

	}

	@Override
	public MutableComponent getArmorSetName() {
		return Component.translatable("botania.armorset.elementium.name");
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		super.addArmorSetDescription(stack, list);
		list.add(Component.translatable("botania.armorset.elementium.desc").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers() {
		return super.getDefaultAttributeModifiers()
				.withModifierAdded(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(
						botaniaRL("armor." + type.getName()), pixieChance),
						EquipmentSlotGroup.bySlot(type.getSlot()));
	}

}
