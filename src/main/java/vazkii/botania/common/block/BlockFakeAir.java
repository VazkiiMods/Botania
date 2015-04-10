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

import java.util.List;
import java.util.Random;

import vazkii.botania.common.lib.LibBlockNames;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFakeAir extends BlockMod {

	public BlockFakeAir() {
		super(Material.air);
		setBlockName(LibBlockNames.FAKE_AIR);
		setBlockBounds(0, 0, 0, 0, 0, 0);
	}

	@Override
	boolean registerInCreative() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		// NO-OP
	}
	
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public boolean isAir(IBlockAccess world, int x, int y, int z) {
		return true;
	}

}
