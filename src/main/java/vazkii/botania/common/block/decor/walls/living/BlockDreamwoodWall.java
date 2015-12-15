/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2015, 8:29:16 PM (GMT)]
 */
package vazkii.botania.common.block.decor.walls.living;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.walls.BlockModWall;

public class BlockDreamwoodWall extends BlockModWall {

	public BlockDreamwoodWall() {
		super(ModBlocks.dreamwood, 0);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
	}

}
