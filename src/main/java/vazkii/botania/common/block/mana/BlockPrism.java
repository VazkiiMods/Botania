/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 17, 2015, 7:16:48 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockPrism extends BlockModContainer implements IManaTrigger, ILexiconable {

	Random random;
	IIcon[] icons;

	public BlockPrism() {
		super(Material.glass);
		setHardness(0.3F);
		setStepSound(soundTypeGlass);
		setLightLevel(1.0F);
		setBlockName(LibBlockNames.PRISM);
		float f = 0.25F;
		setBlockBounds(f, 0F, f, 1F - f, 1F, 1F - f);

		random = new Random();
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
		int meta = par1World.getBlockMetadata(par2, par3, par4);

		if(lens == null && isHeldItemLens) {
			if(!par5EntityPlayer.capabilities.isCreativeMode)
				par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);

			prism.setInventorySlotContents(0, heldItem.copy());
			prism.markDirty();
			par1World.setBlockMetadataWithNotify(par2, par3, par4, meta | 1, 1 | 2);
		} else if(lens != null) {
			ItemStack add = lens.copy();
			if(!par5EntityPlayer.inventory.addItemStackToInventory(add))
				par5EntityPlayer.dropPlayerItemWithRandomChoice(add, false);
			prism.setInventorySlotContents(0, null);
			prism.markDirty();
			par1World.setBlockMetadataWithNotify(par2, par3, par4, meta & 14, 1 | 2);
		}

		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		boolean power = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
		int meta = world.getBlockMetadata(x, y, z);
		boolean powered = (meta & 8) != 0;

		if(!world.isRemote) {
			if(power && !powered)
				world.setBlockMetadataWithNotify(x, y, z, meta | 8, 1 | 2);
			else if(!power && powered)
				world.setBlockMetadataWithNotify(x, y, z, meta & -9, 1 | 2);
		}
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if(!(tile instanceof TileSimpleInventory))
			return;

		TileSimpleInventory inv = (TileSimpleInventory) tile;

		if (inv != null) {
			for (int j1 = 0; j1 < inv.getSizeInventory(); ++j1) {
				ItemStack itemstack = inv.getStackInSlot(j1);

				if (itemstack != null) {
					float f = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem)) {
						int k1 = random.nextInt(21) + 10;

						if (k1 > itemstack.stackSize)
							k1 = itemstack.stackSize;

						itemstack.stackSize -= k1;
						entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float)random.nextGaussian() * f3;
						entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float)random.nextGaussian() * f3;

						if (itemstack.hasTagCompound())
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
				}
			}

			par1World.func_147453_f(par2, par3, par4, par5);
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
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

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.prism;
	}

}
