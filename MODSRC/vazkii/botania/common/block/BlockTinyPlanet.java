/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 1, 2014, 3:49:12 PM (GMT)]
 */
package vazkii.botania.common.block;

import vazkii.botania.common.block.tile.TileTinyPlanet;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTinyPlanet extends BlockModContainer {

	protected BlockTinyPlanet() {
		super(Material.rock);
		setHardness(20F);
		setResistance(100F);
		setStepSound(soundTypeStone);
		float size = 3F / 16F;
		setBlockBounds(size, size, size, 1F - size, 1F - size, 1F - size);
		setBlockName(LibBlockNames.TINY_PLANET);
	}
	
	@Override
	boolean registerInCreative() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileTinyPlanet();
	}
	
}
