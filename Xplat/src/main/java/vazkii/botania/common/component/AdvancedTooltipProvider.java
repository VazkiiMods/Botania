/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.component;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Consumer;

public interface AdvancedTooltipProvider {
	void addToTooltip(
			Item.TooltipContext context,
			Consumer<Component> tooltipAdder,
			TooltipFlag tooltipFlag,
			List<Component> fullTooltip);
}
