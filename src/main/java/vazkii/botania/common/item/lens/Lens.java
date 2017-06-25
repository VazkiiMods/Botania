/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 4:31:16 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaSpreader;

public class Lens  {

	public void apply(ItemStack stack, BurstProperties props) {}

	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return dead;
	}

	public void updateBurst(IManaBurst burst, EntityThrowable entity, ItemStack stack) {}

	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		return true;
	}

	public void onControlledSpreaderTick(ItemStack stack, IManaSpreader spreader, boolean redstone) {}

	public void onControlledSpreaderPulse(ItemStack stack, IManaSpreader spreader, boolean redstone) {}
	
	public int getManaToTransfer(IManaBurst burst, EntityThrowable entity, ItemStack stack, IManaReceiver receiver) {
		return burst.getMana();
	}

}
