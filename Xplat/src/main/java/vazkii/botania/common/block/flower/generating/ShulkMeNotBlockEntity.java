/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.List;

public class ShulkMeNotBlockEntity extends GeneratingFlowerBlockEntity {
	private static final int RADIUS = 8;
	private static final int MANA_PER_SHULKER = 75000;

	public ShulkMeNotBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.SHULK_ME_NOT, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		Level world = getLevel();
		if (world.isClientSide) {
			return;
		}
		int generate = getMaxMana();
		if (getMaxMana() - getMana() < generate) {
			return;
		}

		BlockPos pos = getEffectivePos();
		Vec3 posD = new Vec3(pos.getX(), pos.getY(), pos.getZ());
		List<Shulker> shulkers = world.getEntitiesOfClass(Shulker.class, new AABB(pos).inflate(RADIUS), EntitySelector.ENTITY_STILL_ALIVE);
		for (Shulker shulker : shulkers) {
			if (getMaxMana() - getMana() < generate) {
				break;
			}

			if (shulker.isAlive() && shulker.distanceToSqr(posD) < RADIUS * RADIUS) {
				LivingEntity target = shulker.getTarget();
				if (target instanceof Enemy && target.isAlive()
						&& target.distanceToSqr(posD) < RADIUS * RADIUS && target.getEffect(MobEffects.LEVITATION) != null) {
					target.discard();
					shulker.discard();

					world.playSound(null, pos, BotaniaSounds.shulkMeNot, SoundSource.BLOCKS, 10F, 1F);
					world.playSound(null, target, BotaniaSounds.shulkMeNot, SoundSource.BLOCKS, 10F, 1F);
					world.playSound(null, shulker, BotaniaSounds.shulkMeNot, SoundSource.BLOCKS, 10F, 1F);
					if (world instanceof ServerLevel ws) {
						ws.sendParticles(ParticleTypes.EXPLOSION,
								target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
								100, target.getBbWidth(), target.getBbHeight(), target.getBbWidth(), 0.05);
						ws.sendParticles(ParticleTypes.EXPLOSION,
								shulker.getX(), shulker.getY() + shulker.getBbHeight() / 2, shulker.getZ(),
								100, shulker.getBbWidth(), shulker.getBbHeight(), shulker.getBbWidth(), 0.05);
						ws.sendParticles(ParticleTypes.PORTAL,
								pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
								40, 0, 0, 0, 0.6);
					}

					world.gameEvent(null, GameEvent.BLOCK_ACTIVATE, pos);
					addMana(generate);
					sync();
				}
			}
		}
	}

	@Override
	public int getColor() {
		return 0x815598;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), RADIUS);
	}

	@Override
	public int getMaxMana() {
		return MANA_PER_SHULKER;
	}

}
