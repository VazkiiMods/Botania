/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 18, 2015, 12:10:00 AM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaPool;

/**
 * An item that implements this will have a function called when its standing atop
 * a mana pool.
 */
public interface IManaDissolvable {

	/**
	 * Called for every tick the item is on a mana pool. If the stack has stack
	 * size 0 the item is killed. This is called serverside.
	 */
	public void onDissolveTick(IManaPool pool, ItemStack stack, EntityItem item);

}
