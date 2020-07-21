/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;

public class SubTileShulkMeNot extends TileEntityGeneratingFlower {
	private static final int RADIUS = 8;

	public SubTileShulkMeNot() {
		super(ModSubtiles.SHULK_ME_NOT);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		int generate = getMaxMana();

		World world = getWorld();
		BlockPos pos = getEffectivePos();
		Vector3d posD = new Vector3d(pos.getX(), pos.getY(), pos.getZ());
		if (!world.isRemote) {
			List<ShulkerEntity> shulkers = world.getEntitiesWithinAABB(ShulkerEntity.class, new AxisAlignedBB(pos).grow(RADIUS));
			for (ShulkerEntity shulker : shulkers) {
				if (getMaxMana() - getMana() < generate) {
					break;
				}

				if (shulker.isAlive() && shulker.getDistanceSq(posD) < RADIUS * RADIUS) {
					LivingEntity target = shulker.getAttackTarget();
					if (target instanceof IMob && target.isAlive()
							&& target.getDistanceSq(posD) < RADIUS * RADIUS && target.getActivePotionEffect(Effects.LEVITATION) != null) {
						target.remove();
						shulker.remove();

						for (int i = 0; i < 10; i++) // so it's really loud >_>
						{
							world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_SHULKER_DEATH, SoundCategory.BLOCKS, 10F, 0.1F);
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

	private void particles(World world, BlockPos pos, Entity entity) {
		if (world instanceof ServerWorld) {
			ServerWorld ws = (ServerWorld) world;
			ws.spawnParticle(ParticleTypes.EXPLOSION,
					entity.getPosX() + entity.getWidth() / 2,
					entity.getPosY() + entity.getHeight() / 2,
					entity.getPosZ() + entity.getWidth() / 2,
					100, entity.getWidth(), entity.getHeight(), entity.getWidth(), 0.05);
			ws.spawnParticle(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 40, 0, 0, 0, 0.6);
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
