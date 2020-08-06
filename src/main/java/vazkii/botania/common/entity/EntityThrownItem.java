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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
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
		super(world, x, y, z, item.getItem());
		setPickupDelay(((AccessorItemEntity) item).getPickupDelay());
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

		// [VanillaCopy] derivative from ThrowableEntity
		int pickupDelay = ((AccessorItemEntity) this).getPickupDelay();
		Predicate<Entity> filter = e -> !e.isSpectator() && e.isAlive() && e.canBeCollidedWith() && (!(e instanceof PlayerEntity) || pickupDelay == 0);
		RayTraceResult ray = ProjectileHelper.func_234618_a_(this, filter, RayTraceContext.BlockMode.OUTLINE);
		if (ray.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos pos = ((BlockRayTraceResult) ray).getPos();
			BlockState state = this.world.getBlockState(pos);
			if (state.isIn(Blocks.NETHER_PORTAL)) {
				this.setPortal(pos);
			} else if (state.isIn(Blocks.END_GATEWAY)) {
				TileEntity tileentity = this.world.getTileEntity(pos);
				if (tileentity instanceof EndGatewayTileEntity) {
					((EndGatewayTileEntity) tileentity).teleportEntity(this);
				}
			}
		}

		// Bonk any entities hit
		if (!world.isRemote && ray.getType() == RayTraceResult.Type.ENTITY) {
			Entity bonk = ((EntityRayTraceResult) ray).getEntity();
			bonk.attackEntityFrom(DamageSource.MAGIC, 2.0F);
			Entity item = getItem().getItem().createEntity(world, this, getItem());
			if (item == null) {
				item = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), getItem());
				world.addEntity(item);
			}
			item.setMotion(getMotion().scale(0.25));
			remove();
			return;
		}

		if (!world.isRemote && getMotion().length() < 1.0F) {
			Entity item = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), getItem());
			world.addEntity(item);
			item.setMotion(getMotion());
			remove();
		}
	}
}
