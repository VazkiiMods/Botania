/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModSubtiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubTileHydroangeas extends TileEntityGeneratingFlower {
	private static final String TAG_BURN_TIME = "burnTime";
	public static final String TAG_COOLDOWN = "cooldown";

	private static final BlockPos[] OFFSETS = { new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(-1, 0, 1), new BlockPos(-1, 0, -1), new BlockPos(1, 0, 1), new BlockPos(1, 0, -1) };

	int burnTime, cooldown;

	public SubTileHydroangeas(BlockPos pos, BlockState state) {
		this(ModSubtiles.HYDROANGEAS, pos, state);
	}

	protected SubTileHydroangeas(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (cooldown > 0) {
			cooldown--;
			for (int i = 0; i < 3; i++) {
				WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.1F, 0.1F, 0.1F, 1);
				level.addParticle(data, getEffectivePos().getX() + 0.5 + Math.random() * 0.2 - 0.1, getEffectivePos().getY() + 0.5 + Math.random() * 0.2 - 0.1, getEffectivePos().getZ() + 0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 30, 0);
			}
		}

		if (burnTime == 0) {
			if (getMana() < getMaxMana() && !getLevel().isClientSide) {
				List<BlockPos> offsets = Arrays.asList(OFFSETS);
				Collections.shuffle(offsets);

				for (BlockPos offset : offsets) {
					BlockPos pos = getEffectivePos().offset(offset);

					BlockState bstate = getLevel().getBlockState(pos);
					FluidState fstate = getLevel().getFluidState(pos);
					Tag<Fluid> search = getMaterialToSearchFor();
					if (fstate.is(search) && fstate.isSource()) {
						if (search != FluidTags.WATER) {
							getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
						} else {
							int waterAround = 0;
							for (Direction dir : Direction.values()) {
								if (getLevel().getFluidState(pos.relative(dir)).is(search)) {
									waterAround++;
								}
							}

							if (waterAround < 2) {
								if (bstate.getBlock() instanceof BucketPickup) {
									((BucketPickup) bstate.getBlock()).takeLiquid(getLevel(), pos, bstate);
								} else {
									getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
								}
							}
						}

						if (cooldown == 0) {
							burnTime += getBurnTime();
						} else {
							cooldown = getCooldown();
						}

						sync();
						playSound();
						break;
					}
				}
			}
		} else {
			if (getLevel().random.nextInt(8) == 0) {
				doBurnParticles();
			}
			burnTime--;
			if (burnTime == 0) {
				cooldown = getCooldown();
				sync();
			}
		}
	}

	public void doBurnParticles() {
		WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.05F, 0.05F, 0.7F, 1);
		level.addParticle(data, getEffectivePos().getX() + 0.55 + Math.random() * 0.2 - 0.1, getEffectivePos().getY() + 0.55 + Math.random() * 0.2 - 0.1, getEffectivePos().getZ() + 0.5, 0, (float) Math.random() / 60, 0);
	}

	public Tag<Fluid> getMaterialToSearchFor() {
		return FluidTags.WATER;
	}

	public void playSound() {
		getLevel().playSound(null, getEffectivePos(), SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS, 0.01F, 0.5F + (float) Math.random() * 0.5F);
	}

	public int getBurnTime() {
		return 40;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), 1);
	}

	@Override
	public int getMaxMana() {
		return 150;
	}

	@Override
	public int getColor() {
		return 0x532FE0;
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_BURN_TIME, burnTime);
		cmp.putInt(TAG_COOLDOWN, cooldown);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);

		burnTime = cmp.getInt(TAG_BURN_TIME);
		cooldown = cmp.getInt(TAG_COOLDOWN);
	}

	@Override
	public boolean canGeneratePassively() {
		return burnTime > 0;
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		boolean rain = getLevel().getBiome(getEffectivePos()).getPrecipitation() == Biome.Precipitation.RAIN && (getLevel().isRaining() || getLevel().isThundering());
		return rain ? 2 : 3;
	}

	public int getCooldown() {
		return 0;
	}

	@Override
	public boolean isPassiveFlower() {
		return true;
	}

}
