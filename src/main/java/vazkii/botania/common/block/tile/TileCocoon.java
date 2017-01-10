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

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class TileCocoon extends TileMod {

	private static final String TAG_TIME_PASSED = "timePassed";
	private static final String TAG_EMERALDS_GIVEN = "emeraldsGiven";

	public static final int TOTAL_TIME = 2400;
	public static final int MAX_EMERALDS = 20;

	public int timePassed;
	public int emeraldsGiven;

	@Override
	public void update() {
		timePassed++;
		if(timePassed >= TOTAL_TIME)
			hatch();
	}

	private void hatch() {
		if(!world.isRemote) {
			world.playEvent(2001, pos, Block.getStateId(getBlockType().getStateFromMeta(getBlockMetadata())));
			world.setBlockToAir(pos);

			EntityAgeable entity = null;

			float villagerChance = Math.min(1F, (float) emeraldsGiven / (float) MAX_EMERALDS);

			if(Math.random() < villagerChance) {
				EntityVillager villager = new EntityVillager(world);
				VillagerRegistry.setRandomProfession(villager, world.rand);
				entity = villager;
			} else {
				float specialChance = 0.05F;
				if(Math.random() < specialChance) {
					int entityType = world.rand.nextInt(3);
					switch(entityType) {
					case 0:
						entity = new EntityHorse(world);
						break;
					case 1:
						entity = new EntityWolf(world);
						break;
					case 2:
						entity = new EntityOcelot(world);
						break;
					}
				} else {
					int entityType = world.rand.nextInt(5);
					switch(entityType) {
					case 0:
						entity = new EntitySheep(world);
						break;
					case 1:
						if(Math.random() < 0.01)
							entity = new EntityMooshroom(world);
						else entity = new EntityCow(world);
						break;
					case 2:
						entity = new EntityPig(world);
						break;
					case 3:
						entity = new EntityChicken(world);
						break;
					case 4:
						entity = new EntityRabbit(world);
						break;
					}
				}
			}

			if(entity != null) {
				entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				entity.setGrowingAge(-24000);
				world.spawnEntity(entity);
				entity.spawnExplosionParticle();
			}
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_TIME_PASSED, timePassed);
		cmp.setInteger(TAG_EMERALDS_GIVEN, emeraldsGiven);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		timePassed = cmp.getInteger(TAG_TIME_PASSED);
		emeraldsGiven = cmp.getInteger(TAG_EMERALDS_GIVEN);
	}

}
