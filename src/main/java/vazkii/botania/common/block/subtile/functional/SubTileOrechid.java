/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.WeighedRandom;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SubTileOrechid extends TileEntityFunctionalFlower {
	private static final int COST = 17500;
	private static final int COST_GOG = 700;
	private static final int DELAY = 100;
	private static final int DELAY_GOG = 2;
	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;

	protected SubTileOrechid(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SubTileOrechid(BlockPos pos, BlockState state) {
		this(ModSubtiles.ORECHID, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || redstoneSignal > 0 || !canOperate()) {
			return;
		}

		int cost = getCost();
		if (getMana() >= cost && ticksExisted % getDelay() == 0) {
			BlockPos coords = getCoordsToPut();
			if (coords != null) {
				BlockState state = getOreToPut();
				if (state != null) {
					getLevel().setBlockAndUpdate(coords, state);
					if (ConfigHandler.COMMON.blockBreakParticles.getValue()) {
						getLevel().levelEvent(2001, coords, Block.getId(state));
					}
					getLevel().playSound(null, coords, ModSounds.orechid, SoundSource.BLOCKS, 2F, 1F);

					addMana(-cost);
					sync();
				}
			}
		}
	}

	@Nullable
	private BlockState getOreToPut() {
		List<OrechidOutput> values = getOreList();

		if (values.isEmpty()) {
			return null;
		}

		StateIngredient ore = WeighedRandom.getRandomItem(getLevel().random, values).getOutput();
		return ore.pick(getLevel().getRandom());
	}

	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		for (BlockPos pos : BlockPos.betweenClosed(getEffectivePos().offset(-RANGE, -RANGE_Y, -RANGE),
				getEffectivePos().offset(RANGE, RANGE_Y, RANGE))) {
			BlockState state = getLevel().getBlockState(pos);
			if (getReplaceMatcher().test(state)) {
				possibleCoords.add(pos.immutable());
			}
		}

		if (possibleCoords.isEmpty()) {
			return null;
		}
		return possibleCoords.get(getLevel().random.nextInt(possibleCoords.size()));
	}

	public boolean canOperate() {
		return true;
	}

	public List<OrechidOutput> getOreList() {
		return BotaniaAPI.instance().getOrechidWeights();
	}

	public Predicate<BlockState> getReplaceMatcher() {
		return state -> state.getBlock() == Blocks.STONE;
	}

	public int getCost() {
		return Botania.gardenOfGlassLoaded ? COST_GOG : COST;
	}

	public int getDelay() {
		return Botania.gardenOfGlassLoaded ? DELAY_GOG : DELAY;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0x818181;
	}

	@Override
	public int getMaxMana() {
		return getCost();
	}

}
