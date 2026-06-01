/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.data.gog;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import vazkii.botania.api.BotaniaAPI;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.gogRL;

public class GogGlobalLootModifierProvider extends GlobalLootModifierProvider {
	public GogGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BotaniaAPI.GOG_MODID);
	}

	@Override
	protected void start() {
		add("extra_seeds", new AddTableLootModifier(new LootItemCondition[] {
				AnyOfCondition.anyOf(
						LootTableIdCondition.builder(Blocks.SHORT_GRASS.getLootTable().location()),
						LootTableIdCondition.builder(Blocks.TALL_GRASS.getLootTable().location())
				).build()
		}, ResourceKey.create(Registries.LOOT_TABLE, gogRL("extra_seeds"))));
	}

}
