/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.lib.LibMisc;

public class SubTileManastar extends TileEntitySpecialFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":manastar")
	public static TileEntityType<SubTileManastar> TYPE;

	private static final int SET_STATE_EVENT = 0;
	private static final int NONE = 0, DECREASING = 1, INCREASING = 2;

	private int lastMana = 0;
	private int state = NONE;

	public SubTileManastar() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote) {
			if (state != NONE && Math.random() > 0.6) {
				float r = state == INCREASING ? 0.05F : 1F;
				float b = state == INCREASING ? 1F : 0.05F;
				WispParticleData data = WispParticleData.wisp((float) Math.random() / 7, r, 0.05F, b, 1);
				world.addParticle(data, getEffectivePos().getX() + 0.55 + Math.random() * 0.2 - 0.1, getEffectivePos().getY() + 0.75 + Math.random() * 0.2 - 0.1, getEffectivePos().getZ() + 0.5, 0, (float) Math.random() / 50, 0);
			}
		} else {
			int mana = 0;
			for (Direction dir : Direction.Plane.HORIZONTAL) {
				BlockPos pos = getEffectivePos().offset(dir);
				if (getWorld().isBlockLoaded(pos)) {
					TileEntity tile = getWorld().getTileEntity(pos);
					if (tile instanceof IManaPool) {
						mana += ((IManaPool) tile).getCurrentMana();
					}
				}
			}

			int newState = mana > lastMana ? INCREASING : mana < lastMana ? DECREASING : NONE;
			if (newState != state) {
				getWorld().addBlockEvent(getPos(), getBlockState().getBlock(), SET_STATE_EVENT, newState);
			}

			if (ticksExisted % 60 == 0) {
				lastMana = mana;
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int param) {
		if (id == SET_STATE_EVENT) {
			state = param;
			return true;
		} else {
			return super.receiveClientEvent(id, param);
		}
	}

}
