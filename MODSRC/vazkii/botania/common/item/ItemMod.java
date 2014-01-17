/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 5:30:18 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.BotaniaCreativeTab;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMod extends Item {

	public ItemMod(int par1) {
		super(par1);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}

	@Override
	public Item setUnlocalizedName(String par1Str) {
		GameRegistry.registerItem(this, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this);
	}
}