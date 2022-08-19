/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 29, 2015, 8:17:08 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileHourglass;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockHourglass extends BlockModContainer implements IManaTrigger, IWandable, IWandHUD, ILexiconable {

	Random random;

	protected BlockHourglass() {
		super(Material.iron);
		setBlockName(LibBlockNames.HOURGLASS);
		setHardness(2.0F);
		setStepSound(soundTypeMetal);

		float f = 1F / 16F;
		float w = 8F * f;
		float d = (1F - w) / 2;
		setBlockBounds(d, 0F, d, 1F - d, 1.15F, 1F - d);

		random = new Random();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xs, float ys, float zs) {
		TileHourglass hourglass = (TileHourglass) world.getTileEntity(x, y, z);
		ItemStack hgStack = hourglass.getStackInSlot(0);
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && stack.getItem() == ModItems.twigWand)
			return false;

		if(hourglass.lock) {
			if(!player.worldObj.isRemote)
				player.addChatMessage(new ChatComponentTranslation("botaniamisc.hourglassLock"));
			return true;
		}

		if(hgStack == null && TileHourglass.getStackItemTime(stack) > 0) {
			hourglass.setInventorySlotContents(0, stack.copy());
			hourglass.markDirty();
			stack.stackSize = 0;
			return true;
		} else if(hgStack != null) {
			ItemStack copy = hgStack.copy();
			if(!player.inventory.addItemStackToInventory(copy))
				player.dropPlayerItemWithRandomChoice(copy, false);
			hourglass.setInventorySlotContents(0, null);
			hourglass.markDirty();
			return true;
		}

		return false;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return world.getBlockMetadata(x, y, z) == 0 ? 0 : 15;
	}

	@Override
	public int tickRate(World world) {
		return 4;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		world.setBlockMetadataWithNotify(x, y, z, 0, 1 | 2);
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
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}

	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return ModBlocks.manaGlass.getIcon(0, 0);
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
		return LibRenderIDs.idHourglass;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileHourglass();
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		if(!world.isRemote && !burst.isFake()) {
			TileHourglass tile = (TileHourglass) world.getTileEntity(x, y, z);
			tile.move = !tile.move;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		}
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		TileHourglass tile = (TileHourglass) world.getTileEntity(x, y, z);
		tile.lock = !tile.lock;
		return false;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		TileHourglass tile = (TileHourglass) world.getTileEntity(x, y, z);
		tile.renderHUD(res);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.hourglass;
	}

}
