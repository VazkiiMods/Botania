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

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileCocoon extends TileMod implements ITickableTileEntity{

	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.COCOON)
	public static TileEntityType<TileCocoon> TYPE;

	private static final String TAG_TIME_PASSED = "timePassed";
	private static final String TAG_EMERALDS_GIVEN = "emeraldsGiven";
	private static final String TAG_CHORUS_FRUIT_GIVEN = "chorusFruitGiven";

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
				entity = new ShulkerEntity(world);
			} else if(Math.random() < villagerChance) {
				VillagerEntity villager = new VillagerEntity(world);
				VillagerRegistry.setRandomProfession(villager, world.rand);
				entity = villager;
			} else {
				float specialChance = 0.05F;
				if(Math.random() < specialChance) {
					int entityType = world.rand.nextInt(5);
					switch(entityType) {
					case 0:
						entity = new HorseEntity(world);
						break;
					case 1:
						entity = new WolfEntity(world);
						break;
					case 2:
						entity = new OcelotEntity(world);
						break;
					case 3:
						entity = new ParrotEntity(world);
						break;
					case 4:
						entity = new LlamaEntity(world);
						break;
					}
				} else {
					int entityType = world.rand.nextInt(5);
					switch(entityType) {
					case 0:
						entity = new SheepEntity(world);
						break;
					case 1:
						if(Math.random() < 0.01)
							entity = new MooshroomEntity(world);
						else entity = new CowEntity(world);
						break;
					case 2:
						entity = new PigEntity(world);
						break;
					case 3:
						entity = new ChickenEntity(world);
						break;
					case 4:
						entity = new RabbitEntity(world);
						break;
					}
				}
			}

			if(entity != null) {
				entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				if(entity instanceof AgeableEntity)
					((AgeableEntity) entity).setGrowingAge(-24000);
				entity.onInitialSpawn(world.getDifficultyForLocation(getPos()), null, null);
				world.spawnEntity(entity);
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
