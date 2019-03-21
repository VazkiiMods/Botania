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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;

public class ItemBlockFloatingSpecialFlower extends ItemBlockSpecialFlower {

	public ItemBlockFloatingSpecialFlower(Block block, Properties props) {
		super(block, props);
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
		ITextComponent flowerName = super.getDisplayName(stack);
		return new TextComponentTranslation("botaniamisc.floatingPrefix").appendSibling(flowerName);
	}

}
