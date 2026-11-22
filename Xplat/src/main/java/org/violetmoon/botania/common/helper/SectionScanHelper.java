/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Block scans over large areas that skip whole chunk sections.
 * <p>
 * Several flowers called {@link Level#getBlockState} on every position of a big box, often every few ticks and
 * also when nothing in range could match. This helper visits the positions of the box in the same order as
 * {@link BlockPos#betweenClosed} (x fastest, then y, then z), but skips sections whose palette can't contain a
 * matching state, and reads the other states straight from their section. The matches, and their order, are the same
 * as with a plain loop over {@link Level#getBlockState}, so random picks that depend on the order stay the same.
 * Chunks are fetched like {@link Level#getBlockState} fetches them.
 */
public final class SectionScanHelper {
	private SectionScanHelper() {}

	/**
	 * Calls {@code action} for every position in the box from {@code min} to {@code max} (inclusive) whose block
	 * state matches {@code filter}, in {@link BlockPos#betweenClosed} order. The position passed to {@code action} is
	 * mutable and reused; call {@link BlockPos#immutable()} to keep it. {@code action} may change blocks; the filter
	 * has been applied to the state before that change.
	 */
	public static void forEachMatching(Level level, BlockPos min, BlockPos max, Predicate<BlockState> filter,
			BiConsumer<BlockPos.MutableBlockPos, BlockState> action) {
		if (level.isDebug()) {
			// the debug world makes up its states, use the regular lookup
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
			for (BlockPos p : BlockPos.betweenClosed(min, max)) {
				BlockState state = level.getBlockState(p);
				if (filter.test(state)) {
					action.accept(pos.set(p), state);
				}
			}
			return;
		}

		int minSx = SectionPos.blockToSectionCoord(min.getX());
		int minSy = SectionPos.blockToSectionCoord(min.getY());
		int minSz = SectionPos.blockToSectionCoord(min.getZ());
		int maxSx = SectionPos.blockToSectionCoord(max.getX());
		int maxSy = SectionPos.blockToSectionCoord(max.getY());
		int maxSz = SectionPos.blockToSectionCoord(max.getZ());
		int sizeX = maxSx - minSx + 1;
		int sizeY = maxSy - minSy + 1;
		int sizeZ = maxSz - minSz + 1;

		// per section: the section to read, or null to skip it
		LevelChunkSection[] sections = new LevelChunkSection[sizeX * sizeY * sizeZ];
		// per section row outside the build height: whether void air matches (it does for no filter in practice)
		BlockState voidAir = Blocks.VOID_AIR.defaultBlockState();
		boolean voidAirMatches = filter.test(voidAir);
		boolean[] outside = new boolean[sizeY];
		for (int sy = minSy; sy <= maxSy; sy++) {
			int index = level.getSectionIndexFromSectionY(sy);
			outside[sy - minSy] = index < 0 || index >= level.getSectionsCount();
		}
		for (int sz = minSz; sz <= maxSz; sz++) {
			for (int sx = minSx; sx <= maxSx; sx++) {
				LevelChunk chunk = level.getChunk(sx, sz);
				LevelChunkSection[] chunkSections = chunk.getSections();
				for (int sy = minSy; sy <= maxSy; sy++) {
					if (outside[sy - minSy]) {
						continue;
					}
					LevelChunkSection section = chunkSections[level.getSectionIndexFromSectionY(sy)];
					if (section.maybeHas(filter)) {
						sections[((sz - minSz) * sizeY + (sy - minSy)) * sizeX + (sx - minSx)] = section;
					}
				}
			}
		}

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int z = min.getZ(); z <= max.getZ(); z++) {
			int sz = SectionPos.blockToSectionCoord(z) - minSz;
			for (int y = min.getY(); y <= max.getY(); y++) {
				int syRel = SectionPos.blockToSectionCoord(y) - minSy;
				if (outside[syRel]) {
					if (voidAirMatches) {
						for (int x = min.getX(); x <= max.getX(); x++) {
							action.accept(pos.set(x, y, z), voidAir);
						}
					}
					continue;
				}
				for (int x = min.getX(); x <= max.getX(); x++) {
					LevelChunkSection section = sections[(sz * sizeY + syRel) * sizeX
							+ (SectionPos.blockToSectionCoord(x) - minSx)];
					if (section == null) {
						continue;
					}
					BlockState state = section.getBlockState(x & 15, y & 15, z & 15);
					if (filter.test(state)) {
						action.accept(pos.set(x, y, z), state);
					}
				}
			}
		}
	}
}
