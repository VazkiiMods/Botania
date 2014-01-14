/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 5:31:15 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.BotaniaCreativeTab;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMod extends Block {

	public BlockMod(int par1, Material par2Material) {
		super(par1, par2Material);
		if(registerInCreative())
			setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		blockIcon = IconHelper.forBlock(par1IconRegister, this);
	}

	boolean registerInCreative() {
		return true;
	}


}
