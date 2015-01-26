/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 20, 2014, 8:23:17 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockBifrost extends BlockModContainer {

	public BlockBifrost() {
		super(Material.glass);
		setBlockName(LibBlockNames.BIFROST);
		setLightOpacity(0);
		setBlockUnbreakable();
		setStepSound(soundTypeGlass);
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(ModItems.rainbowRod);
	}

	public boolean shouldSideBeRendered1(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		Block block = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_);

		return block == this ? false : super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return shouldSideBeRendered1(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, 1 - p_149646_5_);
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileBifrost();
	}

}
