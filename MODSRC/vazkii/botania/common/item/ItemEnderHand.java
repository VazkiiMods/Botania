/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 31, 2014, 7:19:26 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibItemNames;

public class ItemEnderHand extends ItemMod implements IManaUsingItem {

	private static final int COST_SELF = 250;
	private static final int COST_OTHER = 5000;

	public ItemEnderHand() {
		setMaxDamage(1);
		setUnlocalizedName(LibItemNames.ENDER_HAND);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(ManaItemHandler.requestManaExact(stack, player, COST_SELF, false)) {
			player.displayGUIChest(player.getInventoryEnderChest());
			ManaItemHandler.requestManaExact(stack, player, COST_SELF, true);
			world.playSoundAtEntity(player, "mob.endermen.portal", 1F, 1F);
		}
		return stack;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer iplayer, EntityLivingBase entity) {
		if(ConfigHandler.enderPickpocketEnabled && entity instanceof EntityPlayer && ManaItemHandler.requestManaExact(stack, iplayer, COST_OTHER, false)) {
			iplayer.displayGUIChest(((EntityPlayer) entity).getInventoryEnderChest());
			ManaItemHandler.requestManaExact(stack, iplayer, COST_OTHER, true);
			iplayer.worldObj.playSoundAtEntity(iplayer, "mob.endermen.portal", 1F, 1F);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
