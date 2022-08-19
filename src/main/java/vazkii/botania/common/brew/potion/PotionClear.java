/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 25, 2015, 6:08:52 PM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibPotionNames;

public class PotionClear extends PotionMod {

	public PotionClear() {
		super(ConfigHandler.potionIDClear, LibPotionNames.CLEAR, false, 0xFFFFFF, 0);
	}

	@Override
	public boolean isInstant() {
		return true;
	}

	@Override
	public void affectEntity(EntityLivingBase e, EntityLivingBase e1, int t, double d) {
		e1.curePotionEffects(new ItemStack(Items.milk_bucket));
	}

}
