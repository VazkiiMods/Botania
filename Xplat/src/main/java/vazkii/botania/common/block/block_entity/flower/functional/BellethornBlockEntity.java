/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.MathHelper;

import java.util.List;
import java.util.function.Predicate;

public class BellethornBlockEntity extends FunctionalFlowerBlockEntity {
	public static final int RANGE = 6;
	public static final int RANGE_MINI = 1;

	protected BellethornBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public BellethornBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaBlockEntities.BELLETHORNE, pos, state);
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

		if (getLevel().isClientSide || isPowered() || !shouldUpdateThisTick()) {
			return;
		}

		final int manaToUse = getManaCost();

		if (getMana() >= manaToUse) {
			int range = getRange();
			List<LivingEntity> entities = getLevel().getEntitiesOfClass(LivingEntity.class,
					MathHelper.inflateBoxAround(getEffectivePos(), range), getSelector().and(e -> e.hurtTime == 0));

			for (LivingEntity entity : entities) {
				int dmg = 4;
				if (entity instanceof Witch) {
					dmg = 20;
				}

				entity.hurt(getLevel().damageSources().magic(), dmg);
				addMana(-manaToUse);
				if (getMana() < manaToUse) {
					break;
				}
			}
		}
	}

	@Override
	protected int getUpdateInterval() {
		return 5;
	}

	public int getManaCost() {
		return 24;
	}

	public int getRange() {
		return RANGE;
	}

	public Predicate<LivingEntity> getSelector() {
		return entity -> entity.isAlive() && !(entity instanceof Player);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
	}

	public static class Mini extends BellethornBlockEntity {
		public Mini(BlockPos pos, BlockState state) {
			super(BotaniaBlockEntities.BELLETHORNE_CHIBI, pos, state);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}
	}

}
