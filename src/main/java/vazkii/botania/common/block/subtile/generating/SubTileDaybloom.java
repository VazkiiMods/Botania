/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 25, 2014, 3:09:35 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

public class SubTileDaybloom extends SubTilePassiveGenerating {

	private static final String TAG_PRIME_POSITION_X = "primePositionX";
	private static final String TAG_PRIME_POSITION_Y = "primePositionY";
	private static final String TAG_PRIME_POSITION_Z = "primePositionZ";
	private static final String TAG_SAVED_POSITION = "savedPosition";

	int primePositionX, primePositionY, primePositionZ;
	boolean savedPosition;

	@Override
	public int getColor() {
		return 0xFFFF00;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(isPrime() && (!savedPosition || primePositionX != supertile.xCoord || primePositionY != supertile.yCoord || primePositionZ != supertile.zCoord))
			supertile.getWorldObj().setBlockToAir(supertile.xCoord, supertile.yCoord, supertile.zCoord);
	}

	public void setPrimusPosition() {
		primePositionX = supertile.xCoord;
		primePositionY = supertile.yCoord;
		primePositionZ = supertile.zCoord;

		savedPosition = true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(ArrayList<ItemStack> list) {
		if(isPrime())
			list.clear();

		return super.getDrops(list);
	}

	@Override
	public boolean canGeneratePassively() {
		boolean rain = supertile.getWorldObj().getWorldChunkManager().getBiomeGenAt(supertile.xCoord, supertile.zCoord).getIntRainfall() > 0 && (supertile.getWorldObj().isRaining() || supertile.getWorldObj().isThundering());
		return supertile.getWorldObj().isDaytime() && !rain && supertile.getWorldObj().canBlockSeeTheSky(supertile.xCoord, supertile.yCoord + 1, supertile.zCoord);
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return isPrime() ? 22 : 25 + (int) (getSurroundingFlowers() * 7.5);
	}

	public int getSurroundingFlowers() {
		int flowers = 0;
		for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
			TileEntity tile = supertile.getWorldObj().getTileEntity(supertile.xCoord + dir.offsetX, supertile.yCoord, supertile.zCoord + dir.offsetZ);
			if(tile != null && tile instanceof ISubTileContainer) {
				ISubTileContainer flower = (ISubTileContainer) tile;
				if(flower.getSubTile() != null && flower.getSubTile().getClass() == getClass()) {
					flowers++;

					Color color = new Color(getColor());
					float r = color.getRed() / 255F;
					float g = color.getGreen() / 255F;
					float b = color.getBlue() / 255F;

					float m = 0.045F;
					if(ticksExisted % 10 == 0)
						Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + 0.5, supertile.yCoord + 0.05, supertile.zCoord + 0.5, r, g, b, 0.1F, dir.offsetX * m, dir.offsetY * m, dir.offsetZ * m);
				}
			}
		}

		return flowers;
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		if(isPrime()) {
			cmp.setInteger(TAG_PRIME_POSITION_X, primePositionX);
			cmp.setInteger(TAG_PRIME_POSITION_Y, primePositionY);
			cmp.setInteger(TAG_PRIME_POSITION_Z, primePositionZ);
			cmp.setBoolean(TAG_SAVED_POSITION, savedPosition);
		}
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		if(isPrime()) {
			primePositionX = cmp.getInteger(TAG_PRIME_POSITION_X);
			primePositionY = cmp.getInteger(TAG_PRIME_POSITION_Y);
			primePositionZ = cmp.getInteger(TAG_PRIME_POSITION_Z);
			savedPosition = cmp.getBoolean(TAG_SAVED_POSITION);
		}
	}

	@Override
	public boolean shouldSyncPassiveGeneration() {
		return true;
	}

	@Override
	public boolean isPassiveFlower() {
		return !isPrime();
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.daybloom;
	}

	public boolean isPrime() {
		return false;
	}

	public static class Prime extends SubTileDaybloom {

		@Override
		public boolean isPrime() {
			return true;
		}

		@Override
		public LexiconEntry getEntry() {
			return LexiconData.primusLoci;
		}

	}

}
