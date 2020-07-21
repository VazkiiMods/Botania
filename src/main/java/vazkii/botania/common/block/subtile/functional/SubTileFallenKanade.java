/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;

public class SubTileFallenKanade extends TileEntityFunctionalFlower {
	private static final int RANGE = 2;
	private static final int COST = 120;

	public SubTileFallenKanade() {
		super(ModSubtiles.FALLEN_KANADE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getWorld().isClient && getWorld().getRegistryKey() != World.END) {
			boolean did = false;
			List<PlayerEntity> players = getWorld().getNonSpectatingEntities(PlayerEntity.class, new Box(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (PlayerEntity player : players) {
				if (player.getStatusEffect(StatusEffects.REGENERATION) == null && getMana() >= COST) {
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 59, 2, true, true));
					addMana(-COST);
					did = true;
				}
			}
			if (did) {
				sync();
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFFFF00;
	}

	@Override
	public int getMaxMana() {
		return 900;
	}

}
