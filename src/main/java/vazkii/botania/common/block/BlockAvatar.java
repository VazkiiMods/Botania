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

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockAvatar extends BlockModContainer implements ILexiconable {

	Random random;

	protected BlockAvatar() {
		super(Material.wood);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setUnlocalizedName(LibBlockNames.AVATAR);
		setBlockBounds(EnumFacing.Axis.Z);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.NORTH));
		random = new Random();
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.CARDINALS);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.CARDINALS).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 2 || meta > 5) {
			meta = 2;
		}
		return getDefaultState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.getFront(meta));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing s, float xs, float ys, float zs) {
		TileAvatar avatar = (TileAvatar) world.getTileEntity(pos);
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
	public void setBlockBoundsBasedOnState(IBlockAccess w, BlockPos pos) {
		setBlockBounds(w.getBlockState(pos).getValue(BotaniaStateProps.CARDINALS).getAxis());
	}

	public void setBlockBounds(EnumFacing.Axis horiz) {
		float f = 1F / 16F;
		float w = f * 9;
		float l = f * 6;
		float ws = (1F - w) / 2;
		float ls = (1F - l) / 2;

		if(horiz == EnumFacing.Axis.Z)
			setBlockBounds(ws, 0F, ls, 1F - ws, 1F + f, 1F - ls);
		else setBlockBounds(ls, 0F, ws, 1F - ls, 1F + f, 1F - ws);
	}

	@Override
	public void breakBlock(World par1World, BlockPos pos, IBlockState state) {
		TileSimpleInventory inv = (TileSimpleInventory) par1World.getTileEntity(pos);

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
						entityitem = new EntityItem(par1World, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float)random.nextGaussian() * f3;
						entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float)random.nextGaussian() * f3;

						if (itemstack.hasTagCompound())
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
				}
			}

			par1World.updateComparatorOutputLevel(pos, state.getBlock());
		}

		super.breakBlock(par1World, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, BlockPos pos, IBlockState state, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		p_149689_1_.setBlockState(pos, state.withProperty(BotaniaStateProps.CARDINALS, p_149689_5_.getHorizontalFacing().getOpposite()));
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 2;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAvatar();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.avatar;
	}

}
