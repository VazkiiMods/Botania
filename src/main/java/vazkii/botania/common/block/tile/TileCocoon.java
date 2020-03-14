/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 8, 2015, 4:32:34 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public class TileCocoon extends TileMod implements ITickableTileEntity{

	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.COCOON)
	public static TileEntityType<TileCocoon> TYPE;

	private static final String TAG_TIME_PASSED = "timePassed";
	private static final String TAG_EMERALDS_GIVEN = "emeraldsGiven";
	private static final String TAG_CHORUS_FRUIT_GIVEN = "chorusFruitGiven";
	private static final String TAG_GAIA_SPIRIT_GIVEN = "gaiaSpiritGiven"; 

	private static final List<EntityType<? extends MobEntity>> SPECIALS = ImmutableList.of(
			EntityType.HORSE, EntityType.DONKEY, EntityType.WOLF, EntityType.OCELOT,
			EntityType.CAT, EntityType.PARROT, EntityType.LLAMA, EntityType.FOX,
			EntityType.PANDA, EntityType.TURTLE
	);
	private static final List<EntityType<? extends MobEntity>> NORMALS = ImmutableList.of(
			EntityType.PIG, EntityType.COW, EntityType.CHICKEN, EntityType.RABBIT, EntityType.SHEEP
	);
	private static final List<EntityType<? extends MobEntity>> AQUATIC = ImmutableList.of(
			EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SQUID
	);

	public static final int TOTAL_TIME = 2400;
	public static final int MAX_EMERALDS = 20;
	public static final int MAX_CHORUS_FRUITS = 20;
	private static final float SPECIAL_CHANCE = 0.075F;

	public int timePassed;
	public int emeraldsGiven;
	public int chorusFruitGiven;
	public boolean gaiaSpiritGiven;

	public TileCocoon() {
		super(TYPE);
	}

	@Override
	public void tick() {
		timePassed++;
		if(timePassed >= TOTAL_TIME)
			hatch();
	}

	private void hatch() {
		if(!world.isRemote) {
			timePassed = 0;
			world.destroyBlock(pos, false);

			MobEntity entity = null;
			BlockPos placePos = pos;
			float rareChance = gaiaSpiritGiven ? 1F : SPECIAL_CHANCE;  

			float villagerChance = Math.min(1F, (float) emeraldsGiven / (float) MAX_EMERALDS);
			float shulkerChance = Math.min(1F, (float) chorusFruitGiven / (float) MAX_CHORUS_FRUITS);

			List<BlockPos> validWater = new ArrayList<>();
			for (Direction d : Direction.values()) {
				if (d != Direction.UP) {
					BlockPos blockPos = pos.offset(d);
					if (world.isBlockLoaded(blockPos)
							&& world.getBlockState(blockPos).getBlock() == Blocks.WATER) {
						validWater.add(blockPos);
					}
				}
			}

			if(Math.random() < shulkerChance) {
				entity = EntityType.SHULKER.create(world);
			} else if(Math.random() < villagerChance) {
				VillagerEntity villager = EntityType.VILLAGER.create(world);
				if(villager != null) {
					IVillagerType type = IVillagerType.byBiome(world.getBiome(pos));
					villager.setVillagerData(villager.getVillagerData().withType(type));
				}
				entity = villager;
			} else if (!validWater.isEmpty()) {
				placePos = validWater.get(world.rand.nextInt(validWater.size()));
				if (Math.random() < rareChance) {
					entity = EntityType.DOLPHIN.create(world);
				} else {
					entity = AQUATIC.get(world.rand.nextInt(AQUATIC.size())).create(world);
				}
			} else {
				if(Math.random() < rareChance) {
					entity = SPECIALS.get(world.rand.nextInt(SPECIALS.size())).create(world);
				} else {
					EntityType<? extends MobEntity> type = NORMALS.get(world.rand.nextInt(NORMALS.size()));
					if (type == EntityType.COW && Math.random() < 0.01)
						type = EntityType.MOOSHROOM;
					entity = type.create(world);
				}
			}

			if(entity != null) {
				entity.setPosition(placePos.getX() + 0.5, placePos.getY() + 0.5, placePos.getZ() + 0.5);
				if(entity instanceof AgeableEntity)
					((AgeableEntity) entity).setGrowingAge(-24000);
				entity.onInitialSpawn(world, world.getDifficultyForLocation(getPos()), SpawnReason.EVENT, null, null);
				entity.enablePersistence();
				world.addEntity(entity);
				entity.spawnExplosionParticle();
			}
		}
	}

	public void forceRare() {
		gaiaSpiritGiven = true;
		timePassed = Math.min(timePassed, TOTAL_TIME / 3);
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
