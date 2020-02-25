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

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class SubTileNarslimmus extends TileEntityGeneratingFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":narslimmus")
	public static TileEntityType<SubTileNarslimmus> TYPE;

	public static final String TAG_WORLD_SPAWNED = "botania:world_spawned";

	private static final int RANGE = 2;

	public SubTileNarslimmus() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(ticksExisted % 5 == 0) {
			List<SlimeEntity> slimes = getWorld().getEntitiesWithinAABB(SlimeEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for(SlimeEntity slime : slimes) {
				if(slime.getPersistentData().getBoolean(TAG_WORLD_SPAWNED) && slime.isAlive()) {
					int size = slime.getSlimeSize();
					int mul = (int) Math.pow(2, size);
					int mana = 1200 * mul;
					if(!slime.world.isRemote) {
						slime.remove();
						slime.playSound(size > 1 ? SoundEvents.ENTITY_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH_SMALL, 1, 0.02F);
						addMana(mana);
						sync();
					}

					for (int j = 0; j < mul * 8; ++j) {
						float f = slime.world.rand.nextFloat() * (float)Math.PI * 2.0F;
						float f1 = slime.world.rand.nextFloat() * 0.5F + 0.5F;
						float f2 = MathHelper.sin(f) * size * 0.5F * f1;
						float f3 = MathHelper.cos(f) * size * 0.5F * f1;
						float f4 = slime.world.rand.nextFloat() * size * 0.5F * f1;
						slime.world.addParticle(ParticleTypes.ITEM_SLIME, slime.posX + f2, slime.getBoundingBox().minY + f4, slime.posZ + f3, 0.0D, 0.0D, 0.0D);
					}
					break;
				}
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 12000;
	}

	@Override
	public int getColor() {
		return 0x71C373;
	}

	@SubscribeEvent
	public static void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		if(event.getEntityLiving() instanceof SlimeEntity
				&& event.getSpawnReason() == SpawnReason.NATURAL
				&& event.getResult() != Event.Result.DENY
				&& isSlimeChunk(event.getEntityLiving().world, event.getX(), event.getZ()))
			event.getEntityLiving().getPersistentData().putBoolean(TAG_WORLD_SPAWNED, true);
	}

	private static boolean isSlimeChunk(World world, double x, double z) {
		return isSlimeChunk(world, new BlockPos(x, 0, z));
	}

	public static boolean isSlimeChunk(World world, BlockPos pos) {
		ChunkPos chunkpos = new ChunkPos(pos);
		return SharedSeedRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, world.getSeed(), 987234911L).nextInt(10) == 0;
	}

}
