/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 6:09:29 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

public class BlockManaGlass extends AbstractGlassBlock {

	public BlockManaGlass(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

}
