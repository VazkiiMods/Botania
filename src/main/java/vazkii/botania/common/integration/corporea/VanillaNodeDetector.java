/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
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
		BlockEntity te = world.getBlockEntity(spark.getAttachPos());
		if (te instanceof SidedInventory) {
			return new SidedVanillaCorporeaNode(world, spark.getAttachPos(), spark, (SidedInventory) te, Direction.UP);
		} else if (te instanceof Inventory) {
			return new VanillaCorporeaNode(world, spark.getAttachPos(), (Inventory) te, spark);
		}
		return null;
	}
}
