/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 7, 2014, 10:59:44 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.List;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileEntropinnyum extends SubTileGenerating {

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(mana == 0) {
			int range = 12;
			List<EntityTNTPrimed> tnts = supertile.getWorldObj().getEntitiesWithinAABB(EntityTNTPrimed.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord - range, supertile.zCoord - range, supertile.xCoord + range + 1, supertile.yCoord + range + 1, supertile.zCoord + range + 1));
			for(EntityTNTPrimed tnt : tnts) {
				if(tnt.fuse == 1 && !tnt.isDead) {
					if(!supertile.getWorldObj().isRemote) {
						tnt.setDead();
						mana += getMaxMana();
						supertile.getWorldObj().playSoundEffect(tnt.posX, tnt.posY, tnt.posZ, "random.explode", 0.2F, (1F + (supertile.getWorldObj().rand.nextFloat() - supertile.getWorldObj().rand.nextFloat()) * 0.2F) * 0.7F);
						sync();
					}

					for(int i = 0; i < 50; i++)
						Botania.proxy.sparkleFX(tnt.worldObj, tnt.posX + Math.random() * 4 - 2, tnt.posY + Math.random() * 4 - 2, tnt.posZ + Math.random() * 4 - 2, 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, (float) (Math.random() * 0.65F + 1.25F), 12);

					supertile.getWorldObj().spawnParticle("hugeexplosion", tnt.posX, tnt.posY, tnt.posZ, 1D, 0D, 0D);
					return;
				}
			}
		}
	}

	@Override
	public int getColor() {
		return 0xcb0000;
	}

	@Override
	public int getMaxMana() {
		return 6500;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.entropinnyum;
	}

}
