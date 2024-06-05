/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.Arrays;

public class ThermalilyBlockEntity extends FluidGeneratorBlockEntity {
	public static final int COOLDOWN_TICKS_MULTIPLER = 400;
	public static final String TAG_COOLDOWN_MAGNITUDE = "cooldownStrength";
	public static final int FAST_PROVIDE_TICKS = 200;

	private int cooldownStrength = 15;
	public static final int[] COOLDOWN_ROLL_PDF = { 10, 5, 3, 2, 1, 1, 3, 3, 3, 2, 1, 1, 1, 2, 2 };
	private int ticksSinceFueled = 0;
	private int heat;

	public ThermalilyBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.THERMALILY, pos, state, FluidTags.LAVA, 600, 45);
	}

	@Override
	public int getCooldownTime(boolean finishedPrevious) {
		if (finishedPrevious) {
			cooldownStrength = rollNewCooldownStrength(getLevel().getRandom(), heat);
		}
		return COOLDOWN_TICKS_MULTIPLER * cooldownStrength;
	}

	public static int rollNewCooldownStrength(RandomSource random, int bias) {
		int[] weights = weightCooldown(COOLDOWN_ROLL_PDF, bias);
		var total = random.nextInt(Arrays.stream(weights).sum());
		var index = 0;
		while (total >= weights[index]) {
			total -= weights[index];
			index++;
		}
		return index + 1;
	}

	public static int[] weightCooldown(int[] originalWeights, int amount) {
		int[] result = new int[originalWeights.length];
		for (int i = 0; i < originalWeights.length; i++) {
			result[i] = (int) Math.max(Math.ceil(originalWeights[i] * (12 - amount - Math.sqrt(originalWeights[i]))), 0);
		}
		return result;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();
		if (burnTime == 0 && cooldown == 0) {
			ticksSinceFueled++;
		} else if (burnTime == startBurnTime) {
			if (ticksSinceFueled <= FAST_PROVIDE_TICKS) {
				heat = heat < 10 ? heat + 1 : heat;
			} else {
				heat = 0;
			}
			ticksSinceFueled = 0;
		}
	}

	@Override
	public int getColor() {
		return 0xD03C00;
	}

	@Override
	public void doBurnParticles() {
		WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.7F, 0.05F, 0.05F, 1);
		emitParticle(data, 0.5 + Math.random() * 0.2 - 0.1, 0.9 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 60, 0);
	}

	@Override
	public void playSound() {
		getLevel().playSound(null, getEffectivePos(), BotaniaSounds.thermalily, SoundSource.BLOCKS, 1F, 1F);
	}

	@Override
	public int getComparatorSignal() {
		return burnTime > 0 ? 0 : cooldownStrength;
	}

	@Override
	public int getMaxMana() {
		return 750;
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_COOLDOWN_MAGNITUDE, cooldownStrength);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);

		cooldownStrength = cmp.getInt(TAG_COOLDOWN_MAGNITUDE);
	}
}
