/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 28, 2014, 8:49:59 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs;

import net.minecraft.block.BlockSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockThatchSlab extends BlockLivingSlab {

	public BlockThatchSlab(boolean full) {
		super(full, ModBlocks.thatch, 0);
		setHardness(1.0F);
		setStepSound(soundTypeGrass);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModBlocks.thatchSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModBlocks.thatchSlab;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}


}
