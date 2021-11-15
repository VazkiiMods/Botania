/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;

public class SubTileNarslimmus extends TileEntityGeneratingFlower {
	public static final String TAG_WORLD_SPAWNED = "botania:world_spawned";

	private static final int RANGE = 2;
	private static final int MAX_MANA = manaForSize(4);

	public SubTileNarslimmus() {
		super(ModSubtiles.NARSLIMMUS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (ticksExisted % 5 == 0) {
			List<SlimeEntity> slimes = getWorld().getEntitiesWithinAABB(SlimeEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (SlimeEntity slime : slimes) {
				if (slime.getPersistentData().getBoolean(TAG_WORLD_SPAWNED) && slime.isAlive()) {
					int size = slime.getSlimeSize();
					if (!slime.world.isRemote) {
						slime.remove();
						slime.playSound(size > 1 ? SoundEvents.ENTITY_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH_SMALL, 1, 0.02F);
						addMana(manaForSize(size));
						sync();
					}

					int times = 8 * (int) Math.pow(2, size);
					for (int j = 0; j < times; ++j) {
						float f = slime.world.rand.nextFloat() * (float) Math.PI * 2.0F;
						float f1 = slime.world.rand.nextFloat() * 0.5F + 0.5F;
						float f2 = MathHelper.sin(f) * size * 0.5F * f1;
						float f3 = MathHelper.cos(f) * size * 0.5F * f1;
						float f4 = slime.world.rand.nextFloat() * size * 0.5F * f1;
						slime.world.addParticle(ParticleTypes.ITEM_SLIME, slime.getPosX() + f2, slime.getBoundingBox().minY + f4, slime.getPosZ() + f3, 0.0D, 0.0D, 0.0D);
					}
					break;
				}
			}
		}
	}

	private static int manaForSize(int size) {
		size = Math.min(size, 4);
		return 1200 * (int) Math.pow(2, size);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return MAX_MANA;
	}

	@Override
	public int getColor() {
		return 0x71C373;
	}

	public static void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		if (event.getEntityLiving() instanceof SlimeEntity
				&& event.getSpawnReason() == SpawnReason.NATURAL
				&& event.getResult() != Event.Result.DENY
				&& isSlimeChunk(event.getEntityLiving().world, event.getX(), event.getZ())) {
			event.getEntityLiving().getPersistentData().putBoolean(TAG_WORLD_SPAWNED, true);
		}
	}

	private static boolean isSlimeChunk(World world, double x, double z) {
		return isSlimeChunk(world, new BlockPos(x, 0, z));
	}

	public static boolean isSlimeChunk(World world, BlockPos pos) {
		ChunkPos chunkpos = new ChunkPos(pos);
		return SharedSeedRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((ServerWorld) world).getSeed(), 987234911L).nextInt(10) == 0;
	}

}
