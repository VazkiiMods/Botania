/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;

public class SubTileShulkMeNot extends TileEntityGeneratingFlower {
	private static final int RADIUS = 8;

	public SubTileShulkMeNot(BlockPos pos, BlockState state) {
		super(ModSubtiles.SHULK_ME_NOT, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		int generate = getMaxMana();

		Level world = getLevel();
		BlockPos pos = getEffectivePos();
		Vec3 posD = new Vec3(pos.getX(), pos.getY(), pos.getZ());
		if (!world.isClientSide) {
			List<Shulker> shulkers = world.getEntitiesOfClass(Shulker.class, new AABB(pos).inflate(RADIUS));
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

						for (int i = 0; i < 10; i++) // so it's really loud >_>
						{
							world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.SHULKER_DEATH, SoundSource.BLOCKS, 10F, 0.1F);
						}
						particles(world, pos, target);
						particles(world, pos, shulker);

						addMana(generate);
						sync();
					}
				}
			}
		}
	}

	private void particles(Level world, BlockPos pos, Entity entity) {
		if (world instanceof ServerLevel) {
			ServerLevel ws = (ServerLevel) world;
			ws.sendParticles(ParticleTypes.EXPLOSION,
					entity.getX() + entity.getBbWidth() / 2,
					entity.getY() + entity.getBbHeight() / 2,
					entity.getZ() + entity.getBbWidth() / 2,
					100, entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth(), 0.05);
			ws.sendParticles(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 40, 0, 0, 0, 0.6);
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
		return 75000;
	}

}
