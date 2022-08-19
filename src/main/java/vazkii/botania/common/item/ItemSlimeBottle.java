/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 17, 2015, 1:32:58 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSlimeBottle extends ItemMod {

	IIcon activeIcon;

	public ItemSlimeBottle() {
		setUnlocalizedName(LibItemNames.SLIME_BOTTLE);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this, 0);
		activeIcon = IconHelper.forItem(par1IconRegister, this, 1);
	}

	@Override
	public IIcon getIconFromDamage(int dmg) {
		return dmg == 0 ? itemIcon : activeIcon;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int something, boolean somethingelse) {
		if(!world.isRemote) {
			int x = MathHelper.floor_double(entity.posX);
			int z = MathHelper.floor_double(entity.posZ);
			boolean slime = SubTileNarslimmus.SpawnIntercepter.isSlimeChunk(world, x, z);
			int meta = stack.getItemDamage();
			int newMeta = slime ? 1 : 0;
			if(meta != newMeta)
				stack.setItemDamage(newMeta);
		}
	}

}
