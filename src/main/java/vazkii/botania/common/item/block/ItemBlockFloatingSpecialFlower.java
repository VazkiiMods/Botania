/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 17, 2014, 5:39:08 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

public class ItemBlockFloatingSpecialFlower extends ItemBlockSpecialFlower {

	public ItemBlockFloatingSpecialFlower(Block block1) {
		super(block1);
	}

	@Nonnull
	@Override
	public String getItemStackDisplayName(@Nonnull ItemStack stack) {
		String flowerName = getTranslationKey(stack) + ".name";
		return String.format(I18n.translateToLocal("botaniamisc.floatingPrefix"), I18n.translateToLocal(flowerName));
	}

}
