/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BaseSpawner.class)
public interface AccessorBaseSpawner {
	@Invoker("isNearPlayer")
	boolean botania_isPlayerInRange(Level level, BlockPos pos);

	@Invoker("delay")
	void botania_updateSpawns(Level level, BlockPos pos);

	@Accessor
	int getSpawnCount();

	@Accessor
	int getSpawnRange();

	@Accessor("nextSpawnData")
	SpawnData getSpawnEntry();

	@Accessor
	int getMaxNearbyEntities();

	@Accessor("spin")
	double getMobRotation();

	@Accessor("spin")
	void setMobRotation(double rot);

	@Accessor("oSpin")
	void setPrevMobRotation(double rot);

	@Accessor
	int getSpawnDelay();

	@Accessor
	void setSpawnDelay(int delay);
}
