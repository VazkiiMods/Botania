/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemModPattern extends Item {
	// todo 1.18
	public ItemModPattern(LoomPattern pattern, Properties settings) {
		super(settings);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> lines, TooltipFlag ctx) {
		//no-op
	}
}
