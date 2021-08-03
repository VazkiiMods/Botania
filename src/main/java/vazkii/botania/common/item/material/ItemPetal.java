/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.ICustomApothecaryColor;
import vazkii.botania.common.core.helper.ColorHelper;

import javax.annotation.Nonnull;

public class ItemPetal extends BlockItem implements ICustomApothecaryColor {
	public final DyeColor color;

	public ItemPetal(Block buriedPetals, DyeColor color, Properties props) {
		super(buriedPetals, props);
		this.color = color;
	}

	@Nonnull
	@Override
	public String getDescriptionId() {
		// Don't take name of the block
		return getOrCreateDescriptionId();
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return ColorHelper.getColorValue(color);
	}
}
