/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaNodeDetector;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.impl.corporea.SidedVanillaCorporeaNode;
import vazkii.botania.common.impl.corporea.VanillaCorporeaNode;

import javax.annotation.Nullable;

public class VanillaNodeDetector implements ICorporeaNodeDetector {
	@Nullable
	@Override
	public ICorporeaNode getNode(World world, ICorporeaSpark spark) {
		BlockPos pos = spark.getAttachPos();

		// [VanillaCopy] HopperBlockEntity
		Inventory inventory = null;
		BlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();
		if (block instanceof InventoryProvider) {
			inventory = ((InventoryProvider) block).getInventory(blockState, world, pos);
		} else if (block.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory) {
				inventory = (Inventory) blockEntity;
				if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
					inventory = ChestBlock.getInventory((ChestBlock) block, blockState, world, pos, true);
				}
			}
		}

		if (inventory instanceof SidedInventory) {
			return new SidedVanillaCorporeaNode(world, spark.getAttachPos(), spark, (SidedInventory) inventory, Direction.UP);
		} else if (inventory != null) {
			return new VanillaCorporeaNode(world, spark.getAttachPos(), inventory, spark);
		}
		return null;
	}
}
