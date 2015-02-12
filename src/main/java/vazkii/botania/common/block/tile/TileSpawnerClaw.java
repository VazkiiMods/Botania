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

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.WeightedRandom;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class TileSpawnerClaw extends TileMod implements IManaReceiver {

	private static final String TAG_MANA = "mana";

	int mana = 0;

	@Override
	public void updateEntity() {
		TileEntity tileBelow = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
		if(mana >= 5 && tileBelow instanceof TileEntityMobSpawner) {
			TileEntityMobSpawner spawner = (TileEntityMobSpawner) tileBelow;
			MobSpawnerBaseLogic logic = spawner.func_145881_a();

			if(!logic.isActivated()) {
				if(!worldObj.isRemote)
					mana -= 6;

				if(logic.getSpawnerWorld().isRemote) {
					if(logic.spawnDelay > 0)
						--logic.spawnDelay;

					if(Math.random() > 0.5)
						Botania.proxy.wispFX(worldObj, xCoord + 0.3 + Math.random() * 0.5, yCoord - 0.3 + Math.random() * 0.25, zCoord + Math.random(), 0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, (float) Math.random() / 3F, -0.025F - 0.005F * (float) Math.random(), 2F);

					logic.field_98284_d = logic.field_98287_c;
					logic.field_98287_c = (logic.field_98287_c + 1000.0F / (logic.spawnDelay + 200.0F)) % 360.0D;
				} else if(logic.spawnDelay == -1)
					resetTimer(logic);

				if(logic.spawnDelay > 0) {
					--logic.spawnDelay;
					return;
				}

				boolean flag = false;

				int spawnCount = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.SPAWN_COUNT);
				int spawnRange = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.SPAWN_RANGE);
				int maxNearbyEntities = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.MAX_NEARBY_ENTITIES);

				for(int i = 0; i < spawnCount; ++i) {
					Entity entity = EntityList.createEntityByName(logic.getEntityNameToSpawn(), logic.getSpawnerWorld());

					if (entity == null)
						return;

					int j = logic.getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getBoundingBox(logic.getSpawnerX(), logic.getSpawnerY(), logic.getSpawnerZ(), logic.getSpawnerX() + 1, logic.getSpawnerY() + 1, logic.getSpawnerZ() + 1).expand(spawnRange * 2, 4.0D, spawnRange * 2)).size();

					if (j >= maxNearbyEntities) {
						resetTimer(logic);
						return;
					}

					double d2 = logic.getSpawnerX() + (logic.getSpawnerWorld().rand.nextDouble() - logic.getSpawnerWorld().rand.nextDouble()) * spawnRange;
					double d3 = logic.getSpawnerY() + logic.getSpawnerWorld().rand.nextInt(3) - 1;
					double d4 = logic.getSpawnerZ() + (logic.getSpawnerWorld().rand.nextDouble() - logic.getSpawnerWorld().rand.nextDouble()) * spawnRange;
					EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
					entity.setLocationAndAngles(d2, d3, d4, logic.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);

					if(entityliving == null || entityliving.getCanSpawnHere()) {
						if(!worldObj.isRemote)
							logic.func_98265_a(entity);
						logic.getSpawnerWorld().playAuxSFX(2004, logic.getSpawnerX(), logic.getSpawnerY(), logic.getSpawnerZ(), 0);

						if (entityliving != null)
							entityliving.spawnExplosionParticle();

						flag = true;
					}
				}

				if (flag)
					resetTimer(logic);
			}
		}
	}

	private void resetTimer(MobSpawnerBaseLogic logic) {
		int maxSpawnDelay = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.MAX_SPAWN_DELAY);
		int minSpawnDelay = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.MIN_SPAWN_DELAY);
		List potentialEntitySpawns = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, logic, LibObfuscation.POTENTIAL_ENTITY_SPAWNS);

		if(maxSpawnDelay <= minSpawnDelay)
			logic.spawnDelay = minSpawnDelay;
		else {
			int i = maxSpawnDelay - minSpawnDelay;
			logic.spawnDelay = minSpawnDelay + logic.getSpawnerWorld().rand.nextInt(i);
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
