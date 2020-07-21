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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EnvironmentInterface(value = EnvType.CLIENT, itf = IRendersAsItem.class)
public class EntityEnderAirBottle extends ThrownEntity implements FlyingItemEntity {
	public EntityEnderAirBottle(EntityType<EntityEnderAirBottle> type, World world) {
		super(type, world);
	}

	public EntityEnderAirBottle(LivingEntity entity, World world) {
		super(ModEntities.ENDER_AIR_BOTTLE, entity, world);
	}

	@Override
	protected void onCollision(@Nonnull HitResult pos) {
		if (pos.getType() == HitResult.Type.BLOCK && !world.isClient) {
			List<BlockPos> coordsList = getCoordsToPut(((BlockHitResult) pos).getBlockPos());
			world.syncWorldEvent(2002, getBlockPos(), 8);

			for (BlockPos coords : coordsList) {
				world.setBlockState(coords, Blocks.END_STONE.getDefaultState());
				if (Math.random() < 0.1) {
					world.syncWorldEvent(2001, coords, Block.getRawIdFromState(Blocks.END_STONE.getDefaultState()));
				}
			}
			remove();
		}
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
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Nonnull
	@Override
	public ItemStack getStack() {
		return new ItemStack(ModItems.enderAirBottle);
	}
}
