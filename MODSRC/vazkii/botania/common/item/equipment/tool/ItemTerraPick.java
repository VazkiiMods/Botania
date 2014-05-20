/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 20, 2014, 10:56:14 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemTerraPick extends ItemManasteelPick {

	IIcon iconTool, iconOverlay;
	
	public ItemTerraPick() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.TERRA_PICK);
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		iconTool = IconHelper.forItem(par1IconRegister, this, 0);
		iconOverlay = IconHelper.forItem(par1IconRegister, this, 1);
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 2 && isEnabled(stack) ? iconOverlay : iconTool;
	}

	boolean isEnabled(ItemStack stack) {
		return false; // TODO
	}
	
}
