package vazkii.botania.forge.mixin;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(Registry.class)
public interface ForgeAccessorRegistry {
	@Invoker("registerDefaulted")
	static <T> DefaultedRegistry<T> callRegisterDefaulted(ResourceKey<? extends Registry<T>> registryName,
			String p_206033_,
			Function<T, Holder.Reference<T>> holderFactory,
			Registry.RegistryBootstrap<T> p_206035_) {
		throw new IllegalStateException();
	}
}
