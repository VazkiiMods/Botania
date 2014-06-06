/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 6, 2014, 9:57:19 PM (GMT)]
 */
package vazkii.botania.common.block.subtile;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibMisc;

public class SubTileManastar extends SubTileEntity {

	int manaLastTick = -1;
	
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		int mana = 0;
		for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) { 
			TileEntity tile = supertile.getWorldObj().getTileEntity(supertile.xCoord + dir.offsetX, supertile.yCoord, supertile.zCoord + dir.offsetZ);
			if(tile instanceof IManaPool)
				mana += ((IManaPool) tile).getCurrentMana();
		}

		if(manaLastTick != -1 && mana != manaLastTick) {
			boolean more = mana > manaLastTick;
			Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.yCoord + 0.75 + Math.random() * 0.2 - 0.1, supertile.zCoord + 0.5, more ? 0.05F : 1F, 0.05F, more ? 1F : 0.05F, (float) Math.random() / 5, (float) -Math.random() / 30);
		}

		manaLastTick = mana;
	}
	
}
