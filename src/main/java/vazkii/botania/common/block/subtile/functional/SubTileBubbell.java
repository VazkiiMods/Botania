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

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileFakeAir;
import vazkii.botania.common.lib.LibMisc;

public class SubTileBubbell extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":bubbell")
	public static TileEntityType<SubTileBubbell> TYPE;


	private static final int RANGE = 12;
	private static final int RANGE_MINI = 6;
	private static final int COST_PER_TICK = 4;
	private static final String TAG_RANGE = "range";

	int range = 2;

	public SubTileBubbell(TileEntityType<?> type) {
		super(type);
	}

	public SubTileBubbell() {
		this(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(getWorld().isRemote)
			return;

		if(ticksExisted % 200 == 0)
			sync();

		if(getMana() > COST_PER_TICK) {
			addMana(-COST_PER_TICK);

			if(ticksExisted % 10 == 0 && range < getRange())
				range++;

			for(BlockPos pos : BlockPos.getAllInBoxMutable(getPos().add(-range, -range, -range), getPos().add(range, range, range))) {
				if(getPos().distanceSq(pos) < range * range) {
					BlockState state = getWorld().getBlockState(pos);
					if(state.getMaterial() == Material.WATER) {
						getWorld().setBlockState(pos, ModBlocks.fakeAir.getDefaultState(), 2);
						TileFakeAir air = (TileFakeAir) getWorld().getTileEntity(pos);
						air.setFlower(this);
					}
				}
			}
		}
	}

	public static boolean isValidBubbell(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof SubTileBubbell) {
			return ((SubTileBubbell) tile).getMana() > COST_PER_TICK;
		}

		return false;
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_RANGE, range);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		range = cmp.getInt(TAG_RANGE);
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
		return new RadiusDescriptor.Circle(getPos(), range);
	}

	public static class Mini extends SubTileBubbell {
		@ObjectHolder(LibMisc.MOD_ID + ":bubbell_chibi")
		public static TileEntityType<SubTileBubbell.Mini> TYPE;

		public Mini() {
			super(TYPE);
		}

		@Override public int getRange() { return RANGE_MINI; }
	}

}
