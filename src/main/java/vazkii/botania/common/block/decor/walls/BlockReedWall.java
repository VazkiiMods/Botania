/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2015, 8:31:04 PM (GMT)]
 */
package vazkii.botania.common.block.decor.walls;

import vazkii.botania.common.block.ModBlocks;

public class BlockReedWall extends BlockModWall {

	public BlockReedWall() {
		super(ModBlocks.reedBlock, 0);
		setHardness(1.0F);
		setStepSound(soundTypeWood);
	}

}
