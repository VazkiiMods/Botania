/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 29, 2015, 10:43:54 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.lexicon.LexiconData;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SubTileNarslimmus extends SubTileGenerating {

	public static final String TAG_WORLD_SPAWNED = "Botania:WorldSpawned";

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(ticksExisted % 5 == 0) {
			float range = 2F;
			List<EntitySlime> slimes = supertile.getWorldObj().getEntitiesWithinAABB(EntitySlime.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord - range, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + range, supertile.zCoord + range));
			for(EntitySlime slime : slimes) {
				if(slime.getEntityData().getBoolean(TAG_WORLD_SPAWNED)) {
					int size = slime.getSlimeSize();
					int mul = (int) Math.pow(2, size);
					int mana = 160 * mul;
					int remain = getMaxMana() - this.mana;
					if(!slime.worldObj.isRemote) {
						slime.setDead();
						slime.worldObj.playSoundAtEntity(slime, "mob.slime." + (size > 1 ? "big" : "small"), 1F, 0.02F);
						this.mana = Math.min(getMaxMana(), this.mana + mana);
						sync();
					}

					for (int j = 0; j < mul * 8; ++j) {
						float f = slime.worldObj.rand.nextFloat() * (float)Math.PI * 2.0F;
						float f1 = slime.worldObj.rand.nextFloat() * 0.5F + 0.5F;
						float f2 = MathHelper.sin(f) * (float) size * 0.5F * f1;
						float f3 = MathHelper.cos(f) * (float) size * 0.5F * f1;
						float f4 = slime.worldObj.rand.nextFloat() * (float) size * 0.5F * f1;
						slime.worldObj.spawnParticle("slime", slime.posX + (double)f2, slime.boundingBox.minY + f4, slime.posZ + (double)f3, 0.0D, 0.0D, 0.0D);
					}
					break;
				}
			}
		}
	}

	@Override
	public int getMaxMana() {
		return 5000;
	}

	@Override
	public int getColor() {
		return 0x71C373;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.narslimmus;
	}
	
	public static class SpawnIntercepter {

		@SubscribeEvent
		public void onSpawn(LivingSpawnEvent.CheckSpawn event) {
			if(event.entityLiving instanceof EntitySlime && event.getResult() != Result.DENY && isSlimeChunk(event.entityLiving.worldObj, MathHelper.floor_double(event.x), MathHelper.floor_double(event.z)))
				event.entityLiving.getEntityData().setBoolean(TAG_WORLD_SPAWNED, true);
		}

		public boolean isSlimeChunk(World world, int x, int z) {
			Chunk chunk = world.getChunkFromBlockCoords(x, z);
			return chunk.getRandomWithSeed(987234911L).nextInt(10) == 0;
		}

	}

}
