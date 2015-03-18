/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 17, 2015, 6:48:29 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityPoolMinecart;
import vazkii.botania.common.lib.LibItemNames;

public class ItemPoolMinecart extends ItemMod {

	public ItemPoolMinecart() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.POOL_MINECART);
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if(BlockRailBase.func_150051_a(p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_))) {
			if(!p_77648_3_.isRemote) {
				EntityMinecart entityminecart = new EntityPoolMinecart(p_77648_3_, p_77648_4_ + 0.5, p_77648_5_ + 0.5, p_77648_6_ + 0.5);

				if(p_77648_1_.hasDisplayName())
					entityminecart.setMinecartName(p_77648_1_.getDisplayName());

				p_77648_3_.spawnEntityInWorld(entityminecart);
			}

			--p_77648_1_.stackSize;
			return true;
		}

		return false;
	}

}
