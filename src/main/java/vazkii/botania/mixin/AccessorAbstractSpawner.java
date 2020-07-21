/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobSpawnerLogic.class)
public interface AccessorAbstractSpawner {
	@Invoker("func_221409_a")
	void callSpawnEntity(Entity e);

	@Invoker
	boolean callIsActivated();

	@Invoker
	void callResetTimer();

	@Accessor
	int getSpawnCount();

	@Accessor
	int getSpawnRange();

	@Accessor
	MobSpawnerEntry getSpawnData();

	@Accessor
	int getMaxNearbyEntities();

	@Accessor
	double getMobRotation();

	@Accessor
	void setMobRotation(double rot);

	@Accessor
	void setPrevMobRotation(double rot);

	@Accessor
	int getSpawnDelay();

	@Accessor
	void setSpawnDelay(int delay);
}
