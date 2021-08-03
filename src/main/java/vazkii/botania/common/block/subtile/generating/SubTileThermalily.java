/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ModSounds;

public class SubTileThermalily extends SubTileHydroangeas {
	public SubTileThermalily() {
		super(ModSubtiles.THERMALILY);
	}

	@Override
	public int getColor() {
		return 0xD03C00;
	}

	@Override
	public void doBurnParticles() {
		WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.7F, 0.05F, 0.05F, 1);
		level.addParticle(data, getEffectivePos().getX() + 0.55 + Math.random() * 0.2 - 0.1, getEffectivePos().getY() + 0.9 + Math.random() * 0.2 - 0.1, getEffectivePos().getZ() + 0.5, 0, (float) Math.random() / 60, 0);
	}

	@Override
	public Tag<Fluid> getMaterialToSearchFor() {
		return FluidTags.LAVA;
	}

	@Override
	public void playSound() {
		getLevel().playSound(null, getEffectivePos(), ModSounds.thermalily, SoundSource.BLOCKS, 0.2F, 1F);
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return 1;
	}

	@Override
	public int getBurnTime() {
		return 900;
	}

	@Override
	public int getValueForPassiveGeneration() {
		return 20;
	}

	@Override
	public int getMaxMana() {
		return 500;
	}

	@Override
	public int getCooldown() {
		return 6000;
	}

	@Override
	public boolean isPassiveFlower() {
		return false;
	}
}
