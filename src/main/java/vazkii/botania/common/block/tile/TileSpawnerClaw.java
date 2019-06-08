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
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.init.Particles;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileSpawnerClaw extends TileMod implements IManaReceiver, ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.SPAWNER_CLAW)
	public static TileEntityType<TileSpawnerClaw> TYPE;
	private static final String TAG_MANA = "mana";

	private int mana = 0;

	public TileSpawnerClaw() {
		super(TYPE);
	}

	@Override
	public void tick() {
		TileEntity tileBelow = world.getTileEntity(pos.down());
		if(mana >= 5 && tileBelow instanceof MobSpawnerTileEntity) {
			MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) tileBelow;
			AbstractSpawner logic = spawner.getSpawnerBaseLogic();

			// [VanillaCopy] MobSpawnBaseLogic.tick, edits noted
			if (!logic.isActivated()) { // Botania - invert activated check
				BlockPos blockpos = logic.getSpawnerPosition();
				if (this.getWorld().isRemote) {
					// Botania - use own particles
					if(Math.random() > 0.5)
						Botania.proxy.wispFX(getPos().getX() + 0.3 + Math.random() * 0.5, getPos().getY() - 0.3 + Math.random() * 0.25, getPos().getZ() + Math.random(), 0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, (float) Math.random() / 3F, -0.025F - 0.005F * (float) Math.random(), 2F);

					if (logic.spawnDelay > 0) {
						--logic.spawnDelay;
					}

					logic.prevMobRotation = logic.mobRotation;
					logic.mobRotation = (logic.mobRotation + (double)(1000.0F / ((float)logic.spawnDelay + 200.0F))) % 360.0D;
				} else {
					// Botania - consume mana
					this.mana -= 6;

					if (logic.spawnDelay == -1) {
						logic.resetTimer();
					}

					if (logic.spawnDelay > 0) {
						--logic.spawnDelay;
						return;
					}

					boolean flag = false;

					for(int i = 0; i < logic.spawnCount; ++i) {
						CompoundNBT nbttagcompound = logic.spawnData.getNbt();
						ListNBT nbttaglist = nbttagcompound.getList("Pos", 6);
						World world = logic.getWorld();
						int j = nbttaglist.size();
						double d0 = j >= 1 ? nbttaglist.getDouble(0) : (double)blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)logic.spawnRange + 0.5D;
						double d1 = j >= 2 ? nbttaglist.getDouble(1) : (double)(blockpos.getY() + world.rand.nextInt(3) - 1);
						double d2 = j >= 3 ? nbttaglist.getDouble(2) : (double)blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)logic.spawnRange + 0.5D;
						Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, false);
						if (entity == null) {
							logic.resetTimer();
							return;
						}

						int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), (double)(blockpos.getX() + 1), (double)(blockpos.getY() + 1), (double)(blockpos.getZ() + 1))).grow((double)logic.spawnRange)).size();
						if (k >= logic.maxNearbyEntities) {
							logic.resetTimer();
							return;
						}

						MobEntity entityliving = entity instanceof MobEntity ? (MobEntity)entity : null;
						entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0F, 0.0F);
						if (entityliving == null || net.minecraftforge.event.ForgeEventFactory.canEntitySpawnSpawner(entityliving, getWorld(), (float)entity.posX, (float)entity.posY, (float)entity.posZ, logic)) {
							if (logic.spawnData.getNbt().size() == 1 && logic.spawnData.getNbt().contains("id", 8) && entity instanceof MobEntity) {
								if (!net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(entityliving, logic.getWorld(), (float)entity.posX, (float)entity.posY, (float)entity.posZ, logic))
									((MobEntity)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (ILivingEntityData)null, (CompoundNBT)null);
							}

							AnvilChunkLoader.spawnEntity(entity, world);
							world.playEvent(2004, blockpos, 0);
							if (entityliving != null) {
								entityliving.spawnExplosionParticle();
							}

							flag = true;
						}
					}

					if (flag) {
						logic.resetTimer();
					}
				}
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		mana = cmp.getInt(TAG_MANA);
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
