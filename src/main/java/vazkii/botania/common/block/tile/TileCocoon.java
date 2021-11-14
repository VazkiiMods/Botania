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
import net.minecraft.entity.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.lib.ModTags;

import java.util.ArrayList;
import java.util.List;

public class TileCocoon extends TileMod implements ITickableTileEntity {
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
		if (!world.isRemote) {
			timePassed = 0;
			world.destroyBlock(pos, false);

			MobEntity entity = null;
			BlockPos placePos = pos;
			float rareChance = gaiaSpiritGiven ? 1F : SPECIAL_CHANCE;

			float villagerChance = Math.min(1F, (float) emeraldsGiven / (float) MAX_EMERALDS);
			float shulkerChance = Math.min(1F, (float) chorusFruitGiven / (float) MAX_CHORUS_FRUITS);

			List<BlockPos> validWater = new ArrayList<>();
			for (Direction d : Direction.values()) {
				BlockPos blockPos = (d == Direction.UP) ? pos : pos.offset(d);
				if (world.isBlockLoaded(blockPos)
						&& world.getBlockState(blockPos).getBlock() == Blocks.WATER) {
					validWater.add(blockPos);
				}
			}

			if (Math.random() < shulkerChance) {
				entity = EntityType.SHULKER.create(world);
			} else if (Math.random() < villagerChance) {
				VillagerEntity villager = EntityType.VILLAGER.create(world);
				if (villager != null) {
					VillagerType type = VillagerType.func_242371_a(world.func_242406_i(this.getPos()));
					villager.setVillagerData(villager.getVillagerData().withType(type));
				}
				entity = villager;
			} else if (!validWater.isEmpty()) {
				placePos = validWater.get(world.rand.nextInt(validWater.size()));
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
				if (world.rand.nextFloat() < 0.01) {
					// gonna make modded minecraft items into a gacha game
					// and somehow find a way to add jeanne d'arc to it
					// - Vazkii 2021
					StringTextComponent name = new StringTextComponent("Jeanne d'");
					name.append(entity.getName());
					name.append(new StringTextComponent(" [SSR]"));
					entity.setCustomName(name.mergeStyle(TextFormatting.GOLD));
					entity.setCustomNameVisible(true);
				}

				entity.setPosition(placePos.getX() + 0.5, placePos.getY() + 0.5, placePos.getZ() + 0.5);
				if (entity instanceof AgeableEntity) {
					((AgeableEntity) entity).setGrowingAge(-24000);
				}
				entity.onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(getPos()), SpawnReason.EVENT, null, null);
				entity.enablePersistence();
				world.addEntity(entity);
				entity.spawnExplosionParticle();
			}
		}
	}

	public void forceRare() {
		gaiaSpiritGiven = true;
		timePassed = Math.max(timePassed, TOTAL_TIME / 2);
	}

	private MobEntity random(ITag<EntityType<?>> tag) {
		EntityType<?> type = tag.getRandomElement(world.rand);
		if (type == EntityType.COW && world.rand.nextFloat() < 0.01) {
			type = EntityType.MOOSHROOM;
		}
		Entity entity = type.create(world);
		return entity instanceof MobEntity ? (MobEntity) entity : null;
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_TIME_PASSED, timePassed);
		cmp.putInt(TAG_EMERALDS_GIVEN, emeraldsGiven);
		cmp.putInt(TAG_CHORUS_FRUIT_GIVEN, chorusFruitGiven);
		cmp.putBoolean(TAG_GAIA_SPIRIT_GIVEN, gaiaSpiritGiven);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		timePassed = cmp.getInt(TAG_TIME_PASSED);
		emeraldsGiven = cmp.getInt(TAG_EMERALDS_GIVEN);
		chorusFruitGiven = cmp.getInt(TAG_CHORUS_FRUIT_GIVEN);
		gaiaSpiritGiven = cmp.getBoolean(TAG_GAIA_SPIRIT_GIVEN);
	}
}
