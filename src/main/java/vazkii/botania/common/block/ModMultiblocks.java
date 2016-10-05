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

import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.entity.EntityDoppleganger;

public final class ModMultiblocks {

	public static MultiblockSet enchanter;
	public static MultiblockSet alfPortal;
	public static MultiblockSet terrasteelPlate;
	public static MultiblockSet gaiaRitual;

	public static void init() {
		enchanter = TileEnchanter.makeMultiblockSet();
		alfPortal = TileAlfPortal.makeMultiblockSet();
		terrasteelPlate = TileTerraPlate.makeMultiblockSet();
		gaiaRitual = EntityDoppleganger.makeMultiblockSet();
	}

}
