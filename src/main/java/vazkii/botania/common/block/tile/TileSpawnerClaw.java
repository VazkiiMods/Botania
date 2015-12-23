/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 23, 2014, 5:32:11 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.WeightedRandom;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibObfuscation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class TileSpawnerClaw extends TileMod implements IManaReceiver, ITickable {

	private static final String TAG_MANA = "mana";

	int mana = 0;

	private static final Method isActivated = ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, null, LibObfuscation.IS_ACTIVATED);
	private static final Field spawnDelay = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.SPAWN_DELAY);
	private static final Field mobRotation = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.MOB_ROTATION);
	private static final Field prevMobRotation = ReflectionHelper.findField(MobSpawnerBaseLogic.class, LibObfuscation.PREV_MOB_ROTATION);
	private static final Method spawnNewEntity = ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, null, LibObfuscation.SPAWN_NEW_ENTITY, Entity.class, boolean.class);
	private static final Method getEntityNameToSpawn = ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, null, LibObfuscation.GET_ENTITY_TO_SPAWN);

	@Override
	public void update() {
		TileEntity tileBelow = worldObj.getTileEntity(pos.down());
		if(mana >= 5 && tileBelow instanceof TileEntityMobSpawner) {
			TileEntityMobSpawner spawner = (TileEntityMobSpawner) tileBelow;
			MobSpawnerBaseLogic logic = spawner.getSpawnerBaseLogic();

			try {
				if(!((Boolean) isActivated.invoke(logic))) {
                    if(!worldObj.isRemote)
                        mana -= 6;

                    if(logic.getSpawnerWorld().isRemote) {
						int delay = spawnDelay.getInt(logic);
                        if(delay > 0)
							spawnDelay.setInt(logic, delay - 1);

                        if(Math.random() > 0.5)
                            Botania.proxy.wispFX(worldObj, getPos().getX() + 0.3 + Math.random() * 0.5, getPos().getY() - 0.3 + Math.random() * 0.25, getPos().getZ() + Math.random(), 0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, (float) Math.random() / 3F, -0.025F - 0.005F * (float) Math.random(), 2F);

						prevMobRotation.set(logic, logic.getMobRotation());
                        mobRotation.set(logic, (logic.getMobRotation() + 1000.0F / (spawnDelay.getInt(logic) + 200.0F)) % 360.0D);
                    } else if(spawnDelay.getInt(logic) == -1)
                        resetTimer(logic);

					int delay = spawnDelay.getInt(logic);
					if(delay > 0) {
						spawnDelay.setInt(logic, delay - 1);
                        return;
                    }

                    boolean flag = false;

                    int spawnCount = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.SPAWN_COUNT);
                    int spawnRange = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.SPAWN_RANGE);
                    int maxNearbyEntities = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.MAX_NEARBY_ENTITIES);

                    for(int i = 0; i < spawnCount; ++i) {
                        Entity entity = EntityList.createEntityByName(((String) getEntityNameToSpawn.invoke(logic)), logic.getSpawnerWorld());

                        if (entity == null)
                            return;

                        int j = logic.getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), new AxisAlignedBB(logic.getSpawnerPosition(), logic.getSpawnerPosition().add(1, 1, 1)).expand(spawnRange * 2, 4.0D, spawnRange * 2)).size();

                        if (j >= maxNearbyEntities) {
                            resetTimer(logic);
                            return;
                        }

                        double d2 = logic.getSpawnerPosition().getX() + (logic.getSpawnerWorld().rand.nextDouble() - logic.getSpawnerWorld().rand.nextDouble()) * spawnRange;
                        double d3 = logic.getSpawnerPosition().getY() + logic.getSpawnerWorld().rand.nextInt(3) - 1;
                        double d4 = logic.getSpawnerPosition().getZ() + (logic.getSpawnerWorld().rand.nextDouble() - logic.getSpawnerWorld().rand.nextDouble()) * spawnRange;
                        EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                        entity.setLocationAndAngles(d2, d3, d4, logic.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);

                        if(entityliving == null || entityliving.getCanSpawnHere()) {
                            if(!worldObj.isRemote)
                                spawnNewEntity.invoke(logic, entity, true);
                            logic.getSpawnerWorld().playAuxSFX(2004, logic.getSpawnerPosition(), 0);

                            if (entityliving != null)
                                entityliving.spawnExplosionParticle();

                            flag = true;
                        }
                    }

                    if (flag)
                        resetTimer(logic);
                }
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	private void resetTimer(MobSpawnerBaseLogic logic) throws IllegalAccessException {
		int maxSpawnDelay = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.MAX_SPAWN_DELAY);
		int minSpawnDelay = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.MIN_SPAWN_DELAY);
		List potentialEntitySpawns = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.POTENTIAL_ENTITY_SPAWNS);

		if(maxSpawnDelay <= minSpawnDelay)
			spawnDelay.set(logic, minSpawnDelay);
		else {
			int i = maxSpawnDelay - minSpawnDelay;
			spawnDelay.set(logic, minSpawnDelay + logic.getSpawnerWorld().rand.nextInt(i));
		}

		if(potentialEntitySpawns != null && potentialEntitySpawns.size() > 0)
			logic.setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart)WeightedRandom.getRandomItem(logic.getSpawnerWorld().rand, potentialEntitySpawns));

		logic.func_98267_a(1);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= 160;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(160, this.mana + mana);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

}
