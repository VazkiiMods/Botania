/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 14, 2014, 3:13:05 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.common.lib.LibItemNames;

public class ItemTerrasteelHelm extends ItemTerrasteelArmor implements IManaDiscountArmor {

	public ItemTerrasteelHelm() {
		this(LibItemNames.TERRASTEEL_HELM);
	}

	public ItemTerrasteelHelm(String name) {
		super(0, name);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		super.onArmorTick(world, player, stack);
		if(!hasArmorSet(player)) {
			int food = player.getFoodStats().getFoodLevel();
	        if(food > 0 && food < 18 && player.shouldHeal() && player.ticksExisted % 80 == 0)
	            player.heal(1F);
		}
	}
	
	@Override
	public float getDiscount(ItemStack stack, int slot, EntityPlayer player) {
		return hasArmorSet(player) ? 0.2F : 0F;
	}

}
