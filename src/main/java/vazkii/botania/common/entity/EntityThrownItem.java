/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.common.network.PacketSpawnEntity;
import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nonnull;

import java.util.function.Predicate;

public class EntityThrownItem extends ItemEntity {
	public EntityThrownItem(EntityType<EntityThrownItem> type, Level world) {
		super(type, world);
	}

	public EntityThrownItem(Level world, double x,
			double y, double z, ItemEntity item) {
		super(world, x, y, z, item.getItem());
		setPickUpDelay(((AccessorItemEntity) item).getPickupDelay());
		setDeltaMovement(item.getDeltaMovement());
		setInvulnerable(true);
	}

	@Nonnull
	@Override
	public EntityType<?> getType() {
		return ModEntities.THROWN_ITEM;
	}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return PacketSpawnEntity.make(this);
	}

	@Override
	public void tick() {
		super.tick();

		// [VanillaCopy] derivative from ThrowableEntity
		int pickupDelay = ((AccessorItemEntity) this).getPickupDelay();
		Predicate<Entity> filter = e -> !e.isSpectator() && e.isAlive() && e.isPickable() && (!(e instanceof Player) || pickupDelay == 0);
		HitResult ray = ProjectileUtil.getHitResult(this, filter);
		if (ray.getType() == HitResult.Type.BLOCK) {
			BlockPos pos = ((BlockHitResult) ray).getBlockPos();
			BlockState state = this.level.getBlockState(pos);
			if (state.is(Blocks.NETHER_PORTAL)) {
				this.handleInsidePortal(pos);
			} else if (state.is(Blocks.END_GATEWAY)) {
				BlockEntity tileentity = this.level.getBlockEntity(pos);
				if (tileentity instanceof TheEndGatewayBlockEntity) {
					((TheEndGatewayBlockEntity) tileentity).teleportEntity(this);
				}
			}
		}

		// Bonk any entities hit
		if (!level.isClientSide && ray.getType() == HitResult.Type.ENTITY) {
			Entity bonk = ((EntityHitResult) ray).getEntity();
			bonk.hurt(DamageSource.MAGIC, 2.0F);
			Entity item = new ItemEntity(level, getX(), getY(), getZ(), getItem());
			level.addFreshEntity(item);
			item.setDeltaMovement(getDeltaMovement().scale(0.25));
			discard();
			return;
		}

		if (!level.isClientSide && getDeltaMovement().length() < 1.0F) {
			Entity item = new ItemEntity(level, getX(), getY(), getZ(), getItem());
			level.addFreshEntity(item);
			item.setDeltaMovement(getDeltaMovement());
			discard();
		}
	}
}
