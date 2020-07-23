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
import vazkii.botania.common.block.ModSubtiles;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;

public class ThermalilyCategory extends HydroangeasCategory {

	public ThermalilyCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.thermalily, ModSubtiles.thermalilyFloating);
	}

	@Override
	protected ITag<Fluid> getMaterial() {
		return FluidTags.LAVA;
	}

	@Override
	protected int getMana() {
		return 900 * 20;
	}
}
