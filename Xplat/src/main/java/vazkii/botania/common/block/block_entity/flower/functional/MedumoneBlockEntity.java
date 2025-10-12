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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.MathHelper;

import java.util.List;

public class MedumoneBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int RANGE = 6;

	public MedumoneBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.MEDUMONE, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || getMana() == 0 || isPowered()) {
			return;
		}
		var testInstance = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 100);
		List<LivingEntity> entities = getLevel().getEntitiesOfClass(LivingEntity.class,
				MathHelper.inflateBoxAround(getEffectivePos(), RANGE),
				livingEntity -> livingEntity.isAlive() && !(livingEntity instanceof Player)
						&& livingEntity.canBeAffected(testInstance));

		for (LivingEntity entity : entities) {
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 100));
			addMana(-1);
			if (getMana() == 0) {
				return;
			}
		}
	}

	@Override
	public boolean isOvergrowthAffected() {
		return false;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
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
