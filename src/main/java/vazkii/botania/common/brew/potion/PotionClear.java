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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibPotionNames;

import javax.annotation.Nonnull;

public class PotionClear extends PotionMod {

	public PotionClear() {
		super(LibPotionNames.CLEAR, false, 0xFFFFFF, 0);
	}

	@Override
	public boolean isInstant() {
		return true;
	}

	@Override
	public void affectEntity(Entity e, Entity e1, @Nonnull EntityLivingBase e2, int t, double d) {
		e2.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
	}

}
