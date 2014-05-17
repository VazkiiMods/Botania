/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 17, 2014, 12:05:37 AM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileClayconia extends SubTileFunctional {

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if(!supertile.getWorldObj().isRemote && supertile.getWorldObj().getTotalWorldTime() % 5 == 0) {
			int manaCost = 80;
			if(mana >= manaCost) {
				ChunkCoordinates coords = getCoordsToPut();
				if(coords != null) {
					supertile.getWorldObj().setBlockToAir(coords.posX, coords.posY, coords.posZ);
					supertile.getWorldObj().playAuxSFX(2001, coords.posX, coords.posY, coords.posZ, Block.getIdFromBlock(Blocks.sand));
					EntityItem item = new EntityItem(supertile.getWorldObj(), coords.posX + 0.5, coords.posY + 0.5, coords.posZ + 0.5, new ItemStack(Items.clay_ball));
					supertile.getWorldObj().spawnEntityInWorld(item);
					mana -= manaCost;
				}
			}
		}
	}
	
	public ChunkCoordinates getCoordsToPut() {
		List<ChunkCoordinates> possibleCoords = new ArrayList();
		int range = 5;
		int rangeY = 3;

		for(int i = -range; i < range; i++)
			for(int j = -rangeY; j < rangeY; j++)
				for(int k = -range; k < range; k++) {
					int x = supertile.xCoord + i;
					int y = supertile.yCoord + j;
					int z = supertile.zCoord + k;
					Block block = supertile.getWorldObj().getBlock(x, y, z);
					if(block == Blocks.sand)
						possibleCoords.add(new ChunkCoordinates(x, y, z));
				}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(supertile.getWorldObj().rand.nextInt(possibleCoords.size()));
	}
	
	@Override
	public int getColor() {
		return 0x7B8792;
	}
	
	@Override
	public int getMaxMana() {
		return 640;
	}
	
	@Override
	public LexiconEntry getEntry() {
		return LexiconData.clayconia;
	}
	
}
