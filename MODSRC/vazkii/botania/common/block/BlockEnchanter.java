/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 15, 2014, 4:08:26 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vazkii.botania.api.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockEnchanter extends BlockModContainer implements IWandable {

	public static Icon overlay;

	public BlockEnchanter() {
		super(LibBlockIDs.idEnchanter, Material.rock);
		setHardness(3.0F);
		setResistance(5.0F);
		setLightValue(1.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(LibBlockNames.ENCHANTER);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		overlay = IconHelper.forBlock(par1IconRegister, this, "Overlay");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEnchanter();
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return Block.blockLapis.blockID;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEnchanter enchanter = (TileEnchanter) par1World.getBlockTileEntity(par2, par3, par4);
		ItemStack stack = par5EntityPlayer.getCurrentEquippedItem();
		if(stack != null && stack.itemID == ModItems.twigWand.itemID)
			return false;

		boolean stackEnchantable = stack != null && stack.isItemEnchantable() && stack.stackSize == 1;

		if(enchanter.itemToEnchant == null) {
			if(stackEnchantable) {
				enchanter.itemToEnchant = stack.copy();
				par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
				enchanter.sync();
			}
		} else if(enchanter.stage == 0) {
			if(par5EntityPlayer.inventory.addItemStackToInventory(enchanter.itemToEnchant.copy())) {
				System.out.println("did");
				enchanter.itemToEnchant = null;
				enchanter.sync();
			} else par5EntityPlayer.addChatMessage("botaniamisc.invFull");
		}

		return true;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TileEnchanter) world.getBlockTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}

}
