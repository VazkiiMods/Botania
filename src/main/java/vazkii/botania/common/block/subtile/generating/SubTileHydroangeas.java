/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

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

	public SubTileHydroangeas() {
		this(ModSubtiles.HYDROANGEAS);
	}

	public SubTileHydroangeas(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (cooldown > 0) {
			cooldown--;
			for (int i = 0; i < 3; i++) {
				WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.1F, 0.1F, 0.1F, 1);
				world.addParticle(data, getEffectivePos().getX() + 0.5 + Math.random() * 0.2 - 0.1, getEffectivePos().getY() + 0.5 + Math.random() * 0.2 - 0.1, getEffectivePos().getZ() + 0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 30, 0);
			}
		}

		if (burnTime == 0) {
			if (getMana() < getMaxMana() && !getWorld().isRemote) {
				List<BlockPos> offsets = Arrays.asList(OFFSETS);
				Collections.shuffle(offsets);

				for (BlockPos offset : offsets) {
					BlockPos pos = getEffectivePos().add(offset);

					FluidState fstate = getWorld().getFluidState(pos);
					ITag<Fluid> search = getMaterialToSearchFor();
					if (fstate.isTagged(search) && fstate.isSource()) {
						if (search != FluidTags.WATER) {
							getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
						} else {
							int waterAround = 0;
							for (Direction dir : Direction.values()) {
								if (getWorld().getFluidState(pos.offset(dir)).isTagged(search)) {
									waterAround++;
								}
							}

							if (waterAround < 2) {
								getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
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
			if (getWorld().rand.nextInt(8) == 0) {
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
		world.addParticle(data, getEffectivePos().getX() + 0.55 + Math.random() * 0.2 - 0.1, getEffectivePos().getY() + 0.55 + Math.random() * 0.2 - 0.1, getEffectivePos().getZ() + 0.5, 0, (float) Math.random() / 60, 0);
	}

	public ITag<Fluid> getMaterialToSearchFor() {
		return FluidTags.WATER;
	}

	public void playSound() {
		getWorld().playSound(null, getEffectivePos(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.01F, 0.5F + (float) Math.random() * 0.5F);
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

	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_BURN_TIME, burnTime);
		cmp.putInt(TAG_COOLDOWN, cooldown);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
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
		boolean rain = getWorld().getBiome(getEffectivePos()).getPrecipitation() == Biome.RainType.RAIN && (getWorld().isRaining() || getWorld().isThundering());
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
