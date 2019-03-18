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
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SubTileOrechid extends SubTileFunctional {

	private static final int COST = 17500;
	private static final int COST_GOG = 700;
	private static final int DELAY = 100;
	private static final int DELAY_GOG = 2;
	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;
	private static final int TRIES = 20;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(supertile.getWorld().isRemote || redstoneSignal > 0 || !canOperate())
			return;

		int cost = getCost();
		if(mana >= cost && ticksExisted % getDelay() == 0) {
			BlockPos coords = getCoordsToPut();
			if(coords != null) {
				IBlockState state = getOreToPut();
				if(state != null) {
					supertile.getWorld().setBlockState(coords, state);
					if(ConfigHandler.COMMON.blockBreakParticles.get())
						supertile.getWorld().playEvent(2001, coords, Block.getStateId(state));
					supertile.getWorld().playSound(null, supertile.getPos(), ModSounds.orechid, SoundCategory.BLOCKS, 2F, 1F);

					mana -= cost;
					sync();
				}
			}
		}
	}

	private IBlockState getOreToPut() {
		for (int i = 0; i < TRIES; i++) {
			List<WeightedRandom.Item> values = new ArrayList<>();
			Map<ResourceLocation, Integer> map = getOreMap();
			for(ResourceLocation s : map.keySet())
				values.add(new TagRandomItem(map.get(s), s));

			ResourceLocation ore = ((TagRandomItem) WeightedRandom.getRandomItem(supertile.getWorld().rand, values)).s;
			Tag<Block> tag = BlockTags.getCollection().get(ore);
			if(tag != null && !tag.getAllElements().isEmpty()) {
				return tag.getRandomElement(getWorld().getRandom()).getDefaultState();
			}
		}

		return null;
	}

	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		for(BlockPos pos : BlockPos.getAllInBox(getPos().add(-RANGE, -RANGE_Y, -RANGE), getPos().add(RANGE, RANGE_Y, RANGE))) {
			IBlockState state = supertile.getWorld().getBlockState(pos);
			if(state.getBlock().isReplaceableOreGen(state, supertile.getWorld(), pos, getReplaceMatcher()))
				possibleCoords.add(pos);
		}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(supertile.getWorld().rand.nextInt(possibleCoords.size()));
	}

	public boolean canOperate() {
		return true;
	}

	public Map<ResourceLocation, Integer> getOreMap() {
		return BotaniaAPI.oreWeights;
	}

	public Predicate<IBlockState> getReplaceMatcher() {
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
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
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
