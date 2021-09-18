/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import com.google.common.collect.ImmutableMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;

import java.util.Map;

public class EntityVineBall extends ThrowableProjectile implements ItemSupplier {
	private static final EntityDataAccessor<Float> GRAVITY = SynchedEntityData.defineId(EntityVineBall.class, EntityDataSerializers.FLOAT);
	private static final Map<Direction, BooleanProperty> propMap = ImmutableMap.of(Direction.NORTH, VineBlock.NORTH, Direction.SOUTH, VineBlock.SOUTH,
			Direction.WEST, VineBlock.WEST, Direction.EAST, VineBlock.EAST);

	public EntityVineBall(EntityType<EntityVineBall> type, Level world) {
		super(type, world);
	}

	public EntityVineBall(LivingEntity thrower, boolean gravity) {
		super(ModEntities.VINE_BALL, thrower, thrower.level);
		entityData.set(GRAVITY, gravity ? 0.03F : 0F);
	}

	public EntityVineBall(double x, double y, double z, Level worldIn) {
		super(ModEntities.VINE_BALL, x, y, z, worldIn);
		entityData.set(GRAVITY, 0.03F);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(GRAVITY, 0F);
	}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return PacketSpawnEntity.make(this);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int j = 0; j < 16; j++) {
				level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ModItems.vineBall)), getX(), getY(), getZ(), Math.random() * 0.2 - 0.1, Math.random() * 0.25, Math.random() * 0.2 - 0.1);
			}
		}
	}

	@Override
	protected void onHit(@Nonnull HitResult rtr) {
		if (!level.isClientSide) {
			if (rtr.getType() == HitResult.Type.BLOCK) {
				Direction dir = ((BlockHitResult) rtr).getDirection();

				if (dir.getAxis() != Direction.Axis.Y) {
					BlockPos pos = ((BlockHitResult) rtr).getBlockPos().relative(dir);
					boolean first = true;
					while (pos.getY() > 0) {
						BlockState state = level.getBlockState(pos);
						if (state.isAir()) {
							BlockState stateSet = ModBlocks.solidVines.defaultBlockState().setValue(propMap.get(dir.getOpposite()), true);

							if (first && !stateSet.canSurvive(level, pos)) {
								break;
							}
							first = false;

							level.setBlockAndUpdate(pos, stateSet);
							level.levelEvent(2001, pos, Block.getId(stateSet));
							pos = pos.below();
						} else {
							break;
						}
					}
				}

			}

			this.level.broadcastEntityEvent(this, (byte) 3);
			discard();
		}
	}

	@Override
	protected float getGravity() {
		return entityData.get(GRAVITY);
	}

	@Nonnull
	@Override
	public ItemStack getItem() {
		return new ItemStack(ModItems.vineBall);
	}
}
