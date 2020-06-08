/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.capability.SimpleCapProvider;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibObfuscation;

import java.lang.invoke.MethodHandle;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ExoflameFurnaceHandler {

	@CapabilityInject(IExoflameHeatable.class) public static Capability<IExoflameHeatable> CAPABILITY;
	public static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "exoflame_heatable");

	private static final MethodHandle CAN_SMELT = LibObfuscation.getMethod(AbstractFurnaceTileEntity.class, "func_214008_b", IRecipe.class);
	private static final MethodHandle RECIPE_TYPE = LibObfuscation.getGetter(AbstractFurnaceTileEntity.class, "field_214014_c");

	@SubscribeEvent
	public static void attachFurnaceCapability(AttachCapabilitiesEvent<TileEntity> event) {
		TileEntity te = event.getObject();
		if (te instanceof AbstractFurnaceTileEntity) {
			AbstractFurnaceTileEntity furnace = (AbstractFurnaceTileEntity) te;
			SimpleCapProvider.attach(event, ID, CAPABILITY, new FurnaceExoflameHeatable(furnace));
		}
	}

	public static boolean canSmelt(AbstractFurnaceTileEntity furnace, IRecipe<?> recipe) throws Throwable {
		return (boolean) CAN_SMELT.invokeExact(furnace, recipe);
	}

	@SuppressWarnings("unchecked")
	public static IRecipeType<? extends AbstractCookingRecipe> getRecipeType(AbstractFurnaceTileEntity furnace) throws Throwable {
		return (IRecipeType<? extends AbstractCookingRecipe>) RECIPE_TYPE.invokeExact(furnace);
	}

	private static class FurnaceExoflameHeatable implements IExoflameHeatable {
		private final AbstractFurnaceTileEntity furnace;

		private IRecipeType<? extends AbstractCookingRecipe> recipeType;
		private AbstractCookingRecipe currentRecipe;

		FurnaceExoflameHeatable(AbstractFurnaceTileEntity furnace) {
			this.furnace = furnace;
		}

		@Override
		public boolean canSmelt() {
			if (furnace.getStackInSlot(0).isEmpty()) {
				return false;
			}
			try {
				if (recipeType == null) {
					this.recipeType = ExoflameFurnaceHandler.getRecipeType(furnace);
				}
				if (currentRecipe != null) { // This is already more caching than Mojang does
					if (currentRecipe.matches(furnace, furnace.getWorld())
							&& ExoflameFurnaceHandler.canSmelt(furnace, currentRecipe)) {
						return true;
					}
				}
				currentRecipe = furnace.getWorld().getRecipeManager().getRecipe(recipeType, furnace, furnace.getWorld()).orElse(null);
				return ExoflameFurnaceHandler.canSmelt(furnace, currentRecipe);
			} catch (Throwable t) {
				Botania.LOGGER.error("Failed to determine if furnace TE can smelt", t);
				return false;
			}
		}

		@Override
		public int getBurnTime() {
			return furnace.burnTime;
		}

		@Override
		public void boostBurnTime() {
			if (getBurnTime() == 0) {
				World world = furnace.getWorld();
				BlockPos pos = furnace.getPos();
				world.setBlockState(pos, world.getBlockState(pos).with(BlockStateProperties.LIT, true));
			}
			furnace.burnTime += 200;
		}

		@Override
		public void boostCookTime() {
			furnace.cookTime = Math.min(currentRecipe.getCookTime() - 1, furnace.cookTime + 1);
		}
	}
}
