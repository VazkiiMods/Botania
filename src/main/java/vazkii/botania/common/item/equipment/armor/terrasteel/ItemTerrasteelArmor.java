/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 14, 2014, 2:58:13 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
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

	public ItemTerrasteelArmor(EntityEquipmentSlot type, Properties props) {
		super(type, BotaniaAPI.TERRASTEEL_ARMOR_MAT, props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ModelBiped provideArmorModelForSlot(ItemStack stack, EntityEquipmentSlot slot) {
		models.put(slot, new ModelArmorTerrasteel(slot));
		return models.get(slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EntityEquipmentSlot slot) {
		return ConfigHandler.CLIENT.enableArmorModels.get() ? LibResources.MODEL_TERRASTEEL_NEW : slot == EntityEquipmentSlot.CHEST ? LibResources.MODEL_TERRASTEEL_1 : LibResources.MODEL_TERRASTEEL_0;
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		UUID uuid = new UUID((getTranslationKey(stack) + slot.toString()).hashCode(), 0);
		if (slot == armorType) {
			int reduction = material.getDamageReductionAmount(slot);
			multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(uuid, "Terrasteel modifier " + type, (double) reduction / 20, 0));
		}
		return multimap;
	}

	private static ItemStack[] armorset;

	@Override
	public ItemStack[] getArmorSetStacks() {
		if(armorset == null)
			armorset = new ItemStack[] {
					new ItemStack(ModItems.terrasteelHelm),
					new ItemStack(ModItems.terrasteelChest),
					new ItemStack(ModItems.terrasteelLegs),
					new ItemStack(ModItems.terrasteelBoots)
		};

		return armorset;
	}

	@Override
	public boolean hasArmorSetItem(EntityPlayer player, int i) {
		if(player == null || player.inventory == null || player.inventory.armorInventory == null)
			return false;
		
		ItemStack stack = player.inventory.armorInventory.get(3 - i);
		if(stack.isEmpty())
			return false;

		switch(i) {
		case 0: return stack.getItem() == ModItems.terrasteelHelm || stack.getItem() == ModItems.terrasteelHelmRevealing;
		case 1: return stack.getItem() == ModItems.terrasteelChest;
		case 2: return stack.getItem() == ModItems.terrasteelLegs;
		case 3: return stack.getItem() == ModItems.terrasteelBoots;
		}

		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ITextComponent getArmorSetName() {
		return new TextComponentTranslation("botania.armorset.terrasteel.name");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
		list.add(new TextComponentTranslation("botania.armorset.terrasteel.desc0"));
		list.add(new TextComponentTranslation("botania.armorset.terrasteel.desc1"));
		list.add(new TextComponentTranslation("botania.armorset.terrasteel.desc2"));
	}

}
