/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class SolegnoliaBlockEntity extends SpecialFlowerBlockEntity {
	private static final double RANGE = 5;
	private static final double RANGE_MINI = 1;

	private static final Set<SolegnoliaBlockEntity> clientFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Set<SolegnoliaBlockEntity> serverFlowers = Collections.newSetFromMap(new WeakHashMap<>());

	private boolean added = false;

	protected SolegnoliaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SolegnoliaBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaBlockEntities.SOLEGNOLIA, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!added) {
			if (getLevel().isClientSide()) {
				clientFlowers.add(this);
			} else {
				serverFlowers.add(this);
			}
			added = true;
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (getLevel().isClientSide()) {
			clientFlowers.remove(this);
		} else {
			serverFlowers.remove(this);
		}
	}

	public static boolean hasSolegnoliaAround(Entity e) {
		for (var flower : e.level().isClientSide() ? clientFlowers : serverFlowers) {
			if (!flower.isPowered() && flower.getLevel() == e.level()
					&& flower.getEffectivePos().distToCenterSqr(e.getX(), e.getY(), e.getZ())
							<= flower.getRange() * flower.getRange()) {
				return true;
			}
		}
		return false;
	}

	public double getRange() {
		return RANGE;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), getRange());
	}

	public static class Mini extends SolegnoliaBlockEntity {
		public Mini(BlockPos pos, BlockState state) {
			super(BotaniaBlockEntities.SOLEGNOLIA_PETITE, pos, state);
		}

		@Override
		public double getRange() {
			return RANGE_MINI;
		}
	}

}
