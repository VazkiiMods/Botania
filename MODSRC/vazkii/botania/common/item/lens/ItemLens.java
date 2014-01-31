/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 31, 2014, 3:02:58 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.item.ItemMod;

public abstract class ItemLens extends ItemMod implements ILens {

	public static Icon iconGlass;
	Icon iconRing;
	
	public ItemLens(int par1, String name) {
		super(par1);
		setUnlocalizedName(name);
		setMaxStackSize(1);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		iconGlass = IconHelper.forName(par1IconRegister, "lensInside");
		iconRing = IconHelper.forName(par1IconRegister, "lensRing");
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@Override
	public Icon getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 == 0 ? iconRing : iconGlass;
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return par2 == 1 ? getLensColor(par1ItemStack) : 0xFFFFFF;
	}
	
}
