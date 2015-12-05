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

import java.util.List;
import java.util.UUID;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorTerrasteel;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTerrasteelArmor extends ItemManasteelArmor {

	public ItemTerrasteelArmor(int type, String name) {
		super(type, name, BotaniaAPI.terrasteelArmorMaterial);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped provideArmorModelForSlot(ItemStack stack, int slot) {
		models[slot] = new ModelArmorTerrasteel(slot);
		return models[slot];
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, int slot) {
		return ConfigHandler.enableArmorModels ? LibResources.MODEL_TERRASTEEL_NEW : slot == 2 ? LibResources.MODEL_TERRASTEEL_1 : LibResources.MODEL_TERRASTEEL_0;
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 4 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	public Multimap getItemAttributeModifiers() {
		Multimap multimap = HashMultimap.create();
		UUID uuid = new UUID(getUnlocalizedName().hashCode(), 0);
		multimap.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(uuid, "Terrasteel modifier " + type, (double) getArmorDisplay(null, new ItemStack(this), type) / 20, 0));
		return multimap;
	}

	static ItemStack[] armorset;

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
		ItemStack stack = player.inventory.armorInventory[3 - i];
		if(stack == null)
			return false;

		switch(i) {
		case 0: return stack.getItem() == ModItems.terrasteelHelm || stack.getItem() == ModItems.terrasteelHelmRevealing;
		case 1: return stack.getItem() == ModItems.terrasteelChest;
		case 2: return stack.getItem() == ModItems.terrasteelLegs;
		case 3: return stack.getItem() == ModItems.terrasteelBoots;
		}

		return false;
	}

	@Override
	public String getArmorSetName() {
		return StatCollector.translateToLocal("botania.armorset.terrasteel.name");
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, List<String> list) {
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.terrasteel.desc0"), list);
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.terrasteel.desc1"), list);
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.terrasteel.desc2"), list);
	}

}
