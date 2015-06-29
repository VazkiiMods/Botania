/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 15, 2015, 4:07:09 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileIncensePlate;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockIncensePlate extends BlockModContainer implements ILexiconable {

	private static final int[] META_ROTATIONS = new int[] { 2, 5, 3, 4 };

	protected BlockIncensePlate() {
		super(Material.wood);
		setBlockName(LibBlockNames.INCENSE_PLATE);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setBlockBounds(true);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int s, float xs, float ys, float zs) {
		TileIncensePlate plate = (TileIncensePlate) world.getTileEntity(x, y, z);
		ItemStack stack = player.getCurrentEquippedItem();
		ItemStack plateStack = plate.getStackInSlot(0);
		boolean did = false;

		if(world.isRemote)
			return true;

		if(plateStack == null && plate.isItemValidForSlot(0, stack)) {
			plate.setInventorySlotContents(0, stack.copy());
			stack.stackSize--;
			did = true;
		} else if(plateStack != null && !plate.burning) {
			if(stack != null && stack.getItem() == Items.flint_and_steel) {
				plate.ignite();
				stack.damageItem(1, player);
				did = true;
			} else {
				ItemStack addStack = plateStack.copy();
				if(!player.inventory.addItemStackToInventory(addStack))
					player.dropPlayerItemWithRandomChoice(addStack, false);
				plate.setInventorySlotContents(0, null);

				did = true;
			}
		}

		if(did)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);

		return did;
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, META_ROTATIONS[l], 2);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
		return ((TileIncensePlate) world.getTileEntity(x, y, z)).comparatorOutput;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess w, 	int x, int y, int z) {
		setBlockBounds(w.getBlockMetadata(x, y, z) < 4);
	}

	public void setBlockBounds(boolean horiz) {
		float f = 1F / 16F;
		float w = 12 * f;
		float l = 4 * f;
		float xs = (1F - w) / 2;
		float zs = (1F - l) / 2;
		if(horiz)
			setBlockBounds(xs, 0F, zs, 1F - xs, f, 1f - zs);
		else setBlockBounds(zs, 0F, xs, 1F - zs, f, 1f - xs);
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
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
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
		return LibRenderIDs.idIncensePlate;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileIncensePlate();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.incense;
	}

}
