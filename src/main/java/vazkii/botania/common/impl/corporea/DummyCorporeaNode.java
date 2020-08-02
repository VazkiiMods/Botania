/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.corporea.ICorporeaRequest;
import vazkii.botania.api.corporea.ICorporeaSpark;

import java.util.ArrayList;
import java.util.List;

public class DummyCorporeaNode extends AbstractCorporeaNode {
	public DummyCorporeaNode(World world, BlockPos pos, ICorporeaSpark spark) {
		super(world, pos, spark);
	}

	@Override
	public List<ItemStack> countItems(ICorporeaRequest request) {
		// todo immutable?
		return new ArrayList<>();
	}

	@Override
	public List<ItemStack> extractItems(ICorporeaRequest request) {
		// todo immutable?
		return new ArrayList<>();
	}
}
