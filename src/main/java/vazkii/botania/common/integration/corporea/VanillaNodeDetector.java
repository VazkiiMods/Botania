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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaNodeDetector;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.impl.corporea.ForgeCapCorporeaNode;

import javax.annotation.Nullable;

public class VanillaNodeDetector implements ICorporeaNodeDetector {
	@Nullable
	@Override
	public ICorporeaNode getNode(World world, ICorporeaSpark spark) {
		BlockEntity te = world.getBlockEntity(spark.getAttachPos());
		if (te instanceof SidedInventory) {
			return new ForgeCapCorporeaNode(world, spark.getAttachPos(), new SidedInvWrapper((ISidedInventory) te, Direction.UP), spark);
		} else if (te instanceof Inventory) {
			return new ForgeCapCorporeaNode(world, spark.getAttachPos(), new InvWrapper((IInventory) te), spark);
		}
		return null;
	}
}
