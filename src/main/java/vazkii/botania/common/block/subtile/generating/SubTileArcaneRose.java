/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 18, 2014, 8:45:25 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.core.helper.ExperienceHelper;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;

public class SubTileArcaneRose extends SubTileGenerating {

	private static final int RANGE = 1;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(mana >= getMaxMana())
			return;

		List<EntityPlayer> players = supertile.getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		for(EntityPlayer player : players)
			if(ExperienceHelper.getPlayerXP(player) >= 1 && player.onGround) {
				ExperienceHelper.drainPlayerXP(player, 1);
				mana += 50;
				return;
			}

		List<EntityXPOrb> orbs = supertile.getWorld().getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		for(EntityXPOrb orb : orbs) {
			mana += orb.getXpValue() * 35;
			orb.setDead();
			return;
		}

	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFF8EF8;
	}

	@Override
	public int getMaxMana() {
		return 6000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.arcaneRose;
	}

}
