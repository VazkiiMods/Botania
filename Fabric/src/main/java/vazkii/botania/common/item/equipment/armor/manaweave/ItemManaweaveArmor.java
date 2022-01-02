/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.manaweave;

import com.google.common.base.Suppliers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.function.Supplier;

public class ItemManaweaveArmor extends ItemManasteelArmor {

	public ItemManaweaveArmor(EquipmentSlot type, Properties props) {
		super(type, BotaniaAPI.instance().getManaweaveArmorMaterial(), props);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return ClientProxy.jingleTheBells ? LibResources.MODEL_MANAWEAVE_NEW_HOLIDAY : LibResources.MODEL_MANAWEAVE_NEW;
	}

	@Nonnull
	@Override
	public String getDescriptionId(ItemStack stack) {
		String name = super.getDescriptionId(stack);
		if (ClientProxy.jingleTheBells) {
			name = name.replaceAll("manaweave", "santaweave");
		}
		return name;
	}

	private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
			new ItemStack(ModItems.manaweaveHelm),
			new ItemStack(ModItems.manaweaveChest),
			new ItemStack(ModItems.manaweaveLegs),
			new ItemStack(ModItems.manaweaveBoots)
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
			case HEAD -> stack.is(ModItems.manaweaveHelm);
			case CHEST -> stack.is(ModItems.manaweaveChest);
			case LEGS -> stack.is(ModItems.manaweaveLegs);
			case FEET -> stack.is(ModItems.manaweaveBoots);
			default -> false;
		};

	}

	@Override
	public MutableComponent getArmorSetName() {
		return new TranslatableComponent("botania.armorset.manaweave.name");
	}

	@Override
	public void addInformationAfterShift(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
		if (ClientProxy.jingleTheBells) {
			list.add(new TranslatableComponent("botaniamisc.santaweaveInfo"));
			list.add(new TextComponent(""));
		}

		super.addInformationAfterShift(stack, world, list, flags);
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		list.add(new TranslatableComponent("botania.armorset.manaweave.desc0").withStyle(ChatFormatting.GRAY));
		list.add(new TranslatableComponent("botania.armorset.manaweave.desc1").withStyle(ChatFormatting.GRAY));
	}
}
