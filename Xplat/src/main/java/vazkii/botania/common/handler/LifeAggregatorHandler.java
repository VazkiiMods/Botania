/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.handler;

import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.LifeAggregatorCarryable;
import vazkii.botania.mixin.BaseSpawnerAccessor;

/**
 * LifeAggregatorCarryable implementations for vanilla blocks the Life Aggregator should be able to pick up by default.
 */
public class LifeAggregatorHandler {
	private LifeAggregatorHandler() {}

	/**
	 * Examines the given list of mob spawn data to return the first entry's entity type, if there is only one entry.
	 * 
	 * @param spawnPotentials The spawn potentials list.
	 * @return The {@link EntityType} for the list's first entry, if the list has exactly one entry, otherwise
	 *         <code>null</code>.
	 */
	public static @Nullable EntityType<?> getEntityType(SimpleWeightedRandomList<SpawnData> spawnPotentials) {
		var unwrappedList = spawnPotentials.unwrap();
		if (unwrappedList.size() != 1) {
			return null;
		}

		return EntityType.by(unwrappedList.getFirst().data().entityToSpawn()).orElse(null);
	}

	/**
	 * LifeAggregatorCarryable implementation for standard monster spawners.
	 */
	public record MonsterSpawnerCarryable(SpawnerBlockEntity blockEntity)
			implements
				LifeAggregatorCarryable.LifeAggregatorCarryableBlockEntity<SpawnerBlockEntity> {

		@Override
		public @Nullable EntityType<?> getEntityType() {
			return LifeAggregatorHandler.getEntityType(
					((BaseSpawnerAccessor) this.blockEntity.getSpawner()).botania_getSpawnPotentials());
		}
	}

	/**
	 * LifeAggregatorCarryable implementation for trial spawners.
	 */
	public record TrialSpawnerCarryable(TrialSpawnerBlockEntity blockEntity)
			implements
				LifeAggregatorCarryable.LifeAggregatorCarryableBlockEntity<TrialSpawnerBlockEntity> {

		@Override
		public @Nullable EntityType<?> getEntityType() {
			return LifeAggregatorHandler.getEntityType(
					this.blockEntity.getTrialSpawner().getConfig().spawnPotentialsDefinition());
		}
	}
}
