/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.data.HashCache;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import vazkii.botania.fabric.data.FabricDatagenMixinHelper;

import java.nio.file.Path;

/**
 * Patches HashCache to write its results using system-agnostic paths, so that the datagen cache files can be committed.
 */
@Mixin(HashCache.ProviderCache.class)
public abstract class HashCacheProviderCacheFabricMixin {

	@WrapOperation(
		method = "save",
		at = @At(value = "INVOKE", target = "Ljava/nio/file/Path;toString()Ljava/lang/String;")
	)
	private String normalizePath(Path instance, Operation<String> original) {

		String pathString = original.call(instance);
		return FabricDatagenMixinHelper.isBotaniaDatagen()
				? pathString.replace('\\', '/')
				: pathString;
	}
}
