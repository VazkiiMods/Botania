/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.manaweave;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Lazy;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorManaweave;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemManaweaveArmor extends ItemManasteelArmor {

	public ItemManaweaveArmor(EquipmentSlot type, Settings props) {
		super(type, BotaniaAPI.instance().getManaweaveArmorMaterial(), props);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public BipedEntityModel<?> provideArmorModelForSlot(EquipmentSlot slot) {
		return new ModelArmorManaweave(slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return ConfigHandler.CLIENT.enableArmorModels.getValue() ? ClientProxy.jingleTheBells ? LibResources.MODEL_MANAWEAVE_NEW_HOLIDAY : LibResources.MODEL_MANAWEAVE_NEW : slot == EquipmentSlot.LEGS ? LibResources.MODEL_MANAWEAVE_1 : LibResources.MODEL_MANAWEAVE_0;
	}

	@Nonnull
	@Override
	@Environment(EnvType.CLIENT)
	public String getTranslationKey(ItemStack stack) {
		String name = super.getTranslationKey(stack);
		if (ClientProxy.jingleTheBells) {
			name = name.replaceAll("manaweave", "santaweave");
		}
		return name;
	}

	private static final Lazy<ItemStack[]> armorSet = new Lazy<>(() -> new ItemStack[] {
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
			return stack.getItem() == ModItems.manaweaveHelm;
		case CHEST:
			return stack.getItem() == ModItems.manaweaveChest;
		case LEGS:
			return stack.getItem() == ModItems.manaweaveLegs;
		case FEET:
			return stack.getItem() == ModItems.manaweaveBoots;
		}

		return false;
	}

	@Override
	public MutableText getArmorSetName() {
		return new TranslatableText("botania.armorset.manaweave.name");
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addInformationAfterShift(ItemStack stack, World world, List<Text> list, TooltipContext flags) {
		if (ClientProxy.jingleTheBells) {
			list.add(new TranslatableText("botaniamisc.santaweaveInfo"));
			list.add(new LiteralText(""));
		}

		super.addInformationAfterShift(stack, world, list, flags);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<Text> list) {
		list.add(new TranslatableText("botania.armorset.manaweave.desc0").formatted(Formatting.GRAY));
		list.add(new TranslatableText("botania.armorset.manaweave.desc1").formatted(Formatting.GRAY));
	}
}
