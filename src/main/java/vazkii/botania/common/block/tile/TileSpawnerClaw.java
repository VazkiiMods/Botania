/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.Optional;

public class TileSpawnerClaw extends TileMod implements IManaReceiver, ITickableTileEntity {
	private static final String TAG_MANA = "mana";

	private int mana = 0;

	public TileSpawnerClaw() {
		super(ModTiles.SPAWNER_CLAW);
	}

	@Override
	public void tick() {
		TileEntity tileBelow = world.getTileEntity(pos.down());
		if (mana >= 5 && tileBelow instanceof MobSpawnerTileEntity) {
			MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) tileBelow;
			AbstractSpawner logic = spawner.getSpawnerBaseLogic();

			// [VanillaCopy] AbstractSpawner.tick, edits noted
			if (!logic.isActivated()) {
				logic.prevMobRotation = logic.mobRotation;
			} else {
				World world = this.getWorld();
				BlockPos blockpos = logic.getSpawnerPosition();
				if (world.isRemote) {
					// Botania - use own particles
					if (Math.random() > 0.5) {
						WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F, 0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, 2F);
						world.addParticle(data, getPos().getX() + 0.3 + Math.random() * 0.5, getPos().getY() - 0.3 + Math.random() * 0.25, getPos().getZ() + Math.random(), 0, -(-0.025F - 0.005F * (float) Math.random()), 0);
					}
					if (logic.spawnDelay > 0) {
						--logic.spawnDelay;
					}

					logic.prevMobRotation = logic.mobRotation;
					logic.mobRotation = (logic.mobRotation + (double) (1000.0F / ((float) logic.spawnDelay + 200.0F))) % 360.0D;
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

					for (int i = 0; i < logic.spawnCount; ++i) {
						CompoundNBT compoundnbt = logic.spawnData.getNbt();
						Optional<EntityType<?>> optional = EntityType.readEntityType(compoundnbt);
						if (!optional.isPresent()) {
							logic.resetTimer();
							return;
						}

						ListNBT listnbt = compoundnbt.getList("Pos", 6);
						int j = listnbt.size();
						double d0 = j >= 1 ? listnbt.getDouble(0) : (double) blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) logic.spawnRange + 0.5D;
						double d1 = j >= 2 ? listnbt.getDouble(1) : (double) (blockpos.getY() + world.rand.nextInt(3) - 1);
						double d2 = j >= 3 ? listnbt.getDouble(2) : (double) blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) logic.spawnRange + 0.5D;
						if (world.hasNoCollisions(optional.get().func_220328_a(d0, d1, d2))) {
							Entity entity = EntityType.func_220335_a(compoundnbt, world, (p_221408_6_) -> {
								p_221408_6_.setLocationAndAngles(d0, d1, d2, p_221408_6_.rotationYaw, p_221408_6_.rotationPitch);
								return p_221408_6_;
							});
							if (entity == null) {
								logic.resetTimer();
								return;
							}

							int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), (double) (blockpos.getX() + 1), (double) (blockpos.getY() + 1), (double) (blockpos.getZ() + 1))).grow((double) logic.spawnRange)).size();
							if (k >= logic.maxNearbyEntities) {
								logic.resetTimer();
								return;
							}

							entity.setLocationAndAngles(entity.getPosX(), entity.getPosY(), entity.getPosZ(), world.rand.nextFloat() * 360.0F, 0.0F);
							if (entity instanceof MobEntity) {
								MobEntity mobentity = (MobEntity) entity;
								if (!net.minecraftforge.event.ForgeEventFactory.canEntitySpawnSpawner(mobentity, world, (float) entity.getPosX(), (float) entity.getPosY(), (float) entity.getPosZ(), logic)) {
									continue;
								}

								if (logic.spawnData.getNbt().size() == 1 && logic.spawnData.getNbt().contains("id", 8)) {
									((MobEntity) entity).onInitialSpawn(world, world.getDifficultyForLocation(entity.func_233580_cy_()), SpawnReason.SPAWNER, (ILivingEntityData) null, (CompoundNBT) null);
								}
							}

							func_221409_a(entity);
							world.playEvent(2004, blockpos, 0);
							if (entity instanceof MobEntity) {
								((MobEntity) entity).spawnExplosionParticle();
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

	// [VanillaCopy] AbstractSpawner
	private void func_221409_a(Entity p_221409_1_) {
		if (this.getWorld().addEntity(p_221409_1_)) {
			for (Entity entity : p_221409_1_.getPassengers()) {
				this.func_221409_a(entity);
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
	public void receiveMana(int mana) {
		this.mana = Math.min(160, this.mana + mana);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}

}
