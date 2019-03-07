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
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.ICustomApothecaryColor;
import vazkii.botania.common.item.block.ItemBlockMod;

public class ItemPetal extends ItemBlockMod implements ICustomApothecaryColor {
	public final EnumDyeColor color;

	public ItemPetal(Block buriedPetals, EnumDyeColor color, Properties props) {
		super(buriedPetals, props);
		this.color = color;
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return color.colorValue;
	}
}
