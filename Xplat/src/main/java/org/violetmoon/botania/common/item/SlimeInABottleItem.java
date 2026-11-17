/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.helper.DataComponentHelper;
import org.violetmoon.botania.common.internal_caps.SlimeChunkSpawned;

public class SlimeInABottleItem extends Item {

	public SlimeInABottleItem(Properties builder) {
		super(builder);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (!level.isClientSide()) {
			boolean slime = SlimeChunkSpawned.isSlimeChunk(level, entity.blockPosition());
			DataComponentHelper.setFlag(stack, BotaniaDataComponents.ACTIVE_TRANSIENT, slime);
		}
	}
}
