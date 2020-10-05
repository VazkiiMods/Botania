/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

<<<<<<< HEAD
import net.minecraft.entity.Entity;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
=======
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.world.spawner.AbstractSpawner;

>>>>>>> 4c77b50dc7c48e2738e9dca513b25bdb627819fb
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobSpawnerLogic.class)
public interface AccessorAbstractSpawner {
	@Invoker
	boolean callIsPlayerInRange();

	@Invoker
	void callUpdateSpawns();

	@Accessor
	int getSpawnCount();

	@Accessor
	int getSpawnRange();

	@Accessor
	MobSpawnerEntry getSpawnEntry();

	@Accessor
	int getMaxNearbyEntities();

	@Accessor("field_9161")
	double getMobRotation();

	@Accessor("field_9161")
	void setMobRotation(double rot);

	@Accessor("field_9159")
	void setPrevMobRotation(double rot);

	@Accessor
	int getSpawnDelay();

	@Accessor
	void setSpawnDelay(int delay);
}
