/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana.spark;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.SparkAttachable;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * A block entity with this capability can have a mana spark attached to it.
 * For the Spark to be allowed to have upgrades, the same block position must also have an ManaPool capability
 */
public interface ManaSparkAttachable extends SparkAttachable<ManaSpark> {

	ResourceLocation ID = botaniaRL("mana_spark_attachable");
	BlockApiNoContext<ManaSparkAttachable> LOOKUP = new BlockApiNoContext<>(ID, ManaSparkAttachable.class);

	/**
	 * Returns how much space for mana is available in this block, normally the total - the current.
	 * Should NEVER return negative values. Make sure to check against that.
	 */
	int getAvailableSpaceForMana();

	/**
	 * Return true if this Tile no longer requires mana and all Sparks
	 * transferring mana to it should cancel their transfer.
	 */
	boolean areIncomingTransfersDone();
}
