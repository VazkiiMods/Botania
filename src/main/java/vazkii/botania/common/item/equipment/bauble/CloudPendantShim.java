/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [22/10/2016, 17:31:04 (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract class CloudPendantShim extends ItemBauble {

	public CloudPendantShim(String name) {
		super(name);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		clientWornTick(stack, player);
	}

	public void clientWornTick(ItemStack stack, EntityLivingBase player) {
		// NO-OP
	}

	public int getMaxAllowedJumps() {
		return 2;
	}

}
