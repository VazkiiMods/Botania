/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 23, 2015, 11:40:51 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibItemNames;

public class ItemGaiaHead extends ItemMod {

	public ItemGaiaHead() {
		setUnlocalizedName(LibItemNames.GAIA_HEAD);
	}

	// Copypasta from ItemSkull and I'm not even going to bother to clean it up
	//
	// Deal with it.
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && side != EnumFacing.DOWN)
		{
			side = EnumFacing.UP;
			pos = pos.down();
		}
		if (side == EnumFacing.DOWN)
		{
			return false;
		}
		else
		{
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);

			if (!flag)
			{
				if (!worldIn.isSideSolid(pos, side, true))
				{
					return false;
				}

				pos = pos.offset(side);
			}

			if (!playerIn.canPlayerEdit(pos, side, stack))
			{
				return false;
			}
			else if (!Blocks.skull.canPlaceBlockAt(worldIn, pos))
			{
				return false;
			}
			else
			{
				if (!worldIn.isRemote)
				{
					if (!Blocks.skull.canPlaceBlockOnSide(worldIn, pos, side)) return false;
					worldIn.setBlockState(pos, ModBlocks.gaiaHead.getDefaultState().withProperty(BlockSkull.FACING, side), 3); // Botania: skull -> gaia Head
					int i = 0;

					if (side == EnumFacing.UP)
					{
						i = MathHelper.floor_double((double)(playerIn.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
					}

					TileEntity tileentity = worldIn.getTileEntity(pos);

					if (tileentity instanceof TileEntitySkull)
					{
						TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;

						if (stack.getMetadata() == 3) // Botania: Don't retrieve skins
						{
//							GameProfile gameprofile = null;
//
//							if (stack.hasTagCompound())
//							{
//								NBTTagCompound nbttagcompound = stack.getTagCompound();
//
//								if (nbttagcompound.hasKey("SkullOwner", 10))
//								{
//									gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
//								}
//								else if (nbttagcompound.hasKey("SkullOwner", 8) && nbttagcompound.getString("SkullOwner").length() > 0)
//								{
//									gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
//								}
//							}
//
//							tileentityskull.setPlayerProfile(gameprofile);
						}
						else
						{
							tileentityskull.setType(stack.getMetadata());
						}

						tileentityskull.setSkullRotation(i);
						Blocks.skull.checkWitherSpawn(worldIn, pos, tileentityskull);
					}

					--stack.stackSize;
				}

				return true;
			}
		}
	}

}
