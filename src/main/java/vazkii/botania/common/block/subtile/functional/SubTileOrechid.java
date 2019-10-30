/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 11, 2014, 5:40:55 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SubTileOrechid extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":orechid")
	public static TileEntityType<SubTileOrechid> TYPE;

	private static final int COST = 17500;
	private static final int COST_GOG = 700;
	private static final int DELAY = 100;
	private static final int DELAY_GOG = 2;
	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;
	private static final int TRIES = 20;

	public SubTileOrechid(TileEntityType<?> type) {
		super(type);
	}

	public SubTileOrechid() {
		this(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(getWorld().isRemote || redstoneSignal > 0 || !canOperate())
			return;

		int cost = getCost();
		if(mana >= cost && ticksExisted % getDelay() == 0) {
			BlockPos coords = getCoordsToPut();
			if(coords != null) {
				BlockState state = getOreToPut();
				if(state != null) {
					getWorld().setBlockState(coords, state);
					if(ConfigHandler.COMMON.blockBreakParticles.get())
						getWorld().playEvent(2001, coords, Block.getStateId(state));
					getWorld().playSound(null, getPos(), ModSounds.orechid, SoundCategory.BLOCKS, 2F, 1F);

					mana -= cost;
					sync();
				}
			}
		}
	}

	private BlockState getOreToPut() {
		for (int i = 0; i < TRIES; i++) {
			List<WeightedRandom.Item> values = new ArrayList<>();
			Map<ResourceLocation, Integer> map = getOreMap();
			for(ResourceLocation s : map.keySet())
				values.add(new TagRandomItem(map.get(s), s));

			ResourceLocation ore = ((TagRandomItem) WeightedRandom.getRandomItem(getWorld().rand, values)).s;
			Tag<Block> tag = BlockTags.getCollection().get(ore);
			if(tag != null && !tag.getAllElements().isEmpty()) {
				return tag.getRandomElement(getWorld().getRandom()).getDefaultState();
			}
		}

		return null;
	}

	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		for(BlockPos pos : BlockPos.getAllInBoxMutable(getPos().add(-RANGE, -RANGE_Y, -RANGE),
				getPos().add(RANGE, RANGE_Y, RANGE))) {
			BlockState state = getWorld().getBlockState(pos);
			if(state.getBlock().isReplaceableOreGen(state, getWorld(), pos, getReplaceMatcher()))
				possibleCoords.add(pos.toImmutable());
		}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(getWorld().rand.nextInt(possibleCoords.size()));
	}

	public boolean canOperate() {
		return true;
	}

	public Map<ResourceLocation, Integer> getOreMap() {
		return BotaniaAPI.oreWeights;
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
        return new RadiusDescriptor.Square(getPos(), RANGE);
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

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.orechid;
	}

	private static class TagRandomItem extends WeightedRandom.Item {

		public final ResourceLocation s;

		public TagRandomItem(int weight, ResourceLocation s) {
			super(weight);
			this.s = s;
		}

	}
}
