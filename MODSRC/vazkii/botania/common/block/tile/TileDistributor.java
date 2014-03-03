/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 3, 2014, 1:51:34 AM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;

public class TileDistributor extends TileMod implements IManaReceiver {

	List<IManaReceiver> validPools = new ArrayList();
	
	static final ForgeDirection[] DIRECTIONS = new ForgeDirection[] {
		ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST
	};
	
	@Override
	public void updateEntity() {
		validPools.clear();
		for(ForgeDirection dir : DIRECTIONS) {
			TileEntity tileAt = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord, zCoord + dir.offsetZ);
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
				PacketDispatcher.sendPacketToAllInDimension(((TileEntity) pool).getDescriptionPacket(), worldObj.provider.dimensionId);
			}
		}
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull();
	}
}
