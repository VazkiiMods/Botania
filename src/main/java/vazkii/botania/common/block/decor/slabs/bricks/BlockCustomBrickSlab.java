/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 28, 2014, 10:15:41 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs.bricks;

import net.minecraft.block.BlockSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockCustomBrickSlab extends BlockLivingSlab {

	public BlockCustomBrickSlab(boolean full) {
		this(full, 0);
	}

	public BlockCustomBrickSlab(boolean full, int meta) {
		super(full, ModBlocks.customBrick, meta);
		setHardness(2.0F);
		setResistance(5.0F);
		setStepSound(soundTypeStone);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.netherBrickSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.netherBrickSlab;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

}