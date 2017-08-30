package vazkii.botania.common.core.handler;

import static java.lang.invoke.MethodHandles.publicLookup;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.common.base.Throwables;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibObfuscation;

// MethodHandles for frequently reflected fields and methods.
// Handles suffixed with _getter and _setter are for fields, while those without suffixes are normal methods.
public final class MethodHandles {

	public static final MethodHandle
	itemAge_getter, itemAge_setter, pickupDelay_getter, // EntityItem, pickup delay already has setter in vanilla
	spawnRange_getter, spawnCount_getter, maxNearbyEntities_getter,
	maxSpawnDelay_getter, minSpawnDelay_getter,
	spawnDelay_getter, spawnDelay_setter, prevMobRotation_setter, mobRotation_setter, potentialSpawns_getter,
	randomEntity_getter, isActivated; // MobSpawnerBaseLogic


	static {
		try {
			Field f = ReflectionHelper.findField(EntityItem.class, LibObfuscation.AGE);
			f.setAccessible(true);
			itemAge_getter = publicLookup().unreflectGetter(f);
			itemAge_setter = publicLookup().unreflectSetter(f);

			f = ReflectionHelper.findField(EntityItem.class, LibObfuscation.PICKUP_DELAY);
			f.setAccessible(true);
			pickupDelay_getter = publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.SPAWN_RANGE);
			f.setAccessible(true);
			spawnRange_getter = publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.SPAWN_COUNT);
			f.setAccessible(true);
			spawnCount_getter = publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.MAX_NEARBY_ENTITIES);
			f.setAccessible(true);
			maxNearbyEntities_getter = publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.MAX_SPAWN_DELAY);
			f.setAccessible(true);
			maxSpawnDelay_getter = publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.MIN_SPAWN_DELAY);
			f.setAccessible(true);
			minSpawnDelay_getter = publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.SPAWN_DELAY);
			f.setAccessible(true);
			spawnDelay_getter = publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.SPAWN_DELAY);
			f.setAccessible(true);
			spawnDelay_setter = publicLookup().unreflectSetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.PREV_MOB_ROTATION);
			f.setAccessible(true);
			prevMobRotation_setter = publicLookup().unreflectSetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.MOB_ROTATION);
			f.setAccessible(true);
			mobRotation_setter = publicLookup().unreflectSetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.POTENTIAL_ENTITY_SPAWNS);
			f.setAccessible(true);
			potentialSpawns_getter = publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.RANDOM_ENTITY);
			f.setAccessible(true);
			randomEntity_getter = publicLookup().unreflectGetter(f);

			Method m = ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, null, LibObfuscation.IS_ACTIVATED);
			m.setAccessible(true);
			isActivated = publicLookup().unreflect(m);
		} catch (Throwable t) {
			Botania.LOGGER.fatal("Couldn't initialize methodhandles! Things will be broken!");
			t.printStackTrace();
			throw Throwables.propagate(t);
		}
	}

	private MethodHandles() {}
}
