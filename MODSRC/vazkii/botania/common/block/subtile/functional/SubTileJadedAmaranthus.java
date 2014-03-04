/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 3, 2014, 10:31:29 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;
import cpw.mods.fml.common.network.PacketDispatcher;

public class SubTileJadedAmaranthus extends SubTileFunctional {

	private static final int COST = 100;
	int cooldown = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(cooldown > 0) {
			cooldown--;
			return;
		}

		if(mana >= COST && !supertile.worldObj.isRemote) {
			int range = 4;
			int x = supertile.xCoord - range + supertile.worldObj.rand.nextInt(range * 2 + 1);
			int y = supertile.yCoord + range;
			int z = supertile.zCoord - range + supertile.worldObj.rand.nextInt(range * 2 + 1);

			for(int i = 0; i < range * 2; i++) {
				int blockID = supertile.worldObj.getBlockId(x, y, z);
				if((blockID == Block.grass.blockID || blockID == Block.dirt.blockID || blockID == Block.tilledField.blockID) && supertile.worldObj.isAirBlock(x, y + 1, z)) {
					int color = supertile.worldObj.rand.nextInt(16);
					if(ModBlocks.flower.canBlockStay(supertile.worldObj, x, y + 1, z))
						supertile.worldObj.setBlock(x, y + 1, z, ModBlocks.flower.blockID, color, 1 | 2);

					mana -= COST;
					PacketDispatcher.sendPacketToAllInDimension(supertile.getDescriptionPacket(), supertile.worldObj.provider.dimensionId);

					cooldown = 30;
					break;
				}

				y--;
			}
		}
	}

	@Override
	public int getColor() {
		return 0x961283;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.jadedAmaranthus;
	}
	
	@Override
	public int getMaxMana() {
		return COST;
	}

}
