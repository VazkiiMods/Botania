/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 11, 2014, 1:13:36 AM (GMT)]
 */
package vazkii.botania.common.item.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartz;

public class ItemBlockSpecialQuartz extends ItemMultiTexture {

	public ItemBlockSpecialQuartz(Block par1) {
		super(par1, par1, new String[]{ "" });
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(@Nonnull ItemStack par1ItemStack) {
		return par1ItemStack.getItemDamage() >= 3 ? "" : ((BlockSpecialQuartz) block).getNames()[par1ItemStack.getItemDamage()];
	}
}