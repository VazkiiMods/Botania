/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 27, 2014, 2:47:40 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileBellethorn extends SubTileFunctional {

	@Override
	public int getColor() {
		return 0xBA3421;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		final int range = 6;
		final int manaToUse = 24;

		if(supertile.getWorldObj().getTotalWorldTime() % 15 == 0) {
			List<EntityLivingBase> entities = supertile.getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + 1, supertile.zCoord + range));
			for(EntityLivingBase entity : entities) {
				if(entity instanceof EntityPlayer)
					continue;

				if(entity.hurtTime == 0 && mana >= manaToUse) {
					entity.attackEntityFrom(DamageSource.magic, 4);
					mana -= manaToUse;
				}
			}
		}
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.bellethorne;
	}

}
