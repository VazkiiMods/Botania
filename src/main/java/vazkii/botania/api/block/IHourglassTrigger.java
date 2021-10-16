/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.BotaniaAPI;

/**
 * A block with this capability will receive a custom callback when a Hovering
 * Hourglass adjacent to the block turns.
 */
public interface IHourglassTrigger {
	BlockApiLookup<IHourglassTrigger, Unit> API = BlockApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "hourglass_trigger"), IHourglassTrigger.class, Unit.class);

	void onTriggeredByHourglass(BlockEntity hourglass);

}
