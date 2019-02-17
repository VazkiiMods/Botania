/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 4:09:57 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.EnumDyeColor;

public class Item16Colors extends ItemMod {
	public final EnumDyeColor color;

	public Item16Colors(EnumDyeColor color, Properties props) {
		super(props);
		this.color = color;
	}
}
