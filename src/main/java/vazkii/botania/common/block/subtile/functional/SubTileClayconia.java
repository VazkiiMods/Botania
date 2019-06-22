/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 17, 2014, 12:05:37 AM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public class SubTileClayconia extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":clayconia")
	public static TileEntityType<SubTileClayconia> TYPE;

	private static final int COST = 80;
	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;

	private static final int RANGE_MINI = 2;
	private static final int RANGE_Y_MINI = 1;

	public SubTileClayconia(TileEntityType<?> type) {
		super(type);
	}

	public SubTileClayconia() {
		this(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(!getWorld().isRemote && ticksExisted % 5 == 0) {
			if(mana >= COST) {
				BlockPos coords = getCoordsToPut();
				if(coords != null) {
					getWorld().removeBlock(coords, false);
					if(ConfigHandler.COMMON.blockBreakParticles.get())
						getWorld().playEvent(2001, coords, Block.getStateId(Blocks.SAND.getDefaultState()));
					ItemEntity item = new ItemEntity(getWorld(), coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5, new ItemStack(Items.CLAY_BALL));
					getWorld().addEntity(item);
					mana -= COST;
				}
			}
		}
	}

	public BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		int range = getRange();
		int rangeY = getRangeY();

		for(int i = -range; i < range + 1; i++)
			for(int j = -rangeY; j < rangeY + 1; j++)
				for(int k = -range; k < range + 1; k++) {
					BlockPos pos = getPos().add(i, j, k);
					Block block = getWorld().getBlockState(pos).getBlock();
					if(block == Blocks.SAND)
						possibleCoords.add(pos);
				}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(getWorld().rand.nextInt(possibleCoords.size()));
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
		return 0x7B8792;
	}

	@Override
	public int getMaxMana() {
		return 640;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.clayconia;
	}

	public static class Mini extends SubTileClayconia {
		@ObjectHolder(LibMisc.MOD_ID + ":bellethorn_chibi")
		public static TileEntityType<SubTileBellethorn.Mini> TYPE;

		public Mini() {
			super(TYPE);
		}

		@Override public int getRange() { return RANGE_MINI; }
		@Override public int getRangeY() { return RANGE_Y_MINI; }
	}
}
