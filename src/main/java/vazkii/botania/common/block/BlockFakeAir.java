/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 10, 2015, 10:22:38 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileFakeAir;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockFakeAir extends BlockModContainer {

	public BlockFakeAir() {
		super(Material.air);
		setBlockName(LibBlockNames.FAKE_AIR);
		setBlockBounds(0, 0, 0, 0, 0, 0);
		setTickRandomly(true);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if(shouldRemove(world, x, y, z))
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}

	private boolean shouldRemove(World world, int x, int y, int z) {
		return !world.isRemote && world.getTileEntity(x, y, z) == null || !(world.getTileEntity(x, y, z) instanceof TileFakeAir) || !((TileFakeAir) world.getTileEntity(x, y, z)).canStay();
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if(shouldRemove(world, x, y, z))
			world.setBlock(x, y, z, Blocks.water);
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 4;
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	public boolean isBlockNormalCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity e) {
		return false;
	}

	@Override
	public boolean canCollideCheck(int par1, boolean par2) {
		return false;
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public boolean canDropFromExplosion(Explosion par1Explosion) {
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		return new ArrayList(); // Empty List
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	@Override
	public boolean isAir(IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFakeAir();
	}

}
