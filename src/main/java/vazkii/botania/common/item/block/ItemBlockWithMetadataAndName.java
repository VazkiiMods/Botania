/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 16, 2014, 5:54:06 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.lib.LibResources;

public class ItemBlockWithMetadataAndName extends ItemBlock {

	public ItemBlockWithMetadataAndName(Block par2Block) {
		super(par2Block);
		setHasSubtypes(true);
	}

	@Nonnull
	@Override
	public String getUnlocalizedNameInefficiently(@Nonnull ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("tile.", "tile." + LibResources.PREFIX_MOD);
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

}
