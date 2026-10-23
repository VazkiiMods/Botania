/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.flower.generating.DandelifeonBlockEntity;
import vazkii.botania.common.block.block_entity.flower.generating.DandelifeonBlockEntity.Cell;

public class CellularBlockEntity extends BlockEntity {
	private static final String TAG_GENERATION = "generation";
	private static final String TAG_TICKED = "ticked";
	private static final String TAG_FLOWER_COORDS = "flowerPos";
	private static final String TAG_VALID_COORDS = "validPos";

	private int generation;
	private int nextGeneration;
	private boolean ticked;
	@Nullable
	private BlockPos flowerCoords;
	@Nullable
	private BlockPos validCoords;

	public CellularBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.CELLULAR_BLOCK, pos, state);
	}

	public void setGeneration(int gen) {
		generation = gen;
	}

	public void setNextGeneration(DandelifeonBlockEntity flower, int gen) {
		nextGeneration = gen;
		getLevel().scheduleTick(getBlockPos(), BotaniaBlocks.CELLULAR_BLOCK, 1);
		if (!ticked) {
			claim(flower);
			ticked = true;
		} else if (!getBlockPos().equals(validCoords) || !flower.getEffectivePos().equals(flowerCoords)) {
			level.removeBlock(worldPosition, false);
		}
	}

	public void claim(DandelifeonBlockEntity flower) {
		if (!ticked) {
			flowerCoords = flower.getEffectivePos();
			validCoords = getBlockPos();
		}
	}

	public void update(Level level) {
		if (nextGeneration == Cell.DEAD) {
			level.removeBlock(getBlockPos(), false);
		}
		generation = nextGeneration;
	}

	public boolean hasActiveParent(DandelifeonBlockEntity dandie) {
		return flowerCoords != null
				&& dandie.getLevel().getBlockEntity(flowerCoords) instanceof DandelifeonBlockEntity parent
				&& parent.shouldUpdateThisTick() && parent.isPowered();
	}

	public int getGeneration() {
		return generation;
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_GENERATION, generation);
		cmp.putBoolean(TAG_TICKED, ticked);
		if (ticked) {
			if (flowerCoords != null) {
				cmp.put(TAG_FLOWER_COORDS, NbtUtils.writeBlockPos(flowerCoords));
			}
			if (validCoords != null) {
				cmp.put(TAG_VALID_COORDS, NbtUtils.writeBlockPos(validCoords));
			}
		}
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		generation = cmp.getInt(TAG_GENERATION);
		ticked = cmp.getBoolean(TAG_TICKED);
		if (ticked) {
			flowerCoords = NbtUtils.readBlockPos(cmp, TAG_FLOWER_COORDS).orElse(null);
			validCoords = NbtUtils.readBlockPos(cmp, TAG_VALID_COORDS).orElse(null);
		}
	}

	@Override
	public boolean onlyOpCanSetNbt() {
		// targeting Create here, sorry about any instances of https://xkcd.com/1172/
		return true;
	}
}
