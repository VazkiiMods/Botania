/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2015, 3:11:36 PM (GMT)]
 */
package vazkii.botania.common.block;

import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockComponent;
import vazkii.botania.common.block.tile.TileEnchanter;

public final class ModMultiblocks {

	public static Multiblock[] enchanter; 
	
	public static void init() {
		enchanter = TileEnchanter.makeMultiblockInstance();
	}
	
}
