/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 21, 2014, 8:44:35 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.lib.LibMisc;

import java.util.Locale;

public class ItemManaCookie extends Item {

	public ItemManaCookie(Properties props) {
		super(props);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "totalbiscuit"),
				(stack, worldIn, entityIn) -> stack.getDisplayName().getString().toLowerCase(Locale.ROOT).contains("totalbiscuit") ? 1F : 0F);
	}
}
