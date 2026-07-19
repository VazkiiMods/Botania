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
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.hash.HashCode;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.data.HashCache;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import vazkii.botania.fabric.data.FabricDatagenMixinHelper;

import java.nio.file.Path;
import java.util.Map;

/**
 * Patches HashCache to write its in sorted order, so that the datagen cache files can be committed.
 */
@Mixin(HashCache.ProviderCacheBuilder.class)
public class HashCacheProviderCacheBuilderFabricMixin {

	@WrapOperation(
		method = "build",
		at = @At(
			value = "INVOKE",
			target = "Lcom/google/common/collect/ImmutableMap;copyOf(Ljava/util/Map;)Lcom/google/common/collect/ImmutableMap;"
		)
	)
	private ImmutableMap<Path, HashCode> orderEntries(Map<? extends Path, ? extends HashCode> map,
			Operation<ImmutableMap<Path, HashCode>> original) {

		var data = original.call(map);
		return FabricDatagenMixinHelper.isBotaniaDatagen()
				? ImmutableSortedMap.copyOf(data)
				: data;
	}
}
