/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.ModTags;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SubTileRafflowsia extends TileEntityGeneratingFlower {
	public static final String TAG_LAST_FLOWERS = "lastFlowers";
	public static final String TAG_LAST_FLOWER_TIMES = "lastFlowerTimes";
	public static final String TAG_STREAK_LENGTH = "streakLength";

	private List<Block> lastFlowers = new LinkedList<>();
	private int streakLength = -1;
	private int lastFlowerCount = 0;

	private static final int RANGE = 5;

	// Below table generated from the function:
	// f(x) = round(-401.45 + 7.03436 x + 16.0932 x^2 + 7.64878 * 1.25226^x, 100),
	// where x is the number of unique flowers in the streak.
	// Function created from a best-fit approximation on the sorted raw mana costs of production of each flower.
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
	private int processFlower(Block flower) {
		for (ListIterator<Block> it = lastFlowers.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			Block streakFlower = it.next();
			if (streakFlower == flower) {
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

		if (getMaxMana() - this.getMana() >= mana && !getLevel().isClientSide && ticksExisted % 40 == 0) {
			for (int i = 0; i < RANGE * 2 + 1; i++) {
				for (int j = 0; j < RANGE * 2 + 1; j++) {
					for (int k = 0; k < RANGE * 2 + 1; k++) {
						BlockPos pos = getEffectivePos().offset(i - RANGE, j - RANGE, k - RANGE);

						BlockState state = getLevel().getBlockState(pos);
						if (state.is(ModTags.Blocks.SPECIAL_FLOWERS) && state.getBlock() != ModSubtiles.rafflowsia) {
							streakLength = Math.min(streakLength + 1, processFlower(state.getBlock()));

							getLevel().destroyBlock(pos, false);
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
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);

		ListTag flowerList = new ListTag();
		for (Block flower : lastFlowers) {
			flowerList.add(StringTag.valueOf(Registry.BLOCK.getResourceKey(flower).toString()));
		}
		cmp.put(TAG_LAST_FLOWERS, flowerList);
		cmp.putInt(TAG_LAST_FLOWER_TIMES, lastFlowerCount);
		cmp.putInt(TAG_STREAK_LENGTH, streakLength);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);

		lastFlowers.clear();
		ListTag flowerList = cmp.getList(TAG_LAST_FLOWERS, 8);
		for (int i = 0; i < flowerList.size(); i++) {
			lastFlowers.add(Registry.BLOCK.get(ResourceLocation.tryParse(flowerList.getString(i))));
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
