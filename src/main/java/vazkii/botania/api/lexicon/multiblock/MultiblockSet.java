/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 27, 2015, 7:31:58 PM (GMT)]
 */
package vazkii.botania.api.lexicon.multiblock;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

import java.util.Collections;
import java.util.Map;

/**
 * A set of Multiblock objects for various rotations.
 */
public class MultiblockSet {

	private final Map<EnumFacing, Multiblock> mbs;

	public MultiblockSet(Multiblock mb) {
		mbs = Collections.unmodifiableMap(mb.createRotations());
	}

	public Multiblock getForEntity(Entity e) {
		return getForFacing(e.getHorizontalFacing());
	}

	public Multiblock getForFacing(EnumFacing facing) {
		return mbs.get(facing);
	}

}
