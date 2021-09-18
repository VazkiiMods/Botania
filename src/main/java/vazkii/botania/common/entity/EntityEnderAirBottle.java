/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ItemSupplier.class)
public class EntityEnderAirBottle extends ThrowableProjectile implements ItemSupplier {
	private static final ResourceLocation GHAST_LOOT_TABLE = prefix("ghast_ender_air_crying");

	public EntityEnderAirBottle(EntityType<EntityEnderAirBottle> type, Level world) {
		super(type, world);
	}

	public EntityEnderAirBottle(LivingEntity entity, Level world) {
		super(ModEntities.ENDER_AIR_BOTTLE, entity, world);
	}

	public EntityEnderAirBottle(double x, double y, double z, Level world) {
		super(ModEntities.ENDER_AIR_BOTTLE, x, y, z, world);
	}

	private void convertStone(@Nonnull BlockPos pos) {
		List<BlockPos> coordsList = getCoordsToPut(pos);
		level.levelEvent(2002, blockPosition(), 8);

		for (BlockPos coords : coordsList) {
			level.setBlockAndUpdate(coords, Blocks.END_STONE.defaultBlockState());
			if (Math.random() < 0.1) {
				level.levelEvent(2001, coords, Block.getId(Blocks.END_STONE.defaultBlockState()));
			}
		}
	}

	@Override
	protected void onHitBlock(@Nonnull BlockHitResult result) {
		if (level.isClientSide) {
			return;
		}
		convertStone(result.getBlockPos());
		discard();
	}

	@Override
	protected void onHitEntity(@Nonnull EntityHitResult result) {
		if (level.isClientSide) {
			return;
		}
		Entity entity = result.getEntity();
		if (entity.getType() == EntityType.GHAST && level.dimension() == Level.OVERWORLD) {
			level.levelEvent(2002, blockPosition(), 8);
			DamageSource source = DamageSource.thrown(this, getOwner());
			entity.hurt(source, 0);

			// Ghasts always appear to be aligned horizontally - but the look doesn't always match, correct for that
			Vec3 lookVec = entity.getLookAngle();
			Vec3 vec = new Vec3(lookVec.x(), 0, lookVec.z()).normalize();

			// Position chosen to appear roughly in the ghast's face
			((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.GHAST_TEAR)),
					entity.getX() + (2.3 * vec.x), entity.getY() + vec.y + 2.6, entity.getZ() + (2.3 * vec.z),
					40,
					Math.abs(vec.z) + 0.15, 0.2, Math.abs(vec.x) + 0.15, 0.2);

			LootTable table = level.getServer().getLootTables().get(GHAST_LOOT_TABLE);
			LootContext.Builder builder = new LootContext.Builder(((ServerLevel) level));
			builder.withParameter(LootContextParams.THIS_ENTITY, entity);
			builder.withParameter(LootContextParams.ORIGIN, entity.position());
			builder.withParameter(LootContextParams.DAMAGE_SOURCE, source);

			LootContext context = builder.create(LootContextParamSets.ENTITY);
			for (ItemStack stack : table.getRandomItems(context)) {
				ItemEntity item = entity.spawnAtLocation(stack, 2);
				item.setDeltaMovement(item.getDeltaMovement().add(vec.scale(0.4)));
			}
		} else {
			convertStone(new BlockPos(result.getLocation()));
		}
		discard();
	}

	private List<BlockPos> getCoordsToPut(BlockPos pos) {
		List<BlockPos> possibleCoords = new ArrayList<>();
		int range = 4;
		int rangeY = 4;

		for (BlockPos bPos : BlockPos.betweenClosed(pos.offset(-range, -rangeY, -range),
				pos.offset(range, rangeY, range))) {
			BlockState state = level.getBlockState(bPos);
			if (state.is(Blocks.STONE)) {
				possibleCoords.add(bPos.immutable());
			}
		}

		Collections.shuffle(possibleCoords, random);

		return possibleCoords.stream().limit(64).collect(Collectors.toList());
	}

	@Override
	protected void defineSynchedData() {}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return PacketSpawnEntity.make(this);
	}

	@Nonnull
	@Override
	public ItemStack getItem() {
		return new ItemStack(ModItems.enderAirBottle);
	}
}
