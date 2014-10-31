/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Oct 31, 2014, 4:37:29 PM (GMT)]
 */
package vazkii.botania.common.block;

import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileBrewery;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBrewery extends BlockModContainer {

	protected BlockBrewery() {
		super(Material.rock);
		float f = 6F / 16F;
		setBlockBounds(f, 0F, f, 1F - f, 1F, 1F - f);
		setBlockName(LibBlockNames.BREWERY);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}
	
	@Override
	public int getRenderType() {
		return LibRenderIDs.idBrewery;
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
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileBrewery();
	} 

}
