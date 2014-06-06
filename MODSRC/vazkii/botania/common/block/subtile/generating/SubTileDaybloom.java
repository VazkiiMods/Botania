/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 25, 2014, 3:09:35 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

public class SubTileDaybloom extends SubTileGenerating {

	@Override
	public int getColor() {
		return 0xFFFF00;
	}

	@Override
	public boolean canGeneratePassively() {
		boolean rain = supertile.getWorldObj().getWorldChunkManager().getBiomeGenAt(supertile.xCoord, supertile.zCoord).getIntRainfall() > 0 && (supertile.getWorldObj().isRaining() || supertile.getWorldObj().isThundering());
		return !supertile.getWorldObj().isRemote && supertile.getWorldObj().isDaytime() && !rain && supertile.getWorldObj().canBlockSeeTheSky(supertile.xCoord, supertile.yCoord + 1, supertile.zCoord);
	}
	
	@Override
	public int getDelayBetweenPassiveGeneration() {
		return 30 + (int) (getSurroundingFlowers() * 7.5);
	}
	
	public int getSurroundingFlowers() {
		int flowers = 0;
		for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
			TileEntity tile = supertile.getWorldObj().getTileEntity(supertile.xCoord + dir.offsetX, supertile.yCoord, supertile.zCoord + dir.offsetZ);
			if(tile != null && tile instanceof TileSpecialFlower) {
				TileSpecialFlower flower = (TileSpecialFlower) tile;
				if(flower.getSubTile() != null && flower.getSubTile().getClass() == getClass())
					flowers++;
			}
		}
		
		return flowers;
	}

	@Override
	public boolean shouldSyncPassiveGeneration() {
		return true;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.daybloom;
	}

}
