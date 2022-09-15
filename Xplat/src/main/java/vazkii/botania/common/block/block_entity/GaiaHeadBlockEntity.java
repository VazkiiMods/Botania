/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.tile.ModTiles;

public class GaiaHeadBlockEntity extends SkullBlockEntity {
	public GaiaHeadBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@NotNull
	@Override
	public BlockEntityType<GaiaHeadBlockEntity> getType() {
		return ModTiles.GAIA_HEAD;
	}

}
