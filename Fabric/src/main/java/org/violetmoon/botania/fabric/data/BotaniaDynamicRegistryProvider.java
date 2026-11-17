/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.common.BotaniaDamageTypes.*;

public class BotaniaDynamicRegistryProvider extends FabricDynamicRegistryProvider {

	public BotaniaDynamicRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		entries.add(RELIC_DAMAGE, RELIC);
		entries.add(PLAYER_ATTACK_ARMOR_PIERCING, PLAYER_AP);
		entries.add(KEY_EXPLOSION, KEY);
		entries.add(PORTAL_BREAD_EXPLOSION, PORTAL);
	}

	@Override
	public String getName() {
		return "BotaniaDynamicRegistryProvider";
	}
}
