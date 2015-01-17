/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 17, 2015, 7:16:48 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockPrism extends BlockModContainer implements IManaTrigger {

	IIcon[] icons;
	
	public BlockPrism() {
		super(Material.glass);
		setHardness(0.3F);
		setStepSound(soundTypeGlass);
		setLightLevel(1.0F);
		setBlockName(LibBlockNames.PRISM);
		float f = 0.25F;
		setBlockBounds(f, 0F, f, 1F - f, 1F, 1F - f);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[2];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side > 1 ? icons[1] : icons[0];
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
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
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if(!(tile instanceof TilePrism))
			return false;

		TilePrism prism = (TilePrism) tile;
		ItemStack lens = prism.getStackInSlot(0);
		ItemStack heldItem = par5EntityPlayer.getCurrentEquippedItem();
		boolean isHeldItemLens = heldItem != null && heldItem.getItem() instanceof ILens;

		if(lens == null && isHeldItemLens) {
			par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
			prism.setInventorySlotContents(0, heldItem.copy());
			prism.markDirty();
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 1 | 2);
		} else if(lens != null) {
			ItemStack add = lens.copy();
			if(!par5EntityPlayer.inventory.addItemStackToInventory(add))
				par5EntityPlayer.dropPlayerItemWithRandomChoice(add, false);
			prism.setInventorySlotContents(0, null);
			prism.markDirty();
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 1 | 2);
		}

		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TilePrism();
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TilePrism)
			((TilePrism) tile).onBurstCollision(burst);
	}

}
