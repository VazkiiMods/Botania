package vazkii.botania.forge;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

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

	private CapabilityUtil() {}
}
