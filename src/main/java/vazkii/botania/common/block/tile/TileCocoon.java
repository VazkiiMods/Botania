/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tag.Tag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.village.VillagerType;
import net.minecraft.world.ServerWorldAccess;
import vazkii.botania.common.lib.ModTags;

import java.util.ArrayList;
import java.util.List;

public class TileCocoon extends TileMod implements Tickable {
	private static final String TAG_TIME_PASSED = "timePassed";
	private static final String TAG_EMERALDS_GIVEN = "emeraldsGiven";
	private static final String TAG_CHORUS_FRUIT_GIVEN = "chorusFruitGiven";
	private static final String TAG_GAIA_SPIRIT_GIVEN = "gaiaSpiritGiven";

	public static final int TOTAL_TIME = 2400;
	public static final int MAX_EMERALDS = 20;
	public static final int MAX_CHORUS_FRUITS = 20;
	private static final float SPECIAL_CHANCE = 0.075F;

	public int timePassed;
	public int emeraldsGiven;
	public int chorusFruitGiven;
	public boolean gaiaSpiritGiven;

	public TileCocoon() {
		super(ModTiles.COCOON);
	}

	@Override
	public void tick() {
		timePassed++;
		if (timePassed >= TOTAL_TIME) {
			hatch();
		}
	}

	private void hatch() {
		if (!world.isClient) {
			timePassed = 0;
			world.breakBlock(pos, false);

			MobEntity entity = null;
			BlockPos placePos = pos;
			float rareChance = gaiaSpiritGiven ? 1F : SPECIAL_CHANCE;

			float villagerChance = Math.min(1F, (float) emeraldsGiven / (float) MAX_EMERALDS);
			float shulkerChance = Math.min(1F, (float) chorusFruitGiven / (float) MAX_CHORUS_FRUITS);

			List<BlockPos> validWater = new ArrayList<>();
			for (Direction d : Direction.values()) {
				BlockPos blockPos = (d == Direction.UP) ? pos : pos.offset(d);
				if (world.isChunkLoaded(blockPos)
						&& world.getBlockState(blockPos).getBlock() == Blocks.WATER) {
					validWater.add(blockPos);
				}
			}

			if (Math.random() < shulkerChance) {
				entity = EntityType.SHULKER.create(world);
			} else if (Math.random() < villagerChance) {
				VillagerEntity villager = EntityType.VILLAGER.create(world);
				if (villager != null) {
					VillagerType type = VillagerType.forBiome(world.method_31081(pos));
					villager.setVillagerData(villager.getVillagerData().withType(type));
				}
				entity = villager;
			} else if (!validWater.isEmpty()) {
				placePos = validWater.get(world.random.nextInt(validWater.size()));
				if (Math.random() < rareChance) {
					entity = random(ModTags.Entities.COCOON_RARE_AQUATIC);
				} else {
					entity = random(ModTags.Entities.COCOON_COMMON_AQUATIC);
				}
			} else {
				if (Math.random() < rareChance) {
					entity = random(ModTags.Entities.COCOON_RARE);
				} else {
					entity = random(ModTags.Entities.COCOON_COMMON);
				}
			}

			if (entity != null) {
				entity.updatePosition(placePos.getX() + 0.5, placePos.getY() + 0.5, placePos.getZ() + 0.5);
				if (entity instanceof PassiveEntity) {
					((PassiveEntity) entity).setBreedingAge(-24000);
				}
				entity.initialize((ServerWorldAccess) world, world.getLocalDifficulty(getPos()), SpawnReason.EVENT, null, null);
				entity.setPersistent();
				world.spawnEntity(entity);
				entity.playSpawnEffects();
			}
		}
	}

	public void forceRare() {
		gaiaSpiritGiven = true;
		timePassed = Math.max(timePassed, TOTAL_TIME / 2);
	}

	private MobEntity random(Tag<EntityType<?>> tag) {
		EntityType<?> type = tag.getRandom(world.random);
		if (type == EntityType.COW && world.random.nextFloat() < 0.01) {
			type = EntityType.MOOSHROOM;
		}
		Entity entity = type.create(world);
		return entity instanceof MobEntity ? (MobEntity) entity : null;
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_TIME_PASSED, timePassed);
		cmp.putInt(TAG_EMERALDS_GIVEN, emeraldsGiven);
		cmp.putInt(TAG_CHORUS_FRUIT_GIVEN, chorusFruitGiven);
		cmp.putBoolean(TAG_GAIA_SPIRIT_GIVEN, gaiaSpiritGiven);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		timePassed = cmp.getInt(TAG_TIME_PASSED);
		emeraldsGiven = cmp.getInt(TAG_EMERALDS_GIVEN);
		chorusFruitGiven = cmp.getInt(TAG_CHORUS_FRUIT_GIVEN);
		gaiaSpiritGiven = cmp.getBoolean(TAG_GAIA_SPIRIT_GIVEN);
	}
}
