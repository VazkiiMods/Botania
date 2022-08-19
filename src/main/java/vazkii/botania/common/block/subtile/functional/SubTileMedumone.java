/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 27, 2015, 7:33:17 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileMedumone extends SubTileFunctional {

	private static final int RANGE = 6;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(mana > 0) {
			List<EntityLivingBase> entities = supertile.getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - RANGE, supertile.yCoord, supertile.zCoord - RANGE, supertile.xCoord + RANGE + 1, supertile.yCoord + 1, supertile.zCoord + RANGE + 1));

			for(EntityLivingBase entity : entities)
				if(!(entity instanceof EntityPlayer)) {
					entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 2, 100));
					mana--;
					if(mana == 0)
						return;
				}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
	}

	@Override
	public int getColor() {
		return 0x3D2204;
	}

	@Override
	public int getMaxMana() {
		return 4000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.medumone;
	}

}
