/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 28, 2014, 6:18:05 PM (GMT)]
 */
package vazkii.botania.common.item.interaction.thaumcraft;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelHelm;
import vazkii.botania.common.lib.LibItemNames;

@Optional.InterfaceList({
	@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.items.IGoggles", striprefs = true),
	@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.items.IRevealer", striprefs = true)})
public class ItemManasteelHelmRevealing extends ItemManasteelHelm implements IGoggles, IRevealer {

	public ItemManasteelHelmRevealing() {
		super(LibItemNames.MANASTEEL_HELM_R);
	}

	@Override
	public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EntityEquipmentSlot slot) {
		return ConfigHandler.enableArmorModels ? LibResources.MODEL_MANASTEEL_NEW : LibResources.MODEL_MANASTEEL_2;
	}

}