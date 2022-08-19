/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 3, 2014, 1:51:34 AM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.lib.LibMisc;

public class TileDistributor extends TileMod implements IManaReceiver {

	List<IManaReceiver> validPools = new ArrayList();

	@Override
	public void updateEntity() {
		validPools.clear();
		for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
			TileEntity tileAt = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord, zCoord + dir.offsetZ);
			if(tileAt != null && tileAt instanceof IManaPool) {
				IManaReceiver receiver = (IManaReceiver) tileAt;
				if(!receiver.isFull())
					validPools.add(receiver);
			}
		}
	}

	@Override
	public int getCurrentMana() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return validPools.isEmpty();
	}

	@Override
	public void recieveMana(int mana) {
		int tiles = validPools.size();
		if(tiles != 0) {
			int manaForEach = mana / tiles;
			for(IManaReceiver pool : validPools) {
				pool.recieveMana(manaForEach);
				TileEntity tile = (TileEntity) pool;
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
			}
		}
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull();
	}
}
