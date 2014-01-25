/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 25, 2014, 9:38:23 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vazkii.botania.api.IWandable;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileSpreader;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockSpreader extends BlockModContainer implements IWandable {

	protected BlockSpreader() {
		super(LibBlockIDs.idSpreader, Material.wood);
		setHardness(2.0F);
		setStepSound(soundWoodFootstep);
		setUnlocalizedName(LibBlockNames.SPREADER);
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
		return ModBlocks.livingwood.getIcon(par1, par2);
	}
	
	@Override
	public int getRenderType() {
		return LibRenderIDs.idSpreader;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileSpreader();
	}

}
