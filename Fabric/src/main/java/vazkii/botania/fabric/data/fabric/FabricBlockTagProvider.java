/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.fabric.data.fabric;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import vazkii.botania.data.BlockTagProvider;

import java.util.concurrent.CompletableFuture;

public class FabricBlockTagProvider extends BlockTagProvider {

	private static TagKey<Block> blockTag(ResourceLocation location) {
		return TagKey.create(Registries.BLOCK, location);
	}

	public FabricBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		// nothing so far, but required for item tag provider
	}

}
