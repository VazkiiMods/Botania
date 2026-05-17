/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.fabric.data.xplat;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;

import vazkii.botania.common.entity.BotaniaEntities;

import java.util.concurrent.CompletableFuture;

public class ConventionalEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public ConventionalEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		tag(ConventionalEntityTypeTags.BOSSES).add(reverseLookup(BotaniaEntities.GAIA_GUARDIAN));

		tag(ConventionalEntityTypeTags.MINECARTS).add(reverseLookup(BotaniaEntities.POOL_MINECART));
	}

	@Override
	public String getName() {
		return "Conventional " + super.getName();
	}
}
