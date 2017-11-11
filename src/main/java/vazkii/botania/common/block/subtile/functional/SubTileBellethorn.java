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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;
import java.util.function.Predicate;

public class SubTileBellethorn extends SubTileFunctional {

	public static final int RANGE = 6;
	public static final int RANGE_MINI = 1;

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

		if(supertile.getWorld().isRemote || redstoneSignal > 0)
			return;

		if(ticksExisted % 200 == 0)
			sync();

		final int manaToUse = getManaCost();

		if(ticksExisted % 5 == 0) {
			int range = getRange();
			List<EntityLivingBase> entities = supertile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(supertile.getPos().add(-range, -range, -range), supertile.getPos().add(range + 1, range + 1, range + 1)), getSelector()::test);

			for(EntityLivingBase entity : entities) {
				if(entity.hurtTime == 0 && mana >= manaToUse) {
					int dmg = 4;
					if(entity instanceof EntityWitch)
						dmg = 20;

					entity.attackEntityFrom(DamageSource.MAGIC, dmg);
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

	public int getRange() {
		return RANGE;
	}

	public Predicate<Entity> getSelector() {
		return entity -> !(entity instanceof EntityPlayer);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), getRange());
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.bellethorne;
	}

	public static class Mini extends SubTileBellethorn {
		@Override public int getRange() { return RANGE_MINI; }
	}

}
