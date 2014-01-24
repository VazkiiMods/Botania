/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 22, 2014, 7:06:38 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.api.ISpecialFlower;
import vazkii.botania.common.core.BotaniaCreativeTab;

public abstract class BlockSpecialFlower<T extends TileEntity> extends BlockFlower implements ISpecialFlower, ITileEntityProvider {

	protected BlockSpecialFlower(int par1, String name) {
		super(par1);
		setUnlocalizedName(name);
		setHardness(0F);
		setStepSound(soundGrassFootstep);
		setTickRandomly(true);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}
	
	@Override
	public abstract T createNewTileEntity(World world);
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
		par1World.removeBlockTileEntity(par2, par3, par4);
	}
	
	@Override
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
		super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
		TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
	}

}
