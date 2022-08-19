/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 29, 2015, 7:17:35 PM (GMT)]
 */
package vazkii.botania.common.block.decor.biomestone;

import vazkii.botania.common.lib.LibBlockNames;

public class BlockBiomeStoneA extends BlockBiomeStone {

	public BlockBiomeStoneA() {
		super(0, LibBlockNames.BIOME_STONE_A);
	}

	@Override
	public int damageDropped(int par1) {
		return par1 % 8 + 8;
	}

}
