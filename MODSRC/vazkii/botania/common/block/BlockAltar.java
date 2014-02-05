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

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vazkii.botania.api.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.network.PacketDispatcher;

public class BlockAltar extends BlockModContainer implements ILexiconable {

	Random random;

	protected BlockAltar() {
		super(LibBlockIDs.idAltar, Material.rock);
		setHardness(3.5F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(LibBlockNames.ALTAR);

		float f = 1F / 16F * 2F;
		setBlockBounds(f, f, f, 1F - f, 1F / 16F * 20F, 1F - f);

		random = new Random();
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if(par5Entity instanceof EntityItem) {
			TileAltar tile = (TileAltar) par1World.getBlockTileEntity(par2, par3, par4);
			if(tile.collideEntityItem((EntityItem) par5Entity))
				PacketDispatcher.sendPacketToAllInDimension(tile.getDescriptionPacket(), par1World.provider.dimensionId);
		}
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
	public Icon getIcon(int par1, int par2) {
		return Block.cobblestone.getIcon(par1, par2);
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
	public int getRenderType() {
		return LibRenderIDs.idAltar;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileAltar();
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		TileSimpleInventory inv = (TileSimpleInventory) par1World.getBlockTileEntity(par2, par3, par4);

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
						entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float)random.nextGaussian() * f3;
						entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float)random.nextGaussian() * f3;

						if (itemstack.hasTagCompound())
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
				}
			}

			par1World.func_96440_m(par2, par3, par4, par5);
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.apothecary;
	}

}
