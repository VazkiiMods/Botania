/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 11, 2014, 2:57:30 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.common.lib.LibItemNames;

public class ItemGrassHorn extends ItemMod {

	public ItemGrassHorn() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.GRASS_HORN);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int time) {
		if(time != getMaxItemUseDuration(stack) && time % 5 == 0) {
			Random rand = new Random((int) player.posX ^ (int) player.posZ);
			int range = 12;
			List<ChunkCoordinates> coords = new ArrayList();

			for(int i = -range; i < range + 1; i++)
				for(int j = -range; j < range + 1; j++)
					for(int k = -3; k < 4; k++) {
						int x = (int) player.posX + i;
						int y = (int) player.posY + k;
						int z = (int) player.posZ + j;

						Block block = player.worldObj.getBlock(x, y, z);
						if(block instanceof BlockBush && !(block instanceof ISpecialFlower))
							coords.add(new ChunkCoordinates(x, y, z));
					}

			Collections.shuffle(coords, rand);

			int count = Math.min(coords.size(), 32);
			for(int i = 0; i < count; i++) {
				ChunkCoordinates currCoords = coords.get(i);
				List<ItemStack> items = new ArrayList();
				Block block = player.worldObj.getBlock(currCoords.posX, currCoords.posY, currCoords.posZ);
				int meta = player.worldObj.getBlockMetadata(currCoords.posX, currCoords.posY, currCoords.posZ);
				items.addAll(block.getDrops(player.worldObj, currCoords.posX, currCoords.posY, currCoords.posZ, meta, 0));

				if(!player.worldObj.isRemote) {
					player.worldObj.setBlockToAir(currCoords.posX, currCoords.posY, currCoords.posZ);
					player.worldObj.playAuxSFX(2001, currCoords.posX, currCoords.posY, currCoords.posZ, Block.getIdFromBlock(block) + (meta << 12));

					for(ItemStack stack_ : items)
						player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, currCoords.posX + 0.5, currCoords.posY + 0.5, currCoords.posZ + 0.5, stack_));
				}
			}
		}

		if(!player.worldObj.isRemote)
			player.worldObj.playSoundAtEntity(player, "note.bassattack", 1F, 0.001F);
	}
}
