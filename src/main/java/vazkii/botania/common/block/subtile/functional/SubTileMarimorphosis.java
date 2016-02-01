/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 29, 2015, 8:17:55 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileMarimorphosis extends SubTileFunctional {

	private static final int COST = 12;
	private static final int RANGE = 8;
	private static final int RANGE_Y = 5;

	private static final int RANGE_MINI = 2;
	private static final int RANGE_Y_MINI = 1;

	private static final Type[] TYPES = new Type[] {
		Type.FOREST,
		Type.PLAINS,
		Type.MOUNTAIN,
		Type.MUSHROOM,
		Type.SWAMP,
		Type.SANDY,
		Type.COLD,
		Type.MESA
	};

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(redstoneSignal > 0)
			return;

		if(!supertile.getWorldObj().isRemote && mana >= COST && ticksExisted % 2 == 0) {
			ChunkCoordinates coords = getCoordsToPut();
			if(coords != null) {
				ItemStack stack = getStoneToPut(coords);
				if(stack != null) {
					Block block = Block.getBlockFromItem(stack.getItem());
					int meta = stack.getItemDamage();
					supertile.getWorldObj().setBlock(coords.posX, coords.posY, coords.posZ, block, meta, 1 | 2);
					if(ConfigHandler.blockBreakParticles)
						supertile.getWorldObj().playAuxSFX(2001, coords.posX, coords.posY, coords.posZ, Block.getIdFromBlock(block) + (meta << 12));

					mana -= COST;
					sync();
				}
			}
		}
	}

	public ItemStack getStoneToPut(ChunkCoordinates coords) {
		List<Type> types = Arrays.asList(BiomeDictionary.getTypesForBiome(supertile.getWorldObj().getBiomeGenForCoords(coords.posX, coords.posZ)));

		List<Integer> values = new ArrayList();
		for(int i = 0; i < 8; i++) {
			int times = 1;
			if(types.contains(TYPES[i]))
				times = 12;

			for(int j = 0; j < times; j++)
				values.add(i);
		}

		return new ItemStack(ModFluffBlocks.biomeStoneA, 1, values.get(supertile.getWorldObj().rand.nextInt(values.size())));
	}

	public ChunkCoordinates getCoordsToPut() {
		List<ChunkCoordinates> possibleCoords = new ArrayList();

		int range = getRange();
		int rangeY = getRangeY();

		for(int i = -range; i < range + 1; i++)
			for(int j = -rangeY; j < rangeY; j++)
				for(int k = -range; k < range + 1; k++) {
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
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), getRange());
	}

	public int getRange() {
		return RANGE;
	}

	public int getRangeY() {
		return RANGE_Y;
	}

	@Override
	public int getColor() {
		return 0x769897;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.marimorphosis;
	}

	public static class Mini extends SubTileMarimorphosis {
		@Override public int getRange() { return RANGE_MINI; }
		@Override public int getRangeY() { return RANGE_Y_MINI; }
	}

}
