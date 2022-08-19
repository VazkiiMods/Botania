/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 24, 2015, 3:15:11 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockAvatar extends BlockModContainer implements ILexiconable {

	private static final int[] META_ROTATIONS = new int[] { 2, 5, 3, 4 };

	Random random;

	protected BlockAvatar() {
		super(Material.wood);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setBlockName(LibBlockNames.AVATAR);
		setBlockBounds(true);

		random = new Random();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int s, float xs, float ys, float zs) {
		TileAvatar avatar = (TileAvatar) world.getTileEntity(x, y, z);
		ItemStack stackOnAvatar = avatar.getStackInSlot(0);
		ItemStack stackOnPlayer = player.getCurrentEquippedItem();
		if(stackOnAvatar != null) {
			ItemStack copyStack = stackOnAvatar.copy();
			avatar.setInventorySlotContents(0, null);
			if(!player.inventory.addItemStackToInventory(copyStack))
				player.dropPlayerItemWithRandomChoice(copyStack, true);
			return true;
		} else if(stackOnPlayer != null && stackOnPlayer.getItem() instanceof IAvatarWieldable) {
			ItemStack copyStack = stackOnPlayer.copy();
			avatar.setInventorySlotContents(0, copyStack);
			stackOnPlayer.stackSize--;
			return true;
		}

		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess w, 	int x, int y, int z) {
		setBlockBounds(w.getBlockMetadata(x, y, z) < 4);
	}

	public void setBlockBounds(boolean horiz) {
		float f = 1F / 16F;
		float w = f * 9;
		float l = f * 6;
		float ws = (1F - w) / 2;
		float ls = (1F - l) / 2;

		if(horiz)
			setBlockBounds(ws, 0F, ls, 1F - ws, 1F + f, 1F - ls);
		else setBlockBounds(ls, 0F, ws, 1F - ls, 1F + f, 1F - ws);
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		TileSimpleInventory inv = (TileSimpleInventory) par1World.getTileEntity(par2, par3, par4);

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
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, META_ROTATIONS[l], 2);
	}

	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
		return RotationHelper.rotateVanillaBlock(Blocks.furnace, worldObj, x, y, z, axis);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return ModBlocks.livingwood.getIcon(0, 0);
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
		return LibRenderIDs.idAvatar;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAvatar();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.avatar;
	}

}
