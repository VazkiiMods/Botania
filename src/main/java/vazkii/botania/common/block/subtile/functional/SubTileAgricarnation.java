/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;

import java.util.Set;

public class SubTileAgricarnation extends TileEntityFunctionalFlower {
	private static final Set<Material> MATERIALS = ImmutableSet.of(Material.PLANT, Material.CACTUS, Material.SOLID_ORGANIC,
			Material.LEAVES, Material.GOURD, Material.UNDERWATER_PLANT, Material.BAMBOO, Material.BAMBOO_SAPLING);
	private static final int RANGE = 5;
	private static final int RANGE_MINI = 2;

	protected SubTileAgricarnation(BlockEntityType<?> type) {
		super(type);
	}

	public SubTileAgricarnation() {
		this(ModSubtiles.AGRICARNATION);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient) {
			return;
		}

		if (ticksExisted % 200 == 0) {
			sync();
		}

		if (ticksExisted % 6 == 0 && redstoneSignal == 0) {
			int range = getRange();
			int x = getEffectivePos().getX() + getWorld().random.nextInt(range * 2 + 1) - range;
			int z = getEffectivePos().getZ() + getWorld().random.nextInt(range * 2 + 1) - range;

			for (int i = 4; i > -2; i--) {
				int y = getEffectivePos().getY() + i;
				BlockPos pos = new BlockPos(x, y, z);
				if (getWorld().isAir(pos)) {
					continue;
				}

				if (isPlant(pos) && getMana() > 5) {
					BlockState state = getWorld().getBlockState(pos);
					addMana(-5);
					state.randomTick((ServerWorld) world, pos, world.random);
					if (ConfigHandler.COMMON.blockBreakParticles.getValue()) {
						getWorld().syncWorldEvent(2005, pos, 6 + getWorld().random.nextInt(4));
					}
					getWorld().playSound(null, x, y, z, ModSounds.agricarnation, SoundCategory.BLOCKS, 0.01F, 0.5F + (float) Math.random() * 0.5F);

					break;
				}
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	/**
	 * @return Whether the block at {@code pos} grows "naturally". That is, whether its IGrowable action is simply
	 *         growing itself, instead of something like spreading around or creating flowers around, etc, and whether
	 *         this
	 *         action would have happened normally over time without bonemeal.
	 */
	private boolean isPlant(BlockPos pos) {
		BlockState state = getWorld().getBlockState(pos);
		Block block = state.getBlock();

		// Spreads when ticked
		if (block instanceof SpreadableBlock) {
			return false;
		}

		// Exclude all BushBlock except known vanilla subclasses
		if (block instanceof PlantBlock && !(block instanceof CropBlock) && !(block instanceof StemBlock)
				&& !(block instanceof SaplingBlock) && !(block instanceof SweetBerryBushBlock)) {
			return false;
		}

		return MATERIALS.contains(state.getMaterial())
				&& block instanceof Fertilizable
				&& ((Fertilizable) block).isFertilizable(getWorld(), pos, state, getWorld().isClient);
	}

	@Override
	public int getColor() {
		return 0x8EF828;
	}

	@Override
	public int getMaxMana() {
		return 200;
	}

	public int getRange() {
		return RANGE;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), getRange());
	}

	public static class Mini extends SubTileAgricarnation {
		public Mini() {
			super(ModSubtiles.AGRICARNATION_CHIBI);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}
	}

}
