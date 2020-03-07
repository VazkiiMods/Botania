/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;

public class Item16Colors extends Item {
	public final DyeColor color;

	public Item16Colors(DyeColor color, Properties props) {
		super(props);
		this.color = color;
	}
}
