/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 8, 2014, 5:25:12 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockTerraPlate extends BlockModContainer implements ILexiconable {

	public static IIcon overlay;
	IIcon[] icons;

	public BlockTerraPlate() {
		super(Material.iron);
		setBlockBounds(0F, 0F, 0F, 1F, 3F / 16F, 1F);
		setHardness(3F);
		setResistance(10F);
		setStepSound(soundTypeMetal);
		setBlockName(LibBlockNames.TERRA_PLATE);

		BotaniaAPI.blacklistBlockFromMagnet(this, Short.MAX_VALUE);
	}

	@Override
	public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int s, float xs, float ys, float zs) {
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && stack.getItem() == ModItems.manaResource && stack.getItemDamage() < 3) {
			if(player == null || !player.capabilities.isCreativeMode) {
				stack.stackSize--;
				if(stack.stackSize == 0 && player != null)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}

			ItemStack target = stack.copy();
			target.stackSize = 1;
			EntityItem item = new EntityItem(worldObj, x + 0.5, y + 0.5, z + 0.5, target);
			item.delayBeforeCanPickup = 40;
			item.motionX = item.motionY = item.motionZ = 0;
			if(!worldObj.isRemote)
				worldObj.spawnEntityInWorld(item);

			return true;
		}

		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
		return false;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[3];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
		overlay = IconHelper.forBlock(par1IconRegister, this, "Overlay");
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return icons[Math.min(2, par1)];
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileTerraPlate();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.terrasteel;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
		TileTerraPlate plate = (TileTerraPlate) par1World.getTileEntity(par2, par3, par4);
		int val = (int) ((double) plate.getCurrentMana() / (double) TileTerraPlate.MAX_MANA * 15.0);
		if(plate.getCurrentMana() > 0)
			val = Math.max(val, 1);

		return val;
	}

}
