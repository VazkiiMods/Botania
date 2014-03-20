/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 12, 2014, 4:25:35 PM (GMT)]
 */
package vazkii.botania.common.block.vanilla;

import net.minecraft.block.BlockFlower;
import vazkii.botania.client.lib.LibRenderIDs;

public class BlockFlowerOverride extends BlockFlower {

	public BlockFlowerOverride(int par1) {
		super(par1);
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idSpecialFlower;
	}

}
