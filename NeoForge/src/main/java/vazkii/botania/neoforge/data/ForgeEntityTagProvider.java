/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.entity.BotaniaEntities;

import java.util.concurrent.CompletableFuture;

public class ForgeEntityTagProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {
	public ForgeEntityTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Registries.ENTITY_TYPE, lookupProvider, (entityType) -> ResourceKey
				.create(Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE.getKey(entityType)),
				BotaniaAPI.MODID, null);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(TagKey.create(Registries.ENTITY_TYPE,
				ResourceLocation.fromNamespaceAndPath("reliquary", "ignored_by_interdiction_torch")))
				.add(BotaniaEntities.MANA_BURST);
	}
}
