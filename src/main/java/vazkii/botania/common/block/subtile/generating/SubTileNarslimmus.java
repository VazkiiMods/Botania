/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkRandom;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.components.EntityComponents;

import java.util.List;

public class SubTileNarslimmus extends TileEntityGeneratingFlower {

	private static final int RANGE = 2;

	public SubTileNarslimmus() {
		super(ModSubtiles.NARSLIMMUS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (ticksExisted % 5 == 0) {
			List<SlimeEntity> slimes = getWorld().getNonSpectatingEntities(SlimeEntity.class, new Box(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (SlimeEntity slime : slimes) {
				if (slime.isAlive() && EntityComponents.NARSLIMMUS.get(slime).isNaturalSpawned()) {
					int size = slime.getSize();
					int mul = (int) Math.pow(2, size);
					int mana = 1200 * mul;
					if (!slime.world.isClient) {
						slime.remove();
						slime.playSound(size > 1 ? SoundEvents.ENTITY_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH_SMALL, 1, 0.02F);
						addMana(mana);
						sync();
					}

					for (int j = 0; j < mul * 8; ++j) {
						float f = slime.world.random.nextFloat() * (float) Math.PI * 2.0F;
						float f1 = slime.world.random.nextFloat() * 0.5F + 0.5F;
						float f2 = MathHelper.sin(f) * size * 0.5F * f1;
						float f3 = MathHelper.cos(f) * size * 0.5F * f1;
						float f4 = slime.world.random.nextFloat() * size * 0.5F * f1;
						slime.world.addParticle(ParticleTypes.ITEM_SLIME, slime.getX() + f2, slime.getBoundingBox().minY + f4, slime.getZ() + f3, 0.0D, 0.0D, 0.0D);
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

	public static void onSpawn(Entity entity) {
		boolean slimeChunk = isSlimeChunk(entity.world, entity.getX(), entity.getZ());
		if (slimeChunk) {
			entity.streamPassengersRecursively().forEach(e -> {
				if (e instanceof SlimeEntity) {
					EntityComponents.NARSLIMMUS.get(e).setNaturalSpawn(true);
				}
			});
		}
	}

	private static boolean isSlimeChunk(World world, double x, double z) {
		return isSlimeChunk(world, new BlockPos(x, 0, z));
	}

	public static boolean isSlimeChunk(World world, BlockPos pos) {
		ChunkPos chunkpos = new ChunkPos(pos);
		return ChunkRandom.getSlimeRandom(chunkpos.x, chunkpos.z, ((ServerWorld) world).getSeed(), 987234911L).nextInt(10) == 0;
	}

}
