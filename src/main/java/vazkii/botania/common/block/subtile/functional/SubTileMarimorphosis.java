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

import gnu.trove.list.array.TIntArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		if(supertile.getWorld().isRemote || redstoneSignal > 0)
			return;

		if(mana >= COST && ticksExisted % 2 == 0) {
			BlockPos coords = getCoordsToPut();
			if(coords != null) {
				IBlockState state = getStoneToPut(coords);
				if(state != null) {
					supertile.getWorld().setBlockState(coords, state);
					if(ConfigHandler.blockBreakParticles)
						supertile.getWorld().playEvent(2001, coords, Block.getStateId(state));

					mana -= COST;
					sync();
				}
			}
		}
	}

	public IBlockState getStoneToPut(BlockPos coords) {
		Set<Type> types = BiomeDictionary.getTypes(supertile.getWorld().getBiome(coords));

		TIntArrayList values = new TIntArrayList();
		for(int i = 0; i < TYPES.length; i++) {
			int times = 1;
			if(types.contains(TYPES[i]))
				times = 12;

			for(int j = 0; j < times; j++)
				values.add(i);
		}

		int meta = values.get(supertile.getWorld().rand.nextInt(values.size()));
		BiomeStoneVariant variant = BiomeStoneVariant.values()[meta];
		return ModFluffBlocks.biomeStoneA.getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, variant);
	}

	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		int range = getRange();
		int rangeY = getRangeY();

		BlockStateMatcher matcher = BlockStateMatcher.forBlock(Blocks.STONE);
		for(BlockPos pos : BlockPos.getAllInBox(getPos().add(-range, -rangeY, -range), getPos().add(range, rangeY, range))) {
			IBlockState state = supertile.getWorld().getBlockState(pos);
			if(state.getBlock().isReplaceableOreGen(state, supertile.getWorld(), pos, matcher))
				possibleCoords.add(pos);
		}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(supertile.getWorld().rand.nextInt(possibleCoords.size()));
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), getRange());
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
