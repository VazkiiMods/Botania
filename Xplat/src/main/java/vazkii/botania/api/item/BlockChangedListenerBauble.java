/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Interface for trinkets that want to receive block change events, similar to certain enchantments on equipment.
 */
public interface BlockChangedListenerBauble {
	void onChangedBlock(ItemStack stack, LivingEntity entity, ServerLevel level, BlockPos pos);
}
