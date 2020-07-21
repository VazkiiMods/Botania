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
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.Map;

@EnvironmentInterface(value = EnvType.CLIENT, itf = IRendersAsItem.class)
public class EntityVineBall extends ThrownEntity implements FlyingItemEntity {
	private static final TrackedData<Float> GRAVITY = DataTracker.registerData(EntityVineBall.class, TrackedDataHandlerRegistry.FLOAT);
	private static final Map<Direction, BooleanProperty> propMap = ImmutableMap.of(Direction.NORTH, VineBlock.NORTH, Direction.SOUTH, VineBlock.SOUTH,
			Direction.WEST, VineBlock.WEST, Direction.EAST, VineBlock.EAST);

	public EntityVineBall(EntityType<EntityVineBall> type, World world) {
		super(type, world);
	}

	public EntityVineBall(LivingEntity thrower, boolean gravity) {
		super(ModEntities.VINE_BALL, thrower, thrower.world);
		dataTracker.set(GRAVITY, gravity ? 0.03F : 0F);
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(GRAVITY, 0F);
	}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void handleStatus(byte id) {
		if (id == 3) {
			for (int j = 0; j < 16; j++) {
				world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(ModItems.vineBall)), getX(), getY(), getZ(), Math.random() * 0.2 - 0.1, Math.random() * 0.25, Math.random() * 0.2 - 0.1);
			}
		}
	}

	@Override
	protected void onCollision(@Nonnull HitResult rtr) {
		if (!world.isClient) {
			if (rtr.getType() == HitResult.Type.BLOCK) {
				Direction dir = ((BlockHitResult) rtr).getSide();

				if (dir.getAxis() != Direction.Axis.Y) {
					BlockPos pos = ((BlockHitResult) rtr).getBlockPos().offset(dir);
					boolean first = true;
					while (pos.getY() > 0) {
						BlockState state = world.getBlockState(pos);
						Block block = state.getBlock();
						if (block.isAir(state, world, pos)) {
							BlockState stateSet = ModBlocks.solidVines.getDefaultState().with(propMap.get(dir.getOpposite()), true);

							if (first && !stateSet.canPlaceAt(world, pos)) {
								break;
							}
							first = false;

							world.setBlockState(pos, stateSet);
							world.syncWorldEvent(2001, pos, Block.getRawIdFromState(stateSet));
							pos = pos.down();
						} else {
							break;
						}
					}
				}

			}

			this.world.sendEntityStatus(this, (byte) 3);
			remove();
		}
	}

	@Override
	protected float getGravity() {
		return dataTracker.get(GRAVITY);
	}

	@Nonnull
	@Override
	public ItemStack getStack() {
		return new ItemStack(ModItems.vineBall);
	}
}
