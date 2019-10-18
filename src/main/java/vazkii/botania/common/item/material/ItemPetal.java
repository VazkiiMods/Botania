/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 3:28:21 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.ICustomApothecaryColor;
import vazkii.botania.common.item.block.ItemBlockMod;

import javax.annotation.Nonnull;

public class ItemPetal extends ItemBlockMod implements ICustomApothecaryColor {
	public final DyeColor color;

	public ItemPetal(Block buriedPetals, DyeColor color, Properties props) {
		super(buriedPetals, props);
		this.color = color;
	}

	@Nonnull
	@Override
	public String getTranslationKey() {
		// Don't take name of the block
		return getDefaultTranslationKey();
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return color.colorValue;
	}
}
