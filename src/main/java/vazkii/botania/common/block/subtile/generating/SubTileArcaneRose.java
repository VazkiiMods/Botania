/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.ExperienceHelper;

import java.util.List;

public class SubTileArcaneRose extends TileEntityGeneratingFlower {
	private static final int RANGE = 1;

	public SubTileArcaneRose() {
		super(ModSubtiles.ROSA_ARCANA);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (world.isRemote || getMana() >= getMaxMana()) {
			return;
		}

		List<PlayerEntity> players = getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		for (PlayerEntity player : players) {
			if (ExperienceHelper.getPlayerXP(player) >= 1 && player.func_233570_aj_()) {
				ExperienceHelper.drainPlayerXP(player, 1);
				addMana(50);
				sync();
				return;
			}
		}

		List<ExperienceOrbEntity> orbs = getWorld().getEntitiesWithinAABB(ExperienceOrbEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		for (ExperienceOrbEntity orb : orbs) {
			if (orb.isAlive()) {
				addMana(orb.getXpValue() * 35);
				orb.remove();
				sync();
				return;
			}
		}

	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFF8EF8;
	}

	@Override
	public int getMaxMana() {
		return 6000;
	}

}
