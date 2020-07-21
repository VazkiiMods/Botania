/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.mixin.AccessorAbstractSpawner;

import java.util.Optional;

public class TileSpawnerClaw extends TileMod implements IManaReceiver, Tickable {
	private static final String TAG_MANA = "mana";

	private int mana = 0;

	public TileSpawnerClaw() {
		super(ModTiles.SPAWNER_CLAW);
	}

	@Override
	public void tick() {
		BlockEntity tileBelow = world.getBlockEntity(pos.down());
		if (mana >= 5 && tileBelow instanceof MobSpawnerBlockEntity) {
			MobSpawnerBlockEntity spawner = (MobSpawnerBlockEntity) tileBelow;
			MobSpawnerLogic logic = spawner.getLogic();
			AccessorAbstractSpawner mLogic = (AccessorAbstractSpawner) logic;

			// [VanillaCopy] AbstractSpawner.tick, edits noted
			if (!mLogic.callIsActivated()) { // Activate when vanilla is *not* running the spawner
				mLogic.setPrevMobRotation(mLogic.getMobRotation());
			} else {
				World world = this.getWorld();
				BlockPos blockpos = logic.getPos();
				if (world.isClient) {
					// Botania - use own particles
					if (Math.random() > 0.5) {
						WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F, 0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, 2F);
						world.addParticle(data, getPos().getX() + 0.3 + Math.random() * 0.5, getPos().getY() - 0.3 + Math.random() * 0.25, getPos().getZ() + Math.random(), 0, -(-0.025F - 0.005F * (float) Math.random()), 0);
					}
					if (mLogic.getSpawnDelay() > 0) {
						mLogic.setSpawnDelay(mLogic.getSpawnDelay() - 1);
					}

					mLogic.setPrevMobRotation(mLogic.getMobRotation());
					mLogic.setMobRotation((mLogic.getMobRotation() + (double) (1000.0F / ((float) mLogic.getSpawnDelay() + 200.0F))) % 360.0D);
				} else {
					// Botania - consume mana
					this.mana -= 6;

					if (mLogic.getSpawnDelay() == -1) {
						mLogic.callResetTimer();
					}

					if (mLogic.getSpawnDelay() > 0) {
						mLogic.setSpawnDelay(mLogic.getSpawnDelay() - 1);
						return;
					}

					boolean flag = false;

					for (int i = 0; i < mLogic.getSpawnCount(); ++i) {
						CompoundTag compoundnbt = mLogic.getSpawnData().getEntityTag();
						Optional<EntityType<?>> optional = EntityType.fromTag(compoundnbt);
						if (!optional.isPresent()) {
							mLogic.callResetTimer();
							return;
						}

						ListTag listnbt = compoundnbt.getList("Pos", 6);
						int j = listnbt.size();
						double d0 = j >= 1 ? listnbt.getDouble(0) : (double) blockpos.getX() + (world.random.nextDouble() - world.random.nextDouble()) * (double) mLogic.getSpawnRange() + 0.5D;
						double d1 = j >= 2 ? listnbt.getDouble(1) : (double) (blockpos.getY() + world.random.nextInt(3) - 1);
						double d2 = j >= 3 ? listnbt.getDouble(2) : (double) blockpos.getZ() + (world.random.nextDouble() - world.random.nextDouble()) * (double) mLogic.getSpawnRange() + 0.5D;
						if (world.doesNotCollide(optional.get().createSimpleBoundingBox(d0, d1, d2))) {
							Entity entity = EntityType.loadEntityWithPassengers(compoundnbt, world, (p_221408_6_) -> {
								p_221408_6_.refreshPositionAndAngles(d0, d1, d2, p_221408_6_.yaw, p_221408_6_.pitch);
								return p_221408_6_;
							});
							if (entity == null) {
								mLogic.callResetTimer();
								return;
							}

							int k = world.getNonSpectatingEntities(entity.getClass(), (new Box((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), (double) (blockpos.getX() + 1), (double) (blockpos.getY() + 1), (double) (blockpos.getZ() + 1))).expand((double) mLogic.getSpawnRange())).size();
							if (k >= mLogic.getMaxNearbyEntities()) {
								mLogic.callResetTimer();
								return;
							}

							entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), world.random.nextFloat() * 360.0F, 0.0F);
							if (entity instanceof MobEntity) {
								MobEntity mobentity = (MobEntity) entity;
								if (!net.minecraftforge.event.ForgeEventFactory.canEntitySpawnSpawner(mobentity, world, (float) entity.getX(), (float) entity.getY(), (float) entity.getZ(), logic)) {
									continue;
								}

								if (mLogic.getSpawnData().getEntityTag().getSize() == 1 && mLogic.getSpawnData().getEntityTag().contains("id", 8)) {
									if (!net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(mobentity, world, (float) entity.getX(), (float) entity.getY(), (float) entity.getZ(), logic, SpawnReason.SPAWNER)) {
										((MobEntity) entity).initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, (EntityData) null, (CompoundTag) null);
									}
								}
							}

							mLogic.callSpawnEntity(entity);
							world.syncWorldEvent(2004, blockpos, 0);
							if (entity instanceof MobEntity) {
								((MobEntity) entity).playSpawnEffects();
							}

							flag = true;
						}
					}

					if (flag) {
						mLogic.callResetTimer();
					}
				}
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
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
