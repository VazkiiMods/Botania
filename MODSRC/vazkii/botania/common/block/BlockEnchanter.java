/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 15, 2014, 4:08:26 PM (GMT)]
 */
package vazkii.botania.common.block;

import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraft.block.material.Material;

public class BlockEnchanter extends BlockMod {

	public BlockEnchanter() {
		super(LibBlockIDs.idEnchanter, Material.rock);
		setHardness(3.0F);
		setResistance(5.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(LibBlockNames.ENCHANTER);
	}

}
