/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 14, 2014, 8:54:11 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;

public class SubTileFallenKanade extends SubTileFunctional {

	private static final int RANGE = 2;
	private static final int COST = 120;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!supertile.getWorld().isRemote && supertile.getWorld().provider.getDimension() != 1) {
			boolean did = false;
			List<EntityPlayer> players = supertile.getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for(EntityPlayer player : players) {
				if(player.getActivePotionEffect(MobEffects.REGENERATION) == null && mana >= COST) {
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 59, 2, true, true));
					mana -= COST;
					did = true;
				}
			}
			if(did)
				sync();
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFFFF00;
	}

	@Override
	public int getMaxMana() {
		return 900;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.fallenKanade;
	}

}
