/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaNodeDetector;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.impl.corporea.SidedVanillaCorporeaNode;
import vazkii.botania.common.impl.corporea.VanillaCorporeaNode;

import javax.annotation.Nullable;

public class VanillaNodeDetector implements ICorporeaNodeDetector {
	@Nullable
	@Override
	public ICorporeaNode getNode(Level level, ICorporeaSpark spark) {
		// [VanillaCopy] HopperBlockEntity
		Container container = null;
		BlockPos blockPos = spark.getAttachPos();
		BlockState blockState = level.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof WorldlyContainerHolder) {
			container = ((WorldlyContainerHolder) block).getContainer(blockState, level, blockPos);
		} else if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if (blockEntity instanceof Container) {
				container = (Container) blockEntity;
				if (container instanceof ChestBlockEntity && block instanceof ChestBlock) {
					container = ChestBlock.getContainer((ChestBlock) block, blockState, level, blockPos, true);
				}
			}
		}

		if (container instanceof WorldlyContainer) {
			return new SidedVanillaCorporeaNode(level, spark.getAttachPos(), spark, (WorldlyContainer) container, Direction.UP);
		} else if (container != null) {
			return new VanillaCorporeaNode(level, spark.getAttachPos(), container, spark);
		}
		return null;
	}
}
