/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.impl.corporea.ForgeCapCorporeaNode;

import javax.annotation.Nullable;

public class ForgeCapNodeDetector implements ICorporeaNodeDetector {
	@Nullable
	@Override
	public ICorporeaNode getNode(World world, ICorporeaSpark spark) {
		IItemHandler inv = InventoryHelper.getInventory(world, spark.getAttachPos(), Direction.UP);
		if (inv != null) {
			return new ForgeCapCorporeaNode(world, spark.getAttachPos(), inv, spark);
		}
		return null;
	}
}
