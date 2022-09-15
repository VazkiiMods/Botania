package vazkii.botania.forge.mixin;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Registry.class)
public interface RegistryForgeAccessor {
	@Invoker("registerDefaulted")
	static <T> DefaultedRegistry<T> callRegisterDefaulted(ResourceKey<? extends Registry<T>> registryName,
			String defaultId,
			Registry.RegistryBootstrap<T> bootstrap) {
		throw new IllegalStateException();
	}
}
