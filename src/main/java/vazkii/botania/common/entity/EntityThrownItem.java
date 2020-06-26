/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public class EntityThrownItem extends ItemEntity {
	public EntityThrownItem(EntityType<EntityThrownItem> type, World world) {
		super(type, world);
	}

	public EntityThrownItem(World world, double x,
			double y, double z, ItemEntity item) {
		super(world, x, y, z, item.getItem());
		setPickupDelay(item.pickupDelay);
		setMotion(item.getMotion());
		setInvulnerable(true);
	}

	@Nonnull
	@Override
	public EntityType<?> getType() {
		return ModEntities.THROWN_ITEM;
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick() {
		super.tick();
		Vector3d vec3 = getPositionVec();
		Vector3d vec31 = getPositionVec().add(getMotion());

		RayTraceResult ray = world.rayTraceBlocks(new RayTraceContext(vec3, vec31,
				RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));

		if (!world.isRemote) {
			Entity entity = null;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().offset(getMotion().scale(2)).grow(2));
			double d0 = 0.0D;

			for (Entity entity1 : list) {
				if (entity1.canBeCollidedWith() && (!(entity1 instanceof PlayerEntity) || pickupDelay == 0)) {
					float f = 1.0F;
					AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(f);
					Optional<Vector3d> ray1 = axisalignedbb.rayTrace(vec3, vec31);

					if (ray1.isPresent()) {
						double d1 = vec3.distanceTo(ray1.get());

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null) {
				ray = new EntityRayTraceResult(entity);
			}
		}

		if (ray != null) {
			if (ray.getType() == RayTraceResult.Type.BLOCK
					&& world.getBlockState(((BlockRayTraceResult) ray).getPos()).getBlock() == Blocks.NETHER_PORTAL) {
				setPortal(((BlockRayTraceResult) ray).getPos());
			} else {
				if (ray.getType() == RayTraceResult.Type.ENTITY) {
					((EntityRayTraceResult) ray).getEntity().attackEntityFrom(DamageSource.MAGIC, 2.0F);
					if (!world.isRemote) {
						Entity item = getItem().getItem().createEntity(world, this, getItem());
						if (item == null) {
							item = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), getItem());
							world.addEntity(item);
						}
						item.setMotion(getMotion().scale(0.25));
					}
					remove();

				}
			}
		}

		Vector3 vec3m = new Vector3(getMotion());
		if (vec3m.mag() < 1.0F) {
			if (!world.isRemote) {
				Entity item = getItem().getItem().createEntity(world, this, getItem());
				if (item == null) {
					item = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), getItem());
					world.addEntity(item);
				}
				item.setMotion(getMotion());
			}
			remove();
		}
	}
}
