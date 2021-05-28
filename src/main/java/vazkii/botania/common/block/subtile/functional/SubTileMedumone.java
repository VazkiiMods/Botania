/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;

public class SubTileMedumone extends TileEntityFunctionalFlower {
	private static final int RANGE = 6;

	public SubTileMedumone() {
		super(ModSubtiles.MEDUMONE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getWorld().isClient && getMana() > 0 && redstoneSignal == 0) {
			List<LivingEntity> entities = getWorld().getNonSpectatingEntities(LivingEntity.class, new Box(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));

			for (LivingEntity entity : entities) {
				if (!(entity instanceof PlayerEntity)) {
					entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 100));
					addMana(-1);
					if (getMana() == 0) {
						return;
					}
				}
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0x3D2204;
	}

	@Override
	public int getMaxMana() {
		return 4000;
	}

}
