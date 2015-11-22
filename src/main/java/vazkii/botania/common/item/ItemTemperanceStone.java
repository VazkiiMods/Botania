/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 12, 2015, 5:56:45 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTemperanceStone extends ItemMod {

	public ItemTemperanceStone() {
		setUnlocalizedName(LibItemNames.TEMPERANCE_STONE);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		int dmg = par1ItemStack.getItemDamage();
		par1ItemStack.setItemDamage(~dmg & 1);
		par2World.playSoundAtEntity(par3EntityPlayer, "random.orb", 0.3F, 0.1F);

		return par1ItemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(par1ItemStack.getItemDamage() == 1)
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.active"), par3List);
		else addStringToTooltip(StatCollector.translateToLocal("botaniamisc.inactive"), par3List);
	}

	void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	public static boolean hasTemperanceActive(EntityPlayer player) {
		IInventory inv = player.inventory;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null && stack.getItem() == ModItems.temperanceStone && stack.getItemDamage() == 1)
				return true;
		}

		return false;
	}

}
