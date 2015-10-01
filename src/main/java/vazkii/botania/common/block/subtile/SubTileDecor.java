/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 28, 2015, 10:17:01 PM (GMT)]
 */
package vazkii.botania.common.block.subtile;

import vazkii.botania.api.subtile.SubTileEntity;

public class SubTileDecor extends SubTileEntity {

	@Override
	public boolean canUpdate() {
		return false;
	}

	public static class Daybloom extends SubTileDecor { }
	public static class Nightshade extends SubTileDecor { }
	public static class Hydroangeas extends SubTileDecor { }

}
