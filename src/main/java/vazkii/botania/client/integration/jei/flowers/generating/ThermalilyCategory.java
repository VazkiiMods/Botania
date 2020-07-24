/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import mezz.jei.api.helpers.IGuiHelper;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.integration.jei.misc.ParticleDrawable;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileThermalily;

public class ThermalilyCategory extends HydroangeasCategory {

	public ThermalilyCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.thermalily, ModSubtiles.thermalilyFloating);
	}

	@Override
	protected void doBurnParticles(ParticleDrawable drawable) {
		WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.7F, 0.05F, 0.05F, 1);
		drawable.addParticle(data,
				0.55 + Math.random() * 0.2 - 0.1,
				0.9 + Math.random() * 0.2 - 0.1,
				0,
				0,
				(float) Math.random() / 60,
				0);
	}

	@Override
	protected ITag<Fluid> getMaterial() {
		return FluidTags.LAVA;
	}

	@Override
	protected int getMana() {
		return SubTileThermalily.BURN_TIME * SubTileThermalily.GEN;
	}
}
