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
	public ICorporeaNode getNode(Level world, ICorporeaSpark spark) {
		BlockPos pos = spark.getAttachPos();

		// [VanillaCopy] HopperBlockEntity
		Container inventory = null;
		BlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();
		if (block instanceof WorldlyContainerHolder) {
			inventory = ((WorldlyContainerHolder) block).getContainer(blockState, world, pos);
		} else if (block.isEntityBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Container) {
				inventory = (Container) blockEntity;
				if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
					inventory = ChestBlock.getContainer((ChestBlock) block, blockState, world, pos, true);
				}
			}
		}

		if (inventory instanceof WorldlyContainer) {
			return new SidedVanillaCorporeaNode(world, spark.getAttachPos(), spark, (WorldlyContainer) inventory, Direction.UP);
		} else if (inventory != null) {
			return new VanillaCorporeaNode(world, spark.getAttachPos(), inventory, spark);
		}
		return null;
	}
}
