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
import net.minecraft.tags.EntityTypeTags;
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

public class HyacidusBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int RANGE = 6;
	private static final int COST = 20;

	public HyacidusBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.HYACIDUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || getMana() < COST || isPowered()) {
			return;
		}

		List<LivingEntity> entities = getLevel().getEntitiesOfClass(LivingEntity.class,
				MathHelper.inflateBoxAround(getEffectivePos(), RANGE),
				livingEntity -> livingEntity.isAlive() && !(livingEntity instanceof Player)
						&& livingEntity.getEffect(MobEffects.POISON) == null
						&& !livingEntity.getType().is(EntityTypeTags.UNDEAD));
		for (LivingEntity entity : entities) {
			entity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, overgrowth ? 1 : 0));
			addMana(-COST);
			if (getMana() < COST) {
				break;
			}
		}
	}

	@Override
	public int getColor() {
		return 0x8B438F;
	}

	@Override
	public int getMaxMana() {
		return 180;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

}
