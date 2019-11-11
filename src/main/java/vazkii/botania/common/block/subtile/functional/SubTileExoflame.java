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

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class SubTileExoflame extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":exoflame")
	public static TileEntityType<SubTileExoflame> TYPE;

	private static final int RANGE = 5;
	private static final int RANGE_Y = 2;
	private static final int COST = 300;
	private static final Field RECIPE_TYPE = ObfuscationReflectionHelper.findField(AbstractFurnaceTileEntity.class, "field_214014_c");
	private static final Method CAN_SMELT = ObfuscationReflectionHelper.findMethod(AbstractFurnaceTileEntity.class, "func_214008_b", IRecipe.class);

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
			if(tile instanceof AbstractFurnaceTileEntity) {
				AbstractFurnaceTileEntity furnace = (AbstractFurnaceTileEntity) tile;
				Pair<AbstractCookingRecipe, Boolean> p = canSmelt(furnace);
				if(p == null)
					continue;
				AbstractCookingRecipe recipe = p.getFirst();
				boolean canSmelt = p.getSecond();

				if(canSmelt && mana > 2) {
					if(furnace.burnTime < 2) {
						if(furnace.burnTime == 0)
							getWorld().setBlockState(pos, state.with(BlockStateProperties.LIT, true));
						furnace.burnTime = 200;
						mana = Math.max(0, mana - COST);
					}
					if(ticksExisted % 2 == 0)
						furnace.cookTime = Math.min(recipe.getCookTime() - 1, furnace.cookTime + 1);

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

		if(did)
			sync();
	}

	@Nullable
	public static Pair<AbstractCookingRecipe, Boolean> canSmelt(AbstractFurnaceTileEntity furnace) {
		IRecipeType<AbstractCookingRecipe> rt;
		AbstractCookingRecipe recipe;
		boolean canSmelt;
		try {
			rt = (IRecipeType<AbstractCookingRecipe>) RECIPE_TYPE.get(furnace);
			recipe = furnace.getWorld().getRecipeManager().getRecipe(rt, furnace, furnace.getWorld()).orElse(null);
			canSmelt = (Boolean) CAN_SMELT.invoke(furnace, recipe);
			return Pair.of(recipe, canSmelt);
		} catch (InvocationTargetException | IllegalAccessException e) {
			Botania.LOGGER.error("Failed to reflect furnace", e);
			return null;
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getPos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 300;
	}

	@Override
	public int getColor() {
		return 0x661600;
	}

}
