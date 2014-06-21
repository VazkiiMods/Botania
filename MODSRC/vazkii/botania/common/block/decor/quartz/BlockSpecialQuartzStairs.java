/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 11, 2014, 1:12:50 AM (GMT)]
 */
package vazkii.botania.common.block.decor.quartz;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockSpecialQuartzStairs extends BlockModStairs {

	public BlockSpecialQuartzStairs(Block source) {
		super(source, 0, "quartzStairs" + ((BlockSpecialQuartz) source).type);
	}
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return this == ModBlocks.elfQuartzStairs ? LexiconData.elvenResources : super.getEntry(world, x, y, z, player, lexicon);
	}

}