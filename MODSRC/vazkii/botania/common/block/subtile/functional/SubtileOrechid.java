/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 11, 2014, 5:40:55 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

public class SubtileOrechid extends SubTileFunctional {

	private static final int COST = 17500;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!supertile.getWorldObj().isRemote && mana >= COST && supertile.getWorldObj().getTotalWorldTime() % 100 == 0) {
			ChunkCoordinates coords = getCoordsToPut();
			if(coords != null) {
				ItemStack stack = getOreToPut();
				if(stack != null) {
					supertile.getWorldObj().setBlock(coords.posX, coords.posY, coords.posZ, Block.getBlockFromItem(stack.getItem()), stack.getItemDamage(), 1 | 2);

					mana -= COST;
					sync();
				}
			}
		}
	}

	public ItemStack getOreToPut() {
		Collection<WeightedRandom.Item> values = new ArrayList();
		for(String s : BotaniaAPI.oreWeights.keySet())
			values.add(new StringRandomItem(BotaniaAPI.oreWeights.get(s), s));

		String ore = ((StringRandomItem) WeightedRandom.getRandomItem(supertile.getWorldObj().rand, values)).s;

		List<ItemStack> ores = OreDictionary.getOres(ore);
		if(ores.isEmpty())
			return getOreToPut();

		return ores.get(0);
	}

	public ChunkCoordinates getCoordsToPut() {
		List<ChunkCoordinates> possibleCoords = new ArrayList();
		int range = 5;
		int rangeY = 3;

		for(int i = -range; i < range; i++)
			for(int j = -rangeY; j < rangeY; j++)
				for(int k = -range; k < range; k++) {
					int x = supertile.xCoord + i;
					int y = supertile.yCoord + j;
					int z = supertile.zCoord + k;
					Block block = supertile.getWorldObj().getBlock(x, y, z);
					if(block != null && block.isReplaceableOreGen(supertile.getWorldObj(), x, y, z, Blocks.stone))
						possibleCoords.add(new ChunkCoordinates(x, y, z));
				}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(supertile.getWorldObj().rand.nextInt(possibleCoords.size()));
	}

	@Override
	public int getColor() {
		return 0x818181;
	}

	@Override
	public int getMaxMana() {
		return COST;
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
