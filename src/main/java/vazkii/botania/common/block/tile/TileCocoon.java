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
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

import vazkii.botania.common.lib.ModTags;

import java.util.ArrayList;
import java.util.List;

public class TileCocoon extends TileMod implements TickableBlockEntity {
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
		if (!level.isClientSide) {
			timePassed = 0;
			level.destroyBlock(worldPosition, false);

			Mob entity = null;
			BlockPos placePos = worldPosition;
			float rareChance = gaiaSpiritGiven ? 1F : SPECIAL_CHANCE;

			float villagerChance = Math.min(1F, (float) emeraldsGiven / (float) MAX_EMERALDS);
			float shulkerChance = Math.min(1F, (float) chorusFruitGiven / (float) MAX_CHORUS_FRUITS);

			List<BlockPos> validWater = new ArrayList<>();
			for (Direction d : Direction.values()) {
				BlockPos blockPos = (d == Direction.UP) ? worldPosition : worldPosition.relative(d);
				if (level.hasChunkAt(blockPos)
						&& level.getBlockState(blockPos).getBlock() == Blocks.WATER) {
					validWater.add(blockPos);
				}
			}

			if (Math.random() < shulkerChance) {
				entity = EntityType.SHULKER.create(level);
			} else if (Math.random() < villagerChance) {
				Villager villager = EntityType.VILLAGER.create(level);
				if (villager != null) {
					VillagerType type = VillagerType.byBiome(level.getBiomeName(worldPosition));
					villager.setVillagerData(villager.getVillagerData().setType(type));
				}
				entity = villager;
			} else if (!validWater.isEmpty()) {
				placePos = validWater.get(level.random.nextInt(validWater.size()));
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
				entity.setPos(placePos.getX() + 0.5, placePos.getY() + 0.5, placePos.getZ() + 0.5);
				if (entity instanceof AgableMob) {
					((AgableMob) entity).setAge(-24000);
				}
				entity.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(getBlockPos()), MobSpawnType.EVENT, null, null);
				entity.setPersistenceRequired();
				level.addFreshEntity(entity);
				entity.spawnAnim();
			}
		}
	}

	public void forceRare() {
		gaiaSpiritGiven = true;
		timePassed = Math.max(timePassed, TOTAL_TIME / 2);
	}

	private Mob random(Tag<EntityType<?>> tag) {
		EntityType<?> type = tag.getRandomElement(level.random);
		if (type == EntityType.COW && level.random.nextFloat() < 0.01) {
			type = EntityType.MOOSHROOM;
		}
		Entity entity = type.create(level);
		return entity instanceof Mob ? (Mob) entity : null;
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
