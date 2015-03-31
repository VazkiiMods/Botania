/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 27, 2014, 2:47:40 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileBellethorn extends SubTileFunctional {

	public static final int RANGE = 6;
	
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

		if(redstoneSignal > 0)
			return;

		final int manaToUse = getManaCost();

		if(ticksExisted % 5 == 0) {
			List<EntityLivingBase> entities = supertile.getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - RANGE, supertile.yCoord, supertile.zCoord - RANGE, supertile.xCoord + RANGE, supertile.yCoord + 1, supertile.zCoord + RANGE));
			IEntitySelector selector = getSelector();

			for(EntityLivingBase entity : entities) {
				if(!selector.isEntityApplicable(entity))
					continue;

				if(entity.hurtTime == 0 && mana >= manaToUse) {
					entity.attackEntityFrom(DamageSource.magic, 4);
					mana -= manaToUse;
					break;
				}
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	public int getManaCost() {
		return 24;
	}

	public IEntitySelector getSelector() {
		return new IEntitySelector() {

			@Override
			public boolean isEntityApplicable(Entity entity) {
				return !(entity instanceof EntityPlayer);
			}

		};
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
	}
	
	@Override
	public LexiconEntry getEntry() {
		return LexiconData.bellethorne;
	}

}
