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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;

public class ItemTemperanceStone extends ItemMod {

	public ItemTemperanceStone() {
		super(LibItemNames.TEMPERANCE_STONE);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		int dmg = stack.getItemDamage();
		stack.setItemDamage(~dmg & 1);
		world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.entity_experience_orb_pickup, SoundCategory.NEUTRAL, 0.3F, 0.1F);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		if(par1ItemStack.getItemDamage() == 1)
			addStringToTooltip(I18n.translateToLocal("botaniamisc.active"), par3List);
		else addStringToTooltip(I18n.translateToLocal("botaniamisc.inactive"), par3List);
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
