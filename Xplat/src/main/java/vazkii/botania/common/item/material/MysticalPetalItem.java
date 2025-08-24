/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.CustomApothecaryColor;

public class MysticalPetalItem extends ItemNameBlockItem implements CustomApothecaryColor {
	public final DyeColor color;

	public MysticalPetalItem(Block buriedPetals, DyeColor color, Properties props) {
		super(buriedPetals, props);
		this.color = color;
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return getPetalLikeColor(color);
	}

	// Color value that looks like the petal textures
	public static int getPetalLikeColor(DyeColor color) {
		return switch (color) {
			case WHITE -> 0xF0FFFF;
			case ORANGE -> 0xF88A10;
			case MAGENTA -> 0xDC04D7;
			case LIGHT_BLUE -> 0x72C4FF;
			case YELLOW -> 0xFFF148;
			case LIME -> 0x62FF2C;
			case PINK -> 0xFB8BC7;
			case GRAY -> 0x71747C;
			case LIGHT_GRAY -> 0x9DA8A7;
			case CYAN -> 0x34B0D0;
			case PURPLE -> 0x950BA8;
			case BLUE -> 0x2448EB;
			case BROWN -> 0x976405;
			case GREEN -> 0x3AAB0D;
			case RED -> 0xDD111F;
			case BLACK -> 0x261E28;
		};
	}
}
