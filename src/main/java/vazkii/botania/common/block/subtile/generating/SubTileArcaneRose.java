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

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.core.helper.ExperienceHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class SubTileArcaneRose extends TileEntityGeneratingFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":rosa_arcana")
	public static TileEntityType<SubTileArcaneRose> TYPE;

	private static final int RANGE = 1;

	public SubTileArcaneRose() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(mana >= getMaxMana())
			return;

		List<PlayerEntity> players = getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		for(PlayerEntity player : players)
			if(ExperienceHelper.getPlayerXP(player) >= 1 && player.onGround) {
				ExperienceHelper.drainPlayerXP(player, 1);
				mana += 50;
				return;
			}

		List<ExperienceOrbEntity> orbs = getWorld().getEntitiesWithinAABB(ExperienceOrbEntity.class, new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		for(ExperienceOrbEntity orb : orbs) {
			mana += orb.getXpValue() * 35;
			orb.remove();
			return;
		}

	}

	@Override
	public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(getPos(), RANGE);
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
