/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 21, 2014, 7:48:54 PM (GMT)]
 */
package vazkii.botania.common.block;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockAltar extends BlockModContainer {

	protected BlockAltar() {
		super(LibBlockIDs.idAltar, Material.rock);
		setHardness(3.5F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(LibBlockNames.ALTAR);
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		ItemStack stack = par5EntityPlayer.getCurrentEquippedItem();
		TileAltar tile = (TileAltar) par1World.getBlockTileEntity(par2, par3, par4);
		if(stack != null && stack.itemID == Item.bucketWater.itemID) {
			if(!tile.hasWater) {
				par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, new ItemStack(Item.bucketEmpty));
				tile.hasWater = true;
				PacketDispatcher.sendPacketToAllInDimension(tile.getDescriptionPacket(), par1World.provider.dimensionId);
			}

			return true;
		}
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
	
	public int getRenderType() {
		return LibRenderIDs.idAltar;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileAltar();
	}

}
