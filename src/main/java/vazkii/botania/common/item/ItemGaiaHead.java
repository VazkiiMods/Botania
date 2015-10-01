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

import net.minecraft.block.BlockSkull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
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
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
	{
		if(p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_).isReplaceable(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_) && p_77648_7_ != 0)
		{
			p_77648_7_ = 1;
			p_77648_5_--;
		}
		if (p_77648_7_ == 0)
		{
			return false;
		}
		else if (!p_77648_3_.isSideSolid(p_77648_4_, p_77648_5_, p_77648_6_, net.minecraftforge.common.util.ForgeDirection.getOrientation(p_77648_7_)))
		{
			return false;
		}
		else
		{
			if (p_77648_7_ == 1)
			{
				++p_77648_5_;
			}

			if (p_77648_7_ == 2)
			{
				--p_77648_6_;
			}

			if (p_77648_7_ == 3)
			{
				++p_77648_6_;
			}

			if (p_77648_7_ == 4)
			{
				--p_77648_4_;
			}

			if (p_77648_7_ == 5)
			{
				++p_77648_4_;
			}

		}
		{
			if (!p_77648_3_.isRemote)
			{
				if (!Blocks.skull.canPlaceBlockOnSide(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_)) return false;
				p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, ModBlocks.gaiaHead, p_77648_7_, 2); // Gaia head instead of skull
				int i1 = 0;

				if (p_77648_7_ == 1)
				{
					i1 = MathHelper.floor_double(p_77648_2_.rotationYaw * 16.0F / 360.0F + 0.5D) & 15;
				}

				TileEntity tileentity = p_77648_3_.getTileEntity(p_77648_4_, p_77648_5_, p_77648_6_);

				if (tileentity != null && tileentity instanceof TileEntitySkull)
				{
					/*if (p_77648_1_.getItemDamage() == 3)
					{
						GameProfile gameprofile = null;

						if (p_77648_1_.hasTagCompound())
						{
							NBTTagCompound nbttagcompound = p_77648_1_.getTagCompound();

							if (nbttagcompound.hasKey("SkullOwner", 10))
							{
								gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
							}
							else if (nbttagcompound.hasKey("SkullOwner", 8) && nbttagcompound.getString("SkullOwner").length() > 0)
							{
								gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
							}
						}

						((TileEntitySkull)tileentity).func_152106_a(gameprofile);
					}
					else
					{
						((TileEntitySkull)tileentity).func_152107_a(p_77648_1_.getItemDamage());
					}*/

					((TileEntitySkull)tileentity).func_145903_a(i1);
					((BlockSkull)Blocks.skull).func_149965_a(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, (TileEntitySkull)tileentity);
				}

				--p_77648_1_.stackSize;
			}

			return true;
		}
	}

}
