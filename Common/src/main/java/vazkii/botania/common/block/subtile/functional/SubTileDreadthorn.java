/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.ModSubtiles;

import java.util.function.Predicate;

public class SubTileDreadthorn extends SubTileBellethorn {
	public SubTileDreadthorn(BlockPos pos, BlockState state) {
		super(ModSubtiles.DREADTHORN, pos, state);
	}

	@Override
	public int getColor() {
		return 0x260B45;
	}

	@Override
	public Predicate<Entity> getSelector() {
		return var1 -> var1 instanceof Animal && !((Animal) var1).isBaby();
	}

	@Override
	public int getManaCost() {
		return 30;
	}

}
