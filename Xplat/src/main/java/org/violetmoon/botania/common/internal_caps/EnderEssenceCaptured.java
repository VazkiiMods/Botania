/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.internal_caps;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.attachment.DataHolderId;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Attached to an {@link net.minecraft.world.entity.monster.EnderMan} that is about to be redirected to a Vinculotus for
 * its final teleportation. The held value signifies if the Enderman already completed its "final" teleport. If yes, it
 * will be prevented from future teleportation attempts.
 *
 * @see vazkii.botania.common.block.block_entity.flower.functional.VinculotusBlockEntity
 */
public class EnderEssenceCaptured {

	public static final ResourceLocation ID = botaniaRL("ender_essence_captured");
	public static final DataHolderId<Boolean> HOLDER = new DataHolderId<>(ID, Codec.BOOL);
}
