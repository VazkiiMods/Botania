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
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class TileCocoon extends TileMod implements ITickableTileEntity{

	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.COCOON)
	public static TileEntityType<TileCocoon> TYPE;

	private static final String TAG_TIME_PASSED = "timePassed";
	private static final String TAG_EMERALDS_GIVEN = "emeraldsGiven";
	private static final String TAG_CHORUS_FRUIT_GIVEN = "chorusFruitGiven";
	private static final List<EntityType<?>> SPECIALS = ImmutableList.of(
			EntityType.HORSE, EntityType.MULE, EntityType.DONKEY,
			EntityType.WOLF, EntityType.OCELOT, EntityType.CAT,
			EntityType.PARROT, EntityType.LLAMA, EntityType.FOX, EntityType.PANDA,
			EntityType.TURTLE
	);
	private static final List<EntityType<?>> NORMALS = ImmutableList.of(
			EntityType.PIG, EntityType.COW, EntityType.CHICKEN, EntityType.RABBIT,
			EntityType.SHEEP, EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH
	);

	public static final int TOTAL_TIME = 2400;
	public static final int MAX_EMERALDS = 20;
	public static final int MAX_CHORUS_FRUITS = 20;

	public int timePassed;
	public int emeraldsGiven;
	public int chorusFruitGiven;

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

			float villagerChance = Math.min(1F, (float) emeraldsGiven / (float) MAX_EMERALDS);
			float shulkerChance = Math.min(1F, (float) chorusFruitGiven / (float) MAX_CHORUS_FRUITS);

			if(Math.random() < shulkerChance) {
				entity = EntityType.SHULKER.create(world);
			} else if(Math.random() < villagerChance) {
				VillagerEntity villager = EntityType.VILLAGER.create(world);
				if(villager != null) {
					IVillagerType type = IVillagerType.byBiome(world.getBiome(pos));
					villager.setVillagerData(villager.getVillagerData().withType(type));
				}
				entity = villager;
			} else {
				float specialChance = 0.075F;
				if(Math.random() < specialChance) {
					entity = (MobEntity) SPECIALS.get(world.rand.nextInt(SPECIALS.size())).create(world);
				} else {
					EntityType<?> type = NORMALS.get(world.rand.nextInt(NORMALS.size()));
					if (type == EntityType.COW && Math.random() < 0.01)
						type = EntityType.MOOSHROOM;
					entity = (MobEntity) type.create(world);
				}
			}

			if(entity != null) {
				entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				if(entity instanceof AgeableEntity)
					((AgeableEntity) entity).setGrowingAge(-24000);
				entity.onInitialSpawn(world, world.getDifficultyForLocation(getPos()), SpawnReason.EVENT, null, null);
				world.addEntity(entity);
				entity.spawnExplosionParticle();
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_TIME_PASSED, timePassed);
		cmp.putInt(TAG_EMERALDS_GIVEN, emeraldsGiven);
		cmp.putInt(TAG_CHORUS_FRUIT_GIVEN, chorusFruitGiven);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		timePassed = cmp.getInt(TAG_TIME_PASSED);
		emeraldsGiven = cmp.getInt(TAG_EMERALDS_GIVEN);
		chorusFruitGiven = cmp.getInt(TAG_CHORUS_FRUIT_GIVEN);
	}

}
