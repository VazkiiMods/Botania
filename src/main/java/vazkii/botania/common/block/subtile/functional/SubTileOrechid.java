/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubTileOrechid extends TileEntityFunctionalFlower {
	private static final int COST = 17500;
	private static final int COST_GOG = 700;
	private static final int DELAY = 100;
	private static final int DELAY_GOG = 2;
	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;

	public SubTileOrechid(BlockEntityType<?> type) {
		super(type);
	}

	public SubTileOrechid() {
		this(ModSubtiles.ORECHID);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient || redstoneSignal > 0 || !canOperate()) {
			return;
		}

		int cost = getCost();
		if (getMana() >= cost && ticksExisted % getDelay() == 0) {
			BlockPos coords = getCoordsToPut();
			if (coords != null) {
				BlockState state = getOreToPut();
				if (state != null) {
					getWorld().setBlockState(coords, state);
					if (ConfigHandler.COMMON.blockBreakParticles.getValue()) {
						getWorld().syncWorldEvent(2001, coords, Block.getRawIdFromState(state));
					}
					getWorld().playSound(null, coords, ModSounds.orechid, SoundCategory.BLOCKS, 2F, 1F);

					addMana(-cost);
					sync();
				}
			}
		}
	}

	@Nullable
	private BlockState getOreToPut() {
		Map<Identifier, Integer> map = getOreMap();
		List<TagRandomItem> values = map.entrySet().stream()
				.flatMap(e -> {
					Tag<Block> tag = BlockTags.getContainer().get(e.getKey());
					if (tag != null && !tag.values().isEmpty()) {
						return Stream.of(new TagRandomItem(e.getValue(), tag));
					} else {
						return Stream.empty();
					}
				})
				.collect(Collectors.toList());

		if (WeightedPicker.getWeightSum(values) == 0) {
			return null;
		}

		Tag<Block> ore = WeightedPicker.getRandom(getWorld().random, values).tag;
		return ore.getRandom(getWorld().getRandom()).getDefaultState();
	}

	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		for (BlockPos pos : BlockPos.iterate(getEffectivePos().add(-RANGE, -RANGE_Y, -RANGE),
				getEffectivePos().add(RANGE, RANGE_Y, RANGE))) {
			BlockState state = getWorld().getBlockState(pos);
			if (getReplaceMatcher().test(state)) {
				possibleCoords.add(pos.toImmutable());
			}
		}

		if (possibleCoords.isEmpty()) {
			return null;
		}
		return possibleCoords.get(getWorld().random.nextInt(possibleCoords.size()));
	}

	public boolean canOperate() {
		return true;
	}

	public Map<Identifier, Integer> getOreMap() {
		return BotaniaAPI.instance().getOreWeights();
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

	private static class TagRandomItem extends WeightedPicker.Entry {

		public final Tag<Block> tag;

		public TagRandomItem(int weight, Tag<Block> tag) {
			super(weight);
			this.tag = tag;
		}

	}
}
