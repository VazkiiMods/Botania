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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.Optional;

public class SubTileExoflame extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":exoflame")
	public static TileEntityType<SubTileExoflame> TYPE;

	private static final int RANGE = 5;
	private static final int RANGE_Y = 2;
	private static final int COST = 300;

	public SubTileExoflame() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(getWorld().isRemote)
			return;

		boolean did = false;

		for(BlockPos pos : BlockPos.getAllInBoxMutable(getPos().add(-RANGE, -RANGE_Y, -RANGE),
				getPos().add(RANGE, RANGE_Y, RANGE))) {
			TileEntity tile = getWorld().getTileEntity(pos);
			BlockState state = getWorld().getBlockState(pos);
			Block block = state.getBlock();
			if(tile != null) {
				if(tile instanceof FurnaceTileEntity && block == Blocks.FURNACE) {
					FurnaceTileEntity furnace = (FurnaceTileEntity) tile;
					boolean canSmelt = canFurnaceSmelt(furnace);
					if(canSmelt && mana > 2) {
						if(furnace.burnTime < 2) {
							if(furnace.burnTime == 0)
								getWorld().setBlockState(pos, state.with(BlockStateProperties.LIT, true));
							furnace.burnTime = 200;
							mana = Math.max(0, mana - COST);
						}
						if(ticksExisted % 2 == 0)
							furnace.cookTime = Math.min(199, furnace.cookTime + 1);

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

	public static boolean canFurnaceSmelt(AbstractFurnaceTileEntity furnace){
		if(furnace.getStackInSlot(0).isEmpty())
			return false;
		else {
			Optional<ItemStack> maybeStack = furnace.getWorld().getRecipeManager()
					.getRecipe(IRecipeType.SMELTING, furnace, furnace.getWorld())
					.map(r -> r.getCraftingResult(furnace));

			if(!maybeStack.isPresent() || maybeStack.get().isEmpty())
				return false;

			ItemStack itemstack = maybeStack.get();
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
