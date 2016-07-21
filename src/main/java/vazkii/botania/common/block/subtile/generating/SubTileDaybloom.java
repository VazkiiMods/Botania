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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.signature.PassiveFlower;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;

@PassiveFlower
public class SubTileDaybloom extends SubTilePassiveGenerating {

	private static final String TAG_PRIME_POSITION_X = "primePositionX";
	private static final String TAG_PRIME_POSITION_Y = "primePositionY";
	private static final String TAG_PRIME_POSITION_Z = "primePositionZ";
	private static final String TAG_SAVED_POSITION = "savedPosition";

	BlockPos primePosition;
	boolean savedPosition;

	@Override
	public int getColor() {
		return 0xFFFF00;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(isPrime() && (!savedPosition || !supertile.getPos().equals(primePosition)))
			supertile.getWorld().setBlockToAir(supertile.getPos());
	}

	public void setPrimusPosition() {
		primePosition = supertile.getPos();

		savedPosition = true;
	}

	@Override
	public List<ItemStack> getDrops(List<ItemStack> list) {
		if(isPrime())
			list.clear();

		return super.getDrops(list);
	}

	@Override
	public boolean canGeneratePassively() {
		boolean rain = supertile.getWorld().getBiome(supertile.getPos()).getRainfall() > 0 && (supertile.getWorld().isRaining() || supertile.getWorld().isThundering());
		return supertile.getWorld().isDaytime() && !rain && supertile.getWorld().canBlockSeeSky(supertile.getPos().up());
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return isPrime() ? 10 : 12;
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		if(isPrime()) {
			cmp.setInteger(TAG_PRIME_POSITION_X, primePosition.getX());
			cmp.setInteger(TAG_PRIME_POSITION_Y, primePosition.getY());
			cmp.setInteger(TAG_PRIME_POSITION_Z, primePosition.getZ());
			cmp.setBoolean(TAG_SAVED_POSITION, savedPosition);
		}
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		if(isPrime()) {
			primePosition = new BlockPos(
				cmp.getInteger(TAG_PRIME_POSITION_X),
				cmp.getInteger(TAG_PRIME_POSITION_Y),
				cmp.getInteger(TAG_PRIME_POSITION_Z)
			);
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
