package vazkii.botania.forge;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public final class CapabilityUtil {
	public static <T, U extends T> ICapabilityProvider makeProvider(Capability<T> cap, U instance) {
		LazyOptional<T> lazyInstanceButNotReally = LazyOptional.of(() -> instance);
		return new ICapabilityProvider() {
			@Nonnull
			@Override
			public <C> LazyOptional<C> getCapability(@NotNull Capability<C> queryCap, @Nullable Direction side) {
				return cap.orEmpty(queryCap, lazyInstanceButNotReally);
			}
		};
	}

	public static class WaterBowlFluidHandler extends FluidHandlerItemStackSimple.SwapEmpty {
		public WaterBowlFluidHandler(ItemStack stack) {
			super(stack, new ItemStack(Items.BOWL), FluidAttributes.BUCKET_VOLUME);
			setFluid(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME));
		}

		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return false;
		}

		@Override
		public boolean canDrainFluidType(FluidStack fluid) {
			return fluid.getFluid() == Fluids.WATER;
		}
	}

	private CapabilityUtil() {}
}
