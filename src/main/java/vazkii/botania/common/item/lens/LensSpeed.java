/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 24, 2015, 4:32:47 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.BurstProperties;

public class LensSpeed extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.motionModifier *= 2F;
		props.maxMana *= 0.75F;
		props.ticksBeforeManaLoss /= 3F;
		props.manaLossPerTick *= 2F;
	}
	
}
