/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 6, 2014, 9:57:19 PM (GMT)]
 */
package vazkii.botania.common.block.subtile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lexicon.LexiconData;
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

		if(getWorld().isRemote) {
			if(state != NONE && Math.random() > 0.6)
				Botania.proxy.wispFX(getPos().getX() + 0.55 + Math.random() * 0.2 - 0.1, getPos().getY() + 0.75 + Math.random() * 0.2 - 0.1, getPos().getZ() + 0.5, state == INCREASING ? 0.05F : 1F, 0.05F, state == INCREASING ? 1F : 0.05F, (float) Math.random() / 7, (float) -Math.random() / 50);
		} else {
			int mana = 0;
			for(Direction dir : MathHelper.HORIZONTALS) {
				BlockPos pos = getPos().offset(dir);
				if(getWorld().isBlockLoaded(pos)) {
					TileEntity tile = getWorld().getTileEntity(pos);
					if(tile instanceof IManaPool)
						mana += ((IManaPool) tile).getCurrentMana();
				}
			}

			int newState = mana > lastMana ? INCREASING : mana < lastMana ? DECREASING : NONE;
			if(newState != state)
				getWorld().addBlockEvent(getPos(), getBlockState().getBlock(), SET_STATE_EVENT, newState);

			if(ticksExisted % 60 == 0)
				lastMana = mana;
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int param) {
		if(id == SET_STATE_EVENT) {
			state = param;
			return true;
		} else {
			return super.receiveClientEvent(id, param);
		}
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.manastar;
	}

}
