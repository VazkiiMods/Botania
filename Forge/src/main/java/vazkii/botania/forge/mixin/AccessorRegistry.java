package vazkii.botania.forge.mixin;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(Registry.class)
public interface AccessorRegistry {
	@Invoker("registerDefaulted")
	static <T> DefaultedRegistry<T> callRegisterDefaulted(ResourceKey<? extends Registry<T>> registryKey,
			String defaultValueName, Supplier<T> loader) {
		throw new IllegalStateException();
	}
}
