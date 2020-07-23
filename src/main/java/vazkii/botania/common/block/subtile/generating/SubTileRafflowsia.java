/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.util.Constants;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SubTileRafflowsia extends TileEntityGeneratingFlower {
	public static final String TAG_LAST_FLOWERS = "lastFlowers";
	public static final String TAG_LAST_FLOWER_TIMES = "lastFlowerTimes";
	public static final String TAG_STREAK_LENGTH = "streakLength";

	@Nullable
	private List<String> lastFlowers = new LinkedList<>();
	private int streakLength = -1;
	private int lastFlowerCount = 0;

	private static final int RANGE = 5;
	private static final int[] STREAK_OUTPUTS = { 300, 1100, 1900, 2700, 3500, 4400, 5300, 6300, 7300, 8300, 9400, 10500, 11600, 12800, 14000, 15200, 16500, 17900, 19200, 20700, 22200, 23800, 25400, 27100, 29000, 30900, 33000, 35200, 37700, 40300, 43200, 46500, 50200, 54300, 59100, 64600, 71100, 78600, 87600, 98400 };

	public SubTileRafflowsia() {
		super(ModSubtiles.RAFFLOWSIA);
	}

	private int getMaxStreak() {
		return STREAK_OUTPUTS.length - 1;
	}

	private int getValueForStreak(int index) {
		// special-case repeated first flowers
		if (index != 0) {
			lastFlowerCount = 0;
		}
		return STREAK_OUTPUTS[index] / ++lastFlowerCount;
	}

	/**
	 * Processes a flower, placing it in the appropriate place in the history.
	 * 
	 * @return the last time the flower showed up in history.
	 */
	private int processFlower(String flower) {
		for (ListIterator<String> it = lastFlowers.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			String streakFlower = it.next();
			if (streakFlower.equals(flower)) {
				it.remove();
				lastFlowers.add(0, streakFlower);
				return index;
			}
		}
		lastFlowers.add(0, flower);
		if (lastFlowers.size() >= getMaxStreak()) {
			lastFlowers.remove(lastFlowers.size() - 1);
		}
		return getMaxStreak();
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		int mana = 2100;

		if (getMaxMana() - this.getMana() >= mana && !getWorld().isRemote && ticksExisted % 40 == 0) {
			for (int i = 0; i < RANGE * 2 + 1; i++) {
				for (int j = 0; j < RANGE * 2 + 1; j++) {
					for (int k = 0; k < RANGE * 2 + 1; k++) {
						BlockPos pos = getEffectivePos().add(i - RANGE, j - RANGE, k - RANGE);

						BlockState state = getWorld().getBlockState(pos);
						if (state.isIn(ModTags.Blocks.SPECIAL_FLOWERS) && state.getBlock() != ModSubtiles.rafflowsia) {
							streakLength = Math.min(streakLength + 1, processFlower(Registry.BLOCK.getKey(state.getBlock()).toString()));

							getWorld().destroyBlock(pos, false);
							addMana(getValueForStreak(streakLength));
							sync();
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);

		ListNBT flowerList = new ListNBT();
		for (String flower : lastFlowers) {
			flowerList.add(StringNBT.valueOf(flower));
		}
		cmp.put(TAG_LAST_FLOWERS, flowerList);
		cmp.putInt(TAG_LAST_FLOWER_TIMES, lastFlowerCount);
		cmp.putInt(TAG_STREAK_LENGTH, streakLength);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);

		lastFlowers.clear();
		ListNBT flowerList = cmp.getList(TAG_LAST_FLOWERS, Constants.NBT.TAG_STRING);
		for (int i = 0; i < flowerList.size(); i++) {
			lastFlowers.add(flowerList.getString(i));
		}
		lastFlowerCount = cmp.getInt(TAG_LAST_FLOWER_TIMES);
		streakLength = cmp.getInt(TAG_STREAK_LENGTH);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0x502C76;
	}

	@Override
	public int getMaxMana() {
		return 100000;
	}

}
