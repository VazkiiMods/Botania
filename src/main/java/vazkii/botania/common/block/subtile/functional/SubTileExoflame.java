/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 19, 2014, 3:42:32 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileExoflame extends SubTileFunctional {

	private static final int RANGE = 5;
	private static final int RANGE_Y = 2;

	@Override
	public void onUpdate() {
		super.onUpdate();

		boolean did = false;
		int cost = 300;

		fireFurnaces : {
			for(int i = -RANGE; i < RANGE + 1; i++)
				for(int j = -RANGE_Y; j < RANGE_Y + 1; j++)
					for(int k = -RANGE; k < RANGE + 1; k++) {
						int x = supertile.xCoord + i;
						int y = supertile.yCoord + j;
						int z = supertile.zCoord + k;

						TileEntity tile = supertile.getWorldObj().getTileEntity(x, y, z);
						Block block = supertile.getWorldObj().getBlock(x, y, z);
						if(tile != null) {
							if(tile instanceof TileEntityFurnace && (block == Blocks.furnace || block == Blocks.lit_furnace)) {
								TileEntityFurnace furnace = (TileEntityFurnace) tile;
								boolean canSmelt = canFurnaceSmelt(furnace);
								if(canSmelt && mana > 2) {
									if(furnace.furnaceBurnTime < 2) {
										if(furnace.furnaceBurnTime == 0)
											BlockFurnace.updateFurnaceBlockState(true, supertile.getWorldObj(), x, y, z);
										furnace.furnaceBurnTime = 200;
										mana = Math.max(0, mana - cost);
									}
									if(ticksExisted % 2 == 0)
										furnace.furnaceCookTime = Math.min(199, furnace.furnaceCookTime + 1);

									did = true;

									if(mana <= 0)
										break fireFurnaces;
								}
							} else if(tile instanceof IExoflameHeatable) {
								IExoflameHeatable heatable = (IExoflameHeatable) tile;

								if(heatable.canSmelt() && mana > 2) {
									if(heatable.getBurnTime() == 0) {
										heatable.boostBurnTime();
										mana = Math.max(0, mana - cost);
									}

									if(ticksExisted % 2 == 0)
										heatable.boostCookTime();

									if(mana <= 0)
										break fireFurnaces;
								}
							}
						}
					}
		}

		if(did)
			sync();
	}

	public static boolean canFurnaceSmelt(TileEntityFurnace furnace){
		if(furnace.getStackInSlot(0) == null)
			return false;
		else {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(furnace.getStackInSlot(0));

			if(itemstack == null)
				return false;

			if(furnace.getStackInSlot(2) == null)
				return true;

			if(!furnace.getStackInSlot(2).isItemEqual(itemstack))
				return false;

			int result = furnace.getStackInSlot(2).stackSize + itemstack.stackSize;
			return result <= 64 && result <= itemstack.getMaxStackSize();
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 300;
	}

	@Override
	public int getColor() {
		return 0x661600;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.exoflame;
	}

}
