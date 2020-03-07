/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.item.DyeColor;

import vazkii.botania.common.block.BlockMod;

public class BlockPetalBlock extends BlockMod {

	public final DyeColor color;

	public BlockPetalBlock(DyeColor color, Properties builder) {
		super(builder);
		this.color = color;
	}
}
