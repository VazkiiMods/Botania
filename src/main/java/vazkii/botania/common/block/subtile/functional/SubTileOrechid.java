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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileOrechid extends SubTileFunctional {

	private static final int COST = 17500;
	private static final int COST_GOG = 700;
	private static final int DELAY = 100;
	private static final int DELAY_GOG = 2;
	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(redstoneSignal > 0 || !canOperate())
			return;

		int cost = getCost();
		if(!supertile.getWorldObj().isRemote && mana >= cost && ticksExisted % getDelay() == 0) {
			ChunkCoordinates coords = getCoordsToPut();
			if(coords != null) {
				ItemStack stack = getOreToPut();
				if(stack != null) {
					Block block = Block.getBlockFromItem(stack.getItem());
					int meta = stack.getItemDamage();
					supertile.getWorldObj().setBlock(coords.posX, coords.posY, coords.posZ, block, meta, 1 | 2);
					if(ConfigHandler.blockBreakParticles)
						supertile.getWorldObj().playAuxSFX(2001, coords.posX, coords.posY, coords.posZ, Block.getIdFromBlock(block) + (meta << 12));
					supertile.getWorldObj().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "botania:orechid", 2F, 1F);

					mana -= cost;
					sync();
				}
			}
		}
	}

	public ItemStack getOreToPut() {
		Collection<WeightedRandom.Item> values = new ArrayList();
		Map<String, Integer> map = getOreMap();
		for(String s : map.keySet())
			values.add(new StringRandomItem(map.get(s), s));

		String ore = ((StringRandomItem) WeightedRandom.getRandomItem(supertile.getWorldObj().rand, values)).s;

		List<ItemStack> ores = OreDictionary.getOres(ore);

		for(ItemStack stack : ores) {
			Item item = stack.getItem();
			String clname = item.getClass().getName();

			// This poem is dedicated to Greg
			//
			// Greg.
			// I get what you do when
			// others say it's a grind.
			// But take your TE ores
			// and stick them in your behind.
			if(clname.startsWith("gregtech") || clname.startsWith("gregapi"))
				continue;

			return stack;
		}

		return getOreToPut();
	}

	public ChunkCoordinates getCoordsToPut() {
		List<ChunkCoordinates> possibleCoords = new ArrayList();

		Block source = getSourceBlock();
		for(int i = -RANGE; i < RANGE + 1; i++)
			for(int j = -RANGE_Y; j < RANGE_Y; j++)
				for(int k = -RANGE; k < RANGE + 1; k++) {
					int x = supertile.xCoord + i;
					int y = supertile.yCoord + j;
					int z = supertile.zCoord + k;
					Block block = supertile.getWorldObj().getBlock(x, y, z);
					if(block != null && block.isReplaceableOreGen(supertile.getWorldObj(), x, y, z, source))
						possibleCoords.add(new ChunkCoordinates(x, y, z));
				}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(supertile.getWorldObj().rand.nextInt(possibleCoords.size()));
	}

	public boolean canOperate() {
		return true;
	}

	public Map<String, Integer> getOreMap() {
		return BotaniaAPI.oreWeights;
	}

	public Block getSourceBlock() {
		return Blocks.stone;
	}

	public int getCost() {
		return Botania.gardenOfGlassLoaded ? COST_GOG : COST;
	}

	public int getDelay() {
		return Botania.gardenOfGlassLoaded ? DELAY_GOG : DELAY;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
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

	private static class StringRandomItem extends WeightedRandom.Item {

		public String s;

		public StringRandomItem(int par1, String s) {
			super(par1);
			this.s = s;
		}

	}
}
