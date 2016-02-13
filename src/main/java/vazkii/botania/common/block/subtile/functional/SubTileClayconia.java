/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 17, 2014, 12:05:37 AM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileClayconia extends SubTileFunctional {

	private static final int COST = 80;
	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;
	
	private static final int RANGE_MINI = 2;
	private static final int RANGE_Y_MINI = 1;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!supertile.getWorldObj().isRemote && ticksExisted % 5 == 0) {
			if(mana >= COST) {
				ChunkCoordinates coords = getCoordsToPut();
				if(coords != null) {
					supertile.getWorldObj().setBlockToAir(coords.posX, coords.posY, coords.posZ);
					if(ConfigHandler.blockBreakParticles)
						supertile.getWorldObj().playAuxSFX(2001, coords.posX, coords.posY, coords.posZ, Block.getIdFromBlock(Block.getBlockFromName("sand")));
					EntityItem item = new EntityItem(supertile.getWorldObj(), coords.posX + 0.5, coords.posY + 0.5, coords.posZ + 0.5, new ItemStack(Items.clay_ball));
					supertile.getWorldObj().spawnEntityInWorld(item);
					mana -= COST;
				}
			}
		}
	}

	public ChunkCoordinates getCoordsToPut() {
		List<ChunkCoordinates> possibleCoords = new ArrayList();

		int range = getRange();
		int rangeY = getRangeY();
		
		for(int i = -range; i < range + 1; i++)
			for(int j = -rangeY; j < rangeY + 1; j++)
				for(int k = -range; k < range + 1; k++) {
					int x = supertile.xCoord + i;
					int y = supertile.yCoord + j;
					int z = supertile.zCoord + k;
					Block block = supertile.getWorldObj().getBlock(x, y, z);
					if(block == Block.getBlockFromName("sand"))
						possibleCoords.add(new ChunkCoordinates(x, y, z));
				}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(supertile.getWorldObj().rand.nextInt(possibleCoords.size()));
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), getRange());
	}

	public int getRange() {
		return RANGE;
	}

	public int getRangeY() {
		return RANGE_Y;
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

	public static class Mini extends SubTileClayconia {
		@Override public int getRange() { return RANGE_MINI; }
		@Override public int getRangeY() { return RANGE_Y_MINI; }
	}
}
