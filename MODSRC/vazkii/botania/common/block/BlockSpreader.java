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

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vazkii.botania.api.IWandable;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.HUDHandler.IHUD;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileSpreader;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockSpreader extends BlockModContainer implements IWandable, IHUD {

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
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileSpreader spreader = (TileSpreader) par1World.getBlockTileEntity(par2, par3, par4);
		ItemStack lens = spreader.getStackInSlot(0);
		ItemStack heldItem = par5EntityPlayer.getCurrentEquippedItem();
		boolean isHeldItemLens = heldItem != null && heldItem.getItem() instanceof ILens;

		if(heldItem != null && heldItem.itemID == ModItems.twigWand.itemID)
			return false;
		
		if(lens == null && isHeldItemLens) {
			par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
			spreader.setInventorySlotContents(0, heldItem.copy());
			spreader.onInventoryChanged();
		} else if(lens != null) {
			ItemStack add = lens.copy();
			if(!par5EntityPlayer.inventory.addItemStackToInventory(add))
				par5EntityPlayer.dropPlayerItem(add);
			spreader.setInventorySlotContents(0, null);
			spreader.onInventoryChanged();
		}
		
		return true;
	}
	
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TileSpreader) world.getBlockTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileSpreader();
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileSpreader) world.getBlockTileEntity(x, y, z)).renderHUD(mc, res);
	}

}
