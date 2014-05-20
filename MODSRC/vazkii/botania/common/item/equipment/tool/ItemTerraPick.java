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

import java.awt.Color;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemTerraPick extends ItemManasteelPick {

	private static final String TAG_ENABLED = "enabled";
	
	IIcon iconTool, iconOverlay;
	
	public ItemTerraPick() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.TERRA_PICK);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		ItemNBTHelper.setBoolean(par1ItemStack, TAG_ENABLED, !isEnabled(par1ItemStack));
		return par1ItemStack;
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
		return pass == 1 && isEnabled(stack) ? iconOverlay : iconTool;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0 || !isEnabled(par1ItemStack))
			return 0xFFFFFF;
		
		return Color.HSBtoRGB(0.375F, (float) Math.min(1F, (Math.sin((double) System.currentTimeMillis() / 200D) * 0.5 + 1F)), 1F);
	}
	
	boolean isEnabled(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ENABLED, false);
	}
	
}
