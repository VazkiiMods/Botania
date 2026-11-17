/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.impl.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.violetmoon.botania.api.corporea.CorporeaRequest;
import org.violetmoon.botania.api.corporea.CorporeaSpark;

import java.util.Collections;
import java.util.List;

public class DummyCorporeaNode extends AbstractCorporeaNode {
	public DummyCorporeaNode(Level world, BlockPos pos, CorporeaSpark spark) {
		super(world, pos, spark);
	}

	@Override
	public List<ItemStack> countItems(CorporeaRequest request) {
		return Collections.emptyList();
	}

	@Override
	public List<ItemStack> extractItems(CorporeaRequest request) {
		return Collections.emptyList();
	}
}
