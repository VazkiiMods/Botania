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
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileExoflame extends SubTileFunctional {

	private static final int RANGE = 5;
	private static final int RANGE_Y = 2;
	private static final int COST = 300;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(supertile.getWorld().isRemote)
			return;

		boolean did = false;

		for(BlockPos pos : BlockPos.getAllInBox(getPos().add(-RANGE, -RANGE_Y, -RANGE), getPos().add(RANGE, RANGE_Y, RANGE))) {
			TileEntity tile = supertile.getWorld().getTileEntity(pos);
			Block block = supertile.getWorld().getBlockState(pos).getBlock();
			if(tile != null) {
				if(tile instanceof TileEntityFurnace && (block == Blocks.FURNACE || block == Blocks.LIT_FURNACE)) {
					TileEntityFurnace furnace = (TileEntityFurnace) tile;
					boolean canSmelt = canFurnaceSmelt(furnace);
					if(canSmelt && mana > 2) {
						if(furnace.getField(0) < 2) { // Field 0 -> Burn time
							if(furnace.getField(0) == 0)
								BlockFurnace.setState(true, supertile.getWorld(), pos);
							furnace.setField(0, 200);
							mana = Math.max(0, mana - COST);
						}
						if(ticksExisted % 2 == 0)
							furnace.setField(2, Math.min(199, furnace.getField(2) + 1)); // Field 2 -> cook time

						did = true;

						if(mana <= 0)
							break;
					}
				} else if(tile instanceof IExoflameHeatable) {
					IExoflameHeatable heatable = (IExoflameHeatable) tile;

					if(heatable.canSmelt() && mana > 2) {
						if(heatable.getBurnTime() == 0) {
							heatable.boostBurnTime();
							mana = Math.max(0, mana - COST);
						}

						if(ticksExisted % 2 == 0)
							heatable.boostCookTime();

						if(mana <= 0)
							break;
					}
				}
			}
		}

		if(did)
			sync();
	}

	public static boolean canFurnaceSmelt(TileEntityFurnace furnace){
		if(furnace.getStackInSlot(0).isEmpty())
			return false;
		else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(furnace.getStackInSlot(0));

			if(itemstack.isEmpty())
				return false;

			if(furnace.getStackInSlot(2).isEmpty())
				return true;

			if(!furnace.getStackInSlot(2).isItemEqual(itemstack))
				return false;

			int result = furnace.getStackInSlot(2).getCount() + itemstack.getCount();
			return result <= 64 && result <= itemstack.getMaxStackSize();
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
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
