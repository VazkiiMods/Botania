/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nonnull;

import java.util.function.Predicate;

public class EntityThrownItem extends ItemEntity {
	public EntityThrownItem(EntityType<EntityThrownItem> type, World world) {
		super(type, world);
	}

	public EntityThrownItem(World world, double x,
			double y, double z, ItemEntity item) {
		super(world, x, y, z, item.getStack());
		setPickupDelay(((AccessorItemEntity) item).getPickupDelay());
		setVelocity(item.getVelocity());
		setInvulnerable(true);
	}

	@Nonnull
	@Override
	public EntityType<?> getType() {
		return ModEntities.THROWN_ITEM;
	}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick() {
		super.tick();

		// [VanillaCopy] derivative from ThrowableEntity
		int pickupDelay = ((AccessorItemEntity) this).getPickupDelay();
		Predicate<Entity> filter = e -> !e.isSpectator() && e.isAlive() && e.collides() && (!(e instanceof PlayerEntity) || pickupDelay == 0);
		HitResult ray = ProjectileUtil.getCollision(this, filter, RayTraceContext.ShapeType.OUTLINE);
		if (ray.getType() == HitResult.Type.BLOCK) {
			BlockPos pos = ((BlockHitResult) ray).getBlockPos();
			BlockState state = this.world.getBlockState(pos);
			if (state.isOf(Blocks.NETHER_PORTAL)) {
				this.setInNetherPortal(pos);
			} else if (state.isOf(Blocks.END_GATEWAY)) {
				BlockEntity tileentity = this.world.getBlockEntity(pos);
				if (tileentity instanceof EndGatewayBlockEntity) {
					((EndGatewayBlockEntity) tileentity).tryTeleportingEntity(this);
				}
			}
		}

		// Bonk any entities hit
		if (!world.isClient && ray.getType() == HitResult.Type.ENTITY) {
			Entity bonk = ((EntityHitResult) ray).getEntity();
			bonk.damage(DamageSource.MAGIC, 2.0F);
			Entity item = getStack().getItem().createEntity(world, this, getStack());
			if (item == null) {
				item = new ItemEntity(world, getX(), getY(), getZ(), getStack());
				world.spawnEntity(item);
			}
			item.setVelocity(getVelocity().multiply(0.25));
			remove();
			return;
		}

		if (!world.isClient && getVelocity().length() < 1.0F) {
			Entity item = new ItemEntity(world, getX(), getY(), getZ(), getStack());
			world.spawnEntity(item);
			item.setVelocity(getVelocity());
			remove();
		}
	}
}
