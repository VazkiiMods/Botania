/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.internal_caps;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import org.jetbrains.annotations.ApiStatus;

import vazkii.botania.api.attachment.DataMarkerId;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Added to slimes that spawned naturally in a slime chunk.
 */
public final class SlimeChunkSpawned {

	public static final ResourceLocation ID = botaniaRL("slime_chunk_spawned");
	public static final DataMarkerId MARKER = new DataMarkerId(ID);

	public static boolean isSlimeChunk(Level world, BlockPos pos) {
		ChunkPos chunkpos = new ChunkPos(pos);
		return WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((ServerLevel) world).getSeed(), 987234911L).nextInt(10) == 0;
	}

	private static boolean isSlimeChunk(Level world, double x, double z) {
		return isSlimeChunk(world, BlockPos.containing(x, 0, z));
	}

	@ApiStatus.Internal
	public static void onSpawn(Entity entity) {
		boolean slimeChunk = isSlimeChunk(entity.level(), entity.getX(), entity.getZ());
		if (slimeChunk) {
			entity.getSelfAndPassengers().forEach(SlimeChunkSpawned::markSlime);
		}
	}

	private static void markSlime(Entity e) {
		if (e instanceof Slime slime) {
			MARKER.addFor(slime);
		}
	}

	private SlimeChunkSpawned() {}
}
