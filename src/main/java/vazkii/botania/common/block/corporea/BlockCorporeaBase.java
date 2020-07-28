/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;

import vazkii.botania.common.block.BlockMod;

public abstract class BlockCorporeaBase extends BlockMod implements ITileEntityProvider {

	public BlockCorporeaBase(Block.Properties builder) {
		super(builder);
	}

}
