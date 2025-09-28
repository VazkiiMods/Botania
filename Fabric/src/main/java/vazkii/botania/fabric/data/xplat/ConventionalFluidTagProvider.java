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

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.data.util.DummyTagLookup;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ConventionalFluidTagProvider extends IntrinsicHolderTagsProvider<Fluid> {
	private static final Set<TagKey<Fluid>> RELEVANT_TAGS = Set.of(
			ConventionalFluidTags.WATER, ConventionalFluidTags.LAVA
	);

	public ConventionalFluidTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		//noinspection deprecation
		super(packOutput, Registries.FLUID, lookupProvider, DummyTagLookup.completedFuture(RELEVANT_TAGS),
				fluid -> fluid.builtInRegistryHolder().key());
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(BotaniaTags.Fluids.HYDROANGEAS_CONSUMABLE).addTag(ConventionalFluidTags.WATER);
		this.tag(BotaniaTags.Fluids.THERMALILY_CONSUMABLE).addTag(ConventionalFluidTags.LAVA);
	}

	@Override
	public String getName() {
		return "Conventional " + super.getName();
	}
}
