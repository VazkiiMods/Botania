/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 3, 2014, 1:44:29 AM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.TileDistributor;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockDistributor extends BlockModContainer {

	Icon iconSide, iconTop;
	
	protected BlockDistributor() {
		super(LibBlockIDs.idDistributor, Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(LibBlockNames.DISTRIBUTOR);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		iconTop = IconHelper.forBlock(par1IconRegister, this, 0);
		iconSide = IconHelper.forBlock(par1IconRegister, this, 1);
	}
	
	@Override
	public Icon getIcon(int par1, int par2) {
		return par1 == 0 ? ModBlocks.livingrock.getIcon(0, 0) : par1 == 1 ? iconTop : iconSide;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileDistributor();
	}

}
