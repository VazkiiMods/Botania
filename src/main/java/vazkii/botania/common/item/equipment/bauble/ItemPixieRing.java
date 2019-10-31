/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 20, 2014, 6:13:45 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IPixieSpawner;

public class ItemPixieRing extends ItemBauble implements IPixieSpawner {

	public ItemPixieRing(Properties props) {
		super(props);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.25F;
	}

}
