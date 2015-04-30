/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 30, 2015, 3:57:57 PM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TileCorporeaCrystalCube extends TileCorporeaBase {

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
	
	@Override
	public String getInventoryName() {
		return LibBlockNames.CORPOREA_CRYSTAL_CUBE;
	}

}
