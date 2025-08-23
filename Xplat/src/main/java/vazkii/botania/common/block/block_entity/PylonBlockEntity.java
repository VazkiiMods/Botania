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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class PylonBlockEntity extends BlockEntity {
	public boolean activated;
	@Nullable
	public BlockPos centerPos;

	public PylonBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.PYLON, pos, state);
	}
}
