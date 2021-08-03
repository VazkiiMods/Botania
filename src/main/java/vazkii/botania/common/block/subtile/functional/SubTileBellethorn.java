/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;

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

		if (getLevel().isClientSide || redstoneSignal > 0) {
			return;
		}

		if (ticksExisted % 200 == 0) {
			sync();
		}

		final int manaToUse = getManaCost();

		if (ticksExisted % 5 == 0) {
			int range = getRange();
			List<LivingEntity> entities = getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(getEffectivePos().offset(-range, -range, -range), getEffectivePos().offset(range + 1, range + 1, range + 1)), getSelector());

			for (LivingEntity entity : entities) {
				if (getMana() < manaToUse) {
					break;
				}
				if (entity.hurtTime == 0) {
					int dmg = 4;
					if (entity instanceof Witch) {
						dmg = 20;
					}

					entity.hurt(DamageSource.MAGIC, dmg);
					addMana(-manaToUse);
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
		return entity -> !(entity instanceof Player);
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
