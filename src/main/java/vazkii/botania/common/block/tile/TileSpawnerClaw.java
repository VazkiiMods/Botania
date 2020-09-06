/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Jul 23, 2014, 5:32:11 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ITickable;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.Botania;

public class TileSpawnerClaw extends TileMod implements IManaReceiver, ITickable {

	private static final String TAG_MANA = "mana";
	private static final int MAX_MANA = 160;

	int mana = 0;

	@Override
	public void update() {
		TileEntity tileBelow = world.getTileEntity(pos.down());
		if(mana >= 5 && tileBelow instanceof TileEntityMobSpawner) {
			TileEntityMobSpawner spawner = (TileEntityMobSpawner) tileBelow;
			MobSpawnerBaseLogic logic = spawner.getSpawnerBaseLogic();

			BlockPos blockpos = logic.getSpawnerPosition();

			// Directly drawn from MobSpawnerBaseLogic, with inverted isActivated check and mana consumption
			if(!logic.isActivated()) {
				if(!world.isRemote)
					mana -= 6;

				if(logic.getSpawnerWorld().isRemote) {
					if(logic.spawnDelay > 0) {
						--logic.spawnDelay;
					}

					if(Math.random() > 0.5)
						Botania.proxy.wispFX(getPos().getX() + 0.3 + Math.random() * 0.5, getPos().getY() - 0.3 + Math.random() * 0.25, getPos().getZ() + Math.random(), 0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, (float) Math.random() / 3F, -0.025F - 0.005F * (float) Math.random(), 2F);

					logic.prevMobRotation = logic.mobRotation;
					logic.mobRotation = (logic.mobRotation + (double) (1000.0F / ((float) logic.spawnDelay + 200.0F))) % 360.0D;
				} else {
					if(logic.spawnDelay == -1) {
						logic.resetTimer();
					}

					if(logic.spawnDelay > 0) {
						--logic.spawnDelay;
						return;
					}

					boolean flag = false;

					for(int i = 0; i < logic.spawnCount; ++i) {
						NBTTagCompound nbttagcompound = logic.spawnData.getNbt();
						NBTTagList nbttaglist = nbttagcompound.getTagList("Pos", 6);
						World world = logic.getSpawnerWorld();
						int j = nbttaglist.tagCount();
						double d0 = j >= 1 ? nbttaglist.getDoubleAt(0) : (double) blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) logic.spawnRange + 0.5D;
						double d1 = j >= 2 ? nbttaglist.getDoubleAt(1) : (double) (blockpos.getY() + world.rand.nextInt(3) - 1);
						double d2 = j >= 3 ? nbttaglist.getDoubleAt(2) : (double) blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) logic.spawnRange + 0.5D;
						Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, false);

						if(entity == null) {
							return;
						}

						int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), (double) (blockpos.getX() + 1), (double) (blockpos.getY() + 1), (double) (blockpos.getZ() + 1))).grow((double) logic.spawnRange)).size();

						if(k >= logic.maxNearbyEntities) {
							logic.resetTimer();
							return;
						}

						EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;
						entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0F, 0.0F);

						if(entityliving == null || net.minecraftforge.event.ForgeEventFactory.canEntitySpawnSpawner(entityliving, world, (float) entity.posX, (float) entity.posY, (float) entity.posZ)) {
							if(logic.spawnData.getNbt().getSize() == 1 && logic.spawnData.getNbt().hasKey("id", 8) && entity instanceof EntityLiving) {
								if(!net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(entityliving, logic.getSpawnerWorld(), (float) entity.posX, (float) entity.posY, (float) entity.posZ))
									((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData) null);
							}

							AnvilChunkLoader.spawnEntity(entity, world);
							world.playEvent(2004, blockpos, 0);

							if(entityliving != null) {
								entityliving.spawnExplosionParticle();
							}

							flag = true;
						}
					}

					if(flag) {
						logic.resetTimer();
					}
				}
			}
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(3 * MAX_MANA, this.mana + mana);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

}
