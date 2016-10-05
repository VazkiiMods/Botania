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
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileManastar extends SubTileEntity {

	int manaLastTick = -1;

	@Override
	public void onUpdate() {
		super.onUpdate();

		int mana = 0;
		for(EnumFacing dir : EnumFacing.HORIZONTALS) {
			TileEntity tile = supertile.getWorld().getTileEntity(supertile.getPos().offset(dir));
			if(tile instanceof IManaPool)
				mana += ((IManaPool) tile).getCurrentMana();
		}

		if(manaLastTick != -1 && mana != manaLastTick && Math.random() > 0.6) {
			boolean more = mana > manaLastTick;
			Botania.proxy.wispFX(supertile.getPos().getX() + 0.55 + Math.random() * 0.2 - 0.1, supertile.getPos().getY() + 0.75 + Math.random() * 0.2 - 0.1, supertile.getPos().getZ() + 0.5, more ? 0.05F : 1F, 0.05F, more ? 1F : 0.05F, (float) Math.random() / 7, (float) -Math.random() / 50);
		}

		if(ticksExisted % 60 == 0)
			manaLastTick = mana;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.manastar;
	}

}
