/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 10, 2015, 10:28:42 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileFakeAir;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileBubbell extends SubTileFunctional {

	private static final int RANGE = 12;
	private static final int RANGE_MINI = 6;
	private static final int COST_PER_TICK = 4;
	private static final String TAG_RANGE = "range";

	int range = 2;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(supertile.getWorld().isRemote)
			return;

		if(ticksExisted % 200 == 0)
			sync();

		if(mana > COST_PER_TICK) {
			mana -= COST_PER_TICK;

			if(ticksExisted % 10 == 0 && range < getRange())
				range++;

			for(BlockPos pos : BlockPos.getAllInBoxMutable(getPos().add(-range, -range, -range), getPos().add(range, range, range))) {
				if(getPos().distanceSq(pos) < range * range) {
					IBlockState state = supertile.getWorld().getBlockState(pos);
					if(state.getMaterial() == Material.WATER) {
						supertile.getWorld().setBlockState(pos, ModBlocks.fakeAir.getDefaultState(), 2);
						TileFakeAir air = (TileFakeAir) supertile.getWorld().getTileEntity(pos);
						air.setFlower(supertile);
					}
				}
			}
		}
	}

	public static boolean isValidBubbell(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof ISubTileContainer) {
			ISubTileContainer container = (ISubTileContainer) tile;
			if(container.getSubTile() != null && container.getSubTile() instanceof SubTileBubbell) {
				SubTileBubbell bubbell = (SubTileBubbell) container.getSubTile();
				return bubbell.mana > COST_PER_TICK;
			}
		}

		return false;
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setInteger(TAG_RANGE, range);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		range = cmp.getInteger(TAG_RANGE);
	}

	@Override
	public int getMaxMana() {
		return 2000;
	}

	@Override
	public int getColor() {
		return 0x0DCF89;
	}

	public int getRange() {
		return RANGE;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(toBlockPos(), range);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.bubbell;
	}

	public static class Mini extends SubTileBubbell {
		@Override public int getRange() { return RANGE_MINI; }
	}

}
