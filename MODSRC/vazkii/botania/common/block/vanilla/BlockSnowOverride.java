/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 12, 2014, 4:08:35 PM (GMT)]
 */
package vazkii.botania.common.block.vanilla;

import net.minecraft.block.BlockSnow;
import net.minecraft.world.IBlockAccess;

public class BlockSnowOverride extends BlockSnow {

	public static int forcedMeta = -1;
	
	public BlockSnowOverride(int par1) {
		super(par1);
	}
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		if(forcedMeta != -1) {
			setBlockBoundsForSnowDepth(forcedMeta);
			forcedMeta = -1;
		} else super.setBlockBoundsBasedOnState(par1IBlockAccess, par2, par3, par4);
	}

}
