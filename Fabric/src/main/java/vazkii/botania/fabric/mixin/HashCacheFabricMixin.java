/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.fabric.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.WorldVersion;
import net.minecraft.data.HashCache;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.fabric.data.FabricDatagenMixinHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

/**
 * Patches HashCache to only write cache files that are missing or contain a changed set of hashes.
 */
@Mixin(HashCache.class)
public abstract class HashCacheFabricMixin {
	@Unique
	private Map<String, HashCache.ProviderCache> botania_originalCaches;

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/data/HashCache;caches:Ljava/util/Map;",
			opcode = Opcodes.PUTFIELD
		)
	)
	private void rememberOriginalHashes(Path rootDir, Collection<String> providers, WorldVersion version,
			CallbackInfo ci, @Local Map<String, HashCache.ProviderCache> map) {
		botania_originalCaches = ImmutableMap.copyOf(map);
	}

	@WrapWithCondition(
		method = "method_46571",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/data/HashCache$ProviderCache;save(Ljava/nio/file/Path;Ljava/nio/file/Path;Ljava/lang/String;)V"
		)
	)
	private boolean hasCacheChanged(HashCache.ProviderCache instance, Path rootDir, Path cachePath, String date,
			@Local(argsOnly = true) String providerId) {

		return !FabricDatagenMixinHelper.isBotaniaDatagen()
				|| !instance.equals(botania_originalCaches.get(providerId))
				|| !Files.exists(cachePath);
	}
}
