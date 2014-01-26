/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 26, 2014, 12:22:58 AM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TilePool;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockPool extends BlockModContainer {

	protected BlockPool() {
		super(LibBlockIDs.idPool, Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(LibBlockNames.POOL);
		setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TilePool();
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
	public Icon getIcon(int par1, int par2) {
		return ModBlocks.livingrock.getIcon(par1, par2);
	}
	
	@Override
	public int getRenderType() {
		return LibRenderIDs.idPool;
	}

}
