/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 15, 2014, 7:25:47 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileMunchdew extends SubTileGenerating {

	@Override
	public void onUpdate() {
		super.onUpdate();

		int manaPerLeaf = 16;
		if(getMaxMana() - mana >= manaPerLeaf && !supertile.getWorldObj().isRemote && supertile.getWorldObj().getTotalWorldTime() % 4 == 0) {
			List<ChunkCoordinates> coords = new ArrayList();
			int range = 8;
			int rangeY = 16;

			int x = supertile.xCoord;
			int y = supertile.yCoord;
			int z = supertile.zCoord;

			for(int i = -range; i < range + 1; i++)
				for(int j = 0; j < rangeY; j++)
					for(int k = -range; k < range + 1; k++) {
						int xp = x + i;
						int yp = y + j;
						int zp = z + k;
						Block block = supertile.getWorldObj().getBlock(xp, yp, zp);
						if(block.getMaterial() == Material.leaves) {
							boolean exposed = false;
							for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
								if(supertile.getWorldObj().getBlock(xp + dir.offsetX, yp + dir.offsetY, zp + dir.offsetZ).isAir(supertile.getWorldObj(), xp + dir.offsetX, yp + dir.offsetY, zp + dir.offsetZ)) {
									exposed = true;
									break;
								}

							if(exposed)
								coords.add(new ChunkCoordinates(xp, yp, zp));
						}
					}

			if(coords.isEmpty())
				return;

			Collections.shuffle(coords);
			ChunkCoordinates breakCoords = coords.get(0);
			Block block = supertile.getWorldObj().getBlock(breakCoords.posX, breakCoords.posY, breakCoords.posZ);
			int meta = supertile.getWorldObj().getBlockMetadata(breakCoords.posX, breakCoords.posY, breakCoords.posZ);
			supertile.getWorldObj().setBlockToAir(breakCoords.posX, breakCoords.posY, breakCoords.posZ);
			if(ConfigHandler.blockBreakParticles)
				supertile.getWorldObj().playAuxSFX(2001, breakCoords.posX, breakCoords.posY, breakCoords.posZ, Block.getIdFromBlock(block) + (meta << 12));
			mana += manaPerLeaf;
		}
	}

	@Override
	public int getColor() {
		return 0x79C42F;
	}

	@Override
	public int getMaxMana() {
		return 200;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.munchdew;
	}
}
