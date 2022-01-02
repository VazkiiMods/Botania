/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.level.Level;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.ITileBound;

import javax.annotation.Nullable;

/**
 * Items with this capability can be bound to a position.
 * That position is highlighted when the item is being held
 *
 * @see ITileBound
 */
public interface ICoordBoundItem {
	ItemApiLookup<ICoordBoundItem, Unit> API = ItemApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "coord_bound_item"),
			ICoordBoundItem.class, Unit.class);

	@Nullable
	BlockPos getBinding(Level world);

}
