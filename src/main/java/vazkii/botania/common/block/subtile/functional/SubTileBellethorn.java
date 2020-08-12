/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;
import java.util.function.Predicate;

public class SubTileBellethorn extends TileEntityFunctionalFlower {
	public static final int RANGE = 6;
	public static final int RANGE_MINI = 1;

	public SubTileBellethorn(BlockEntityType<?> type) {
		super(type);
	}

	public SubTileBellethorn() {
		this(ModSubtiles.BELLETHORNE);
	}

	@Override
	public int getColor() {
		return 0xBA3421;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient || redstoneSignal > 0) {
			return;
		}

		if (ticksExisted % 200 == 0) {
			sync();
		}

		final int manaToUse = getManaCost();

		if (ticksExisted % 5 == 0) {
			int range = getRange();
			List<LivingEntity> entities = getWorld().getEntitiesByClass(LivingEntity.class, new Box(getEffectivePos().add(-range, -range, -range), getEffectivePos().add(range + 1, range + 1, range + 1)), getSelector());

			for (LivingEntity entity : entities) {
				if (entity.hurtTime == 0 && getMana() >= manaToUse) {
					int dmg = 4;
					if (entity instanceof WitchEntity) {
						dmg = 20;
					}

					entity.damage(DamageSource.MAGIC, dmg);
					addMana(-manaToUse);
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
		return entity -> !(entity instanceof PlayerEntity);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), getRange());
	}

	public static class Mini extends SubTileBellethorn {
		public Mini() {
			super(ModSubtiles.BELLETHORNE_CHIBI);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}
	}

}
