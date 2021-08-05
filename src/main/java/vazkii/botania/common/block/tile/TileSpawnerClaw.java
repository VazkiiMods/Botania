/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.mixin.AccessorAbstractSpawner;

import java.util.Optional;

public class TileSpawnerClaw extends TileMod implements IManaReceiver, TickableBlockEntity {
	private static final String TAG_MANA = "mana";
	private static final int MAX_MANA = 160;

	private int mana = 0;

	public TileSpawnerClaw(BlockPos pos, BlockState state) {
		super(ModTiles.SPAWNER_CLAW, pos, state);
	}

	@Override
	public void tick() {
		BlockEntity tileBelow = level.getBlockEntity(worldPosition.below());
		if (mana >= 5 && tileBelow instanceof SpawnerBlockEntity) {
			SpawnerBlockEntity spawner = (SpawnerBlockEntity) tileBelow;
			BaseSpawner logic = spawner.getSpawner();
			AccessorAbstractSpawner mLogic = (AccessorAbstractSpawner) logic;

			// [VanillaCopy] AbstractSpawner.tick, edits noted
			if (!mLogic.botania_isPlayerInRange()) { // Activate when vanilla is *not* running the spawner
				Level world = this.getLevel();
				BlockPos blockpos = logic.getPos();
				if (world.isClientSide) {
					// Botania - use own particles
					if (Math.random() > 0.5) {
						WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F, 0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, 2F);
						world.addParticle(data, getBlockPos().getX() + 0.3 + Math.random() * 0.5, getBlockPos().getY() - 0.3 + Math.random() * 0.25, getBlockPos().getZ() + Math.random(), 0, -(-0.025F - 0.005F * (float) Math.random()), 0);
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
						mLogic.botania_updateSpawns();
					}

					if (mLogic.getSpawnDelay() > 0) {
						mLogic.setSpawnDelay(mLogic.getSpawnDelay() - 1);
						return;
					}

					boolean flag = false;

					for (int i = 0; i < mLogic.getSpawnCount(); ++i) {
						CompoundTag compoundnbt = mLogic.getSpawnEntry().getTag();
						Optional<EntityType<?>> optional = EntityType.by(compoundnbt);
						if (!optional.isPresent()) {
							mLogic.botania_updateSpawns();
							return;
						}

						ListTag listnbt = compoundnbt.getList("Pos", 6);
						int j = listnbt.size();
						double d0 = j >= 1 ? listnbt.getDouble(0) : (double) blockpos.getX() + (world.random.nextDouble() - world.random.nextDouble()) * (double) mLogic.getSpawnRange() + 0.5D;
						double d1 = j >= 2 ? listnbt.getDouble(1) : (double) (blockpos.getY() + world.random.nextInt(3) - 1);
						double d2 = j >= 3 ? listnbt.getDouble(2) : (double) blockpos.getZ() + (world.random.nextDouble() - world.random.nextDouble()) * (double) mLogic.getSpawnRange() + 0.5D;
						if (world.noCollision(optional.get().getAABB(d0, d1, d2))) {
							ServerLevel serverWorld = (ServerLevel) world;
							Entity entity = EntityType.loadEntityRecursive(compoundnbt, world, (p_221408_6_) -> {
								p_221408_6_.moveTo(d0, d1, d2, p_221408_6_.yRot, p_221408_6_.xRot);
								return p_221408_6_;
							});
							if (entity == null) {
								mLogic.botania_updateSpawns();
								return;
							}

							int k = world.getEntitiesOfClass(entity.getClass(), (new AABB((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), (double) (blockpos.getX() + 1), (double) (blockpos.getY() + 1), (double) (blockpos.getZ() + 1))).inflate((double) mLogic.getSpawnRange())).size();
							if (k >= mLogic.getMaxNearbyEntities()) {
								mLogic.botania_updateSpawns();
								return;
							}

							entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), world.random.nextFloat() * 360.0F, 0.0F);
							if (entity instanceof Mob) {
								Mob mobentity = (Mob) entity;
								if (!mobentity.checkSpawnRules(world, MobSpawnType.SPAWNER) || !mobentity.checkSpawnObstruction(world)) {
									continue;
								}

								if (mLogic.getSpawnEntry().getTag().size() == 1 && mLogic.getSpawnEntry().getTag().contains("id", 8)) {
									((Mob) entity).finalizeSpawn(serverWorld, world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, (SpawnGroupData) null, (CompoundTag) null);
								}

								serverWorld.tryAddFreshEntityWithPassengers(entity);
								world.levelEvent(2004, blockpos, 0);
								if (entity instanceof Mob) {
									((Mob) entity).spawnAnim();
								}

								flag = true;
							}
						}
					}

					if (flag) {
						mLogic.botania_updateSpawns();
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
		return mana >= MAX_MANA;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(3 * MAX_MANA, this.mana + mana);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}

}
