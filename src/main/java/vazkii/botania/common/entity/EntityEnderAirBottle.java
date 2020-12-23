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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@EnvironmentInterface(value = EnvType.CLIENT, itf = FlyingItemEntity.class)
public class EntityEnderAirBottle extends ThrownEntity implements FlyingItemEntity {
	private static final Identifier GHAST_LOOT_TABLE = prefix("ghast_ender_air_crying");

	public EntityEnderAirBottle(EntityType<EntityEnderAirBottle> type, World world) {
		super(type, world);
	}

	public EntityEnderAirBottle(LivingEntity entity, World world) {
		super(ModEntities.ENDER_AIR_BOTTLE, entity, world);
	}

	public EntityEnderAirBottle(double x, double y, double z, World world) {
		super(ModEntities.ENDER_AIR_BOTTLE, x, y, z, world);
	}

	private void convertStone(@Nonnull BlockPos pos) {
		List<BlockPos> coordsList = getCoordsToPut(pos);
		world.syncWorldEvent(2002, getBlockPos(), 8);

		for (BlockPos coords : coordsList) {
			world.setBlockState(coords, Blocks.END_STONE.getDefaultState());
			if (Math.random() < 0.1) {
				world.syncWorldEvent(2001, coords, Block.getRawIdFromState(Blocks.END_STONE.getDefaultState()));
			}
		}
	}

	@Override
	protected void onBlockHit(@Nonnull BlockHitResult result) {
		if (world.isClient) {
			return;
		}
		convertStone(result.getBlockPos());
		remove();
	}

	@Override
	protected void onEntityHit(@Nonnull EntityHitResult result) {
		if (world.isClient) {
			return;
		}
		Entity entity = result.getEntity();
		if (entity.getType() == EntityType.GHAST && world.getRegistryKey() == World.OVERWORLD) {
			world.syncWorldEvent(2002, getBlockPos(), 8);
			DamageSource source = DamageSource.thrownProjectile(this, getOwner());
			entity.damage(source, 0);

			LootTable table = world.getServer().getLootManager().getTable(GHAST_LOOT_TABLE);
			LootContext.Builder builder = new LootContext.Builder(((ServerWorld) world));
			builder.parameter(LootContextParameters.THIS_ENTITY, entity);
			builder.parameter(LootContextParameters.ORIGIN, entity.getPos());
			builder.parameter(LootContextParameters.DAMAGE_SOURCE, source);

			LootContext context = builder.build(LootContextTypes.ENTITY);
			for (ItemStack stack : table.generateLoot(context)) {
				ItemEntity item = entity.dropStack(stack, 2);
				item.setVelocity(item.getVelocity().add(entity.getRotationVector().multiply(0.4)));
			}
		} else {
			convertStone(new BlockPos(result.getPos()));
		}
		remove();
	}

	private List<BlockPos> getCoordsToPut(BlockPos pos) {
		List<BlockPos> possibleCoords = new ArrayList<>();
		int range = 4;
		int rangeY = 4;

		for (BlockPos bPos : BlockPos.iterate(pos.add(-range, -rangeY, -range),
				pos.add(range, rangeY, range))) {
			BlockState state = world.getBlockState(bPos);
			if (state.getBlock() == Blocks.STONE) {
				possibleCoords.add(bPos.toImmutable());
			}
		}

		Collections.shuffle(possibleCoords, random);

		return possibleCoords.stream().limit(64).collect(Collectors.toList());
	}

	@Override
	protected void initDataTracker() {}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return PacketSpawnEntity.make(this);
	}

	@Nonnull
	@Override
	public ItemStack getStack() {
		return new ItemStack(ModItems.enderAirBottle);
	}
}
