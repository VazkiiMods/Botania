/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.internal.Colored;

public class PetalBlock extends Block implements Colored {

	public final DyeColor color;

	public PetalBlock(DyeColor color, Properties properties) {
		super(properties);
		this.color = color;
	}

	@Override
	public DyeColor getColor() {
		return color;
	}
}
