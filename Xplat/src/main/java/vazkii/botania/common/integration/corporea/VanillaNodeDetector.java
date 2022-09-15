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

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.corporea.CorporeaNode;
import vazkii.botania.api.corporea.CorporeaNodeDetector;
import vazkii.botania.api.corporea.CorporeaSpark;
import vazkii.botania.common.impl.corporea.SidedVanillaCorporeaNode;
import vazkii.botania.common.impl.corporea.VanillaCorporeaNode;

public class VanillaNodeDetector implements CorporeaNodeDetector {
	@Nullable
	@Override
	public CorporeaNode getNode(Level level, CorporeaSpark spark) {
		// [VanillaCopy] HopperBlockEntity
		Container container = null;
		BlockPos blockPos = spark.getAttachPos();
		BlockState blockState = level.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof WorldlyContainerHolder worldlyContainer) {
			container = worldlyContainer.getContainer(blockState, level, blockPos);
		} else if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if (blockEntity instanceof Container beContainer) {
				container = beContainer;
				if (container instanceof ChestBlockEntity && block instanceof ChestBlock chest) {
					container = ChestBlock.getContainer(chest, blockState, level, blockPos, true);
				}
			}
		}

		if (container instanceof WorldlyContainer worldlyContainer) {
			return new SidedVanillaCorporeaNode(level, spark.getAttachPos(), spark, worldlyContainer, Direction.UP);
		} else if (container != null) {
			return new VanillaCorporeaNode(level, spark.getAttachPos(), container, spark);
		}
		return null;
	}
}
