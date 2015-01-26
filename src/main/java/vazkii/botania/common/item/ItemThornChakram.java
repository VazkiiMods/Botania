/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 25, 2015, 6:42:47 PM (GMT)]
 */
package vazkii.botania.common.item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityThornChakram;
import vazkii.botania.common.lib.LibItemNames;

public class ItemThornChakram extends ItemMod {

	public ItemThornChakram() {
		setUnlocalizedName(LibItemNames.THORN_CHAKRAM);
		setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)  {
		--p_77659_1_.stackSize;

		p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if(!p_77659_2_.isRemote)
			p_77659_2_.spawnEntityInWorld(new EntityThornChakram(p_77659_2_, p_77659_3_));

		return p_77659_1_;
	}

}
