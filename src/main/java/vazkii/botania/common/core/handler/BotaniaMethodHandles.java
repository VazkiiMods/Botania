package vazkii.botania.common.core.handler;

import com.google.common.base.Throwables;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.common.lib.LibObfuscation;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

// MethodHandles for frequently reflected fields and methods.
// Handles suffixed with _getter and _setter are for fields, while those without suffixes are normal methods.
public final class BotaniaMethodHandles {

    public static final MethodHandle
            renderPosX_getter, renderPosY_getter, renderPosZ_getter, // RenderManager
            itemAge_getter, itemAge_setter, pickupDelay_getter, // EntityItem, pickup delay already has setter in vanilla
            spawnRange_getter, spawnCount_getter, maxNearbyEntities_getter,
            maxSpawnDelay_getter, minSpawnDelay_getter,
            spawnDelay_getter, spawnDelay_setter, prevMobRotation_setter, mobRotation_setter, potentialSpawns_getter,
            getEntityNameToSpawn, isActivated, spawnNewEntity; // MobSpawnerBaseLogic

    static {
        try {
            Field f = ReflectionHelper.findField(RenderManager.class, LibObfuscation.RENDERPOSX);
            f.setAccessible(true);
            renderPosX_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderManager.class, LibObfuscation.RENDERPOSY);
            f.setAccessible(true);
            renderPosY_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderManager.class, LibObfuscation.RENDERPOSZ);
            f.setAccessible(true);
            renderPosZ_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(EntityItem.class, LibObfuscation.AGE);
            f.setAccessible(true);
            itemAge_getter = MethodHandles.publicLookup().unreflectGetter(f);
            itemAge_setter = MethodHandles.publicLookup().unreflectSetter(f);

            f = ReflectionHelper.findField(EntityItem.class, LibObfuscation.PICKUP_DELAY);
            f.setAccessible(true);
            pickupDelay_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.SPAWN_RANGE);
            f.setAccessible(true);
            spawnRange_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.SPAWN_COUNT);
            f.setAccessible(true);
            spawnCount_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.MAX_NEARBY_ENTITIES);
            f.setAccessible(true);
            maxNearbyEntities_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.MAX_SPAWN_DELAY);
            f.setAccessible(true);
            maxSpawnDelay_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.MIN_SPAWN_DELAY);
            f.setAccessible(true);
            minSpawnDelay_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.SPAWN_DELAY);
            f.setAccessible(true);
            spawnDelay_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.SPAWN_DELAY);
            f.setAccessible(true);
            spawnDelay_setter = MethodHandles.publicLookup().unreflectSetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.PREV_MOB_ROTATION);
            f.setAccessible(true);
            prevMobRotation_setter = MethodHandles.publicLookup().unreflectSetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.MOB_ROTATION);
            f.setAccessible(true);
            mobRotation_setter = MethodHandles.publicLookup().unreflectSetter(f);

            f = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.POTENTIAL_ENTITY_SPAWNS);
            f.setAccessible(true);
            potentialSpawns_getter = MethodHandles.publicLookup().unreflectGetter(f);

            Method m = ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, null, LibObfuscation.IS_ACTIVATED);
            m.setAccessible(true);
            isActivated = MethodHandles.publicLookup().unreflect(m);

            m = ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, null, LibObfuscation.SPAWN_NEW_ENTITY, Entity.class, boolean.class);
            m.setAccessible(true);
            spawnNewEntity = MethodHandles.publicLookup().unreflect(m);

            m = ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, null, LibObfuscation.GET_ENTITY_TO_SPAWN);
            m.setAccessible(true);
            getEntityNameToSpawn = MethodHandles.publicLookup().unreflect(m);
        } catch (Throwable t) {
            throw Throwables.propagate(t);
        }
    }

    private BotaniaMethodHandles() {}
}
