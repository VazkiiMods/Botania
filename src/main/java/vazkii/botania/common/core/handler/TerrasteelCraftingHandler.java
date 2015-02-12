/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 14, 2014, 4:30:40 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;

// This is legacy code and is no longer used. Check out TileTerraPlate instead.
public final class TerrasteelCraftingHandler {

	private static final String TAG_TIME = "Botania-CraftingTime";
	private static final int TIME = 100;
	private static final int MANA_PER_TICK = (int) (TilePool.MAX_MANA / 2 / TIME * 0.9);

	public static void onEntityUpdate(EntityItem item) {
		ItemStack stack = item.getEntityItem();
		if(stack != null && stack.getItem() == ModItems.manaResource && stack.getItemDamage() == 0) {
			int time = validateCraftingItem(item);

			if(time != -1) {
				doParticles(item, time);
				if(time == TIME)
					item.worldObj.playSoundAtEntity(item, "botania:terrasteelCraft", 1F, 1F);

				getManaFromPools : {
					int x = MathHelper.floor_double(item.posX);
					int y = MathHelper.floor_double(item.posY);
					int z = MathHelper.floor_double(item.posZ);

					int range = 12;

					for(int i = -range; i < range + 1; i++)
						for(int j = -range; j < range + 1; j++)
							for(int k = - range; k < range + 1; k++) {
								TileEntity tile = item.worldObj.getTileEntity(x + i, y + j, z + k);

								if(tile instanceof IManaPool) {
									IManaPool pool = (IManaPool) tile;

									if(!item.worldObj.isRemote && pool.getCurrentMana() >= MANA_PER_TICK) {
										pool.recieveMana(-MANA_PER_TICK);
										item.worldObj.markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
										incrementCraftingTime(item, time);
										break getManaFromPools;
									}
								}
							}
				}
			}
		}
	}

	static int validateCraftingItem(EntityItem item) {
		ItemStack estack = item.getEntityItem();
		if(estack.stackSize != 1)
			return -1;

		int x = MathHelper.floor_double(item.posX);
		int y = MathHelper.floor_double(item.posY);
		int z = MathHelper.floor_double(item.posZ);

		if(item.worldObj.getBlock(x, y - 1, z) != Blocks.beacon)
			return -1;

		TileEntityBeacon beacon = (TileEntityBeacon) item.worldObj.getTileEntity(x, y - 1, z);
		if(beacon.getLevels() <= 0)
			return -1;

		List<EntityItem> items = item.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1));

		EntityItem diamond = null;
		EntityItem pearl = null;

		for(EntityItem otherItem : items) {
			if(otherItem == item)
				continue;

			ItemStack stack = otherItem.getEntityItem();
			if(stack.getItem() == ModItems.manaResource && stack.stackSize == 1) {
				int meta = stack.getItemDamage();
				if(meta == 1) {
					if(pearl == null) {
						pearl = otherItem;
						continue;
					} else return -1;
				} else if(meta == 2) {
					if(diamond == null) {
						diamond = otherItem;
						continue;
					} else return -1;
				} else return -1;
			} else return -1;
		}

		if(diamond != null && pearl != null) {
			int time = getTimeInCrafting(item);
			if(time > 0) {
				diamond.delayBeforeCanPickup = 1;
				diamond.age = 0;
				pearl.delayBeforeCanPickup = 1;
				pearl.age = 0;
				item.delayBeforeCanPickup = 1;
				item.age = 0;
			}

			return time;
		}
		return -1;
	}

	static int getTimeInCrafting(EntityItem item) {
		ItemStack stack = item.getEntityItem();
		return ItemNBTHelper.getInt(stack, TAG_TIME, 0);
	}

	static void doParticles(EntityItem item, int ticks) {
		if(item.worldObj.isRemote) {
			int totalSpiritCount = 3;
			double tickIncrement = 360D / totalSpiritCount;

			int speed = 5;
			double wticks = ticks * speed - tickIncrement;

			double r = Math.sin((ticks - TIME) / 10D) * 2;
			double g = Math.sin(wticks * Math.PI / 180 * 0.55);

			for(int i = 0; i < totalSpiritCount; i++) {
				double x = (int) item.posX + Math.sin(wticks * Math.PI / 180) * r + 0.5;
				double y = (int) item.posY + 0.25;
				double z = (int) item.posZ + Math.cos(wticks * Math.PI / 180) * r + 0.5;

				wticks += tickIncrement;
				float[] colorsfx = new float[] {
						0F, (float) ticks / (float) TIME, 1F - (float) ticks / (float) TIME
				};
				Botania.proxy.wispFX(item.worldObj, x, y, z, colorsfx[0], colorsfx[1], colorsfx[2], 0.85F, (float)g * 0.05F, 0.25F);
				Botania.proxy.wispFX(item.worldObj, x, y, z, colorsfx[0], colorsfx[1], colorsfx[2], (float) Math.random() * 0.1F + 0.1F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, 0.9F);

				if(ticks == TIME)
					for(int j = 0; j < 15; j++)
						Botania.proxy.wispFX(item.worldObj, item.posX, item.posY, item.posZ, colorsfx[0], colorsfx[1], colorsfx[2], (float) Math.random() * 0.15F + 0.15F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F);
			}
		}
	}

	static void incrementCraftingTime(EntityItem item, int time) {
		if(time >= TIME)
			finalizeCraftingRecipe(item);

		else {
			ItemStack stack = item.getEntityItem();
			ItemNBTHelper.setInt(stack, TAG_TIME, time + 1);
		}
	}

	static void finalizeCraftingRecipe(EntityItem item) {
		int x = MathHelper.floor_double(item.posX);
		int y = MathHelper.floor_double(item.posY);
		int z = MathHelper.floor_double(item.posZ);

		List<EntityItem> items = item.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1));
		for(EntityItem otherItem : items)
			if(otherItem != item)
				otherItem.setDead();
			else item.setEntityItemStack(new ItemStack(ModItems.manaResource, 1, 4));
	}

}
