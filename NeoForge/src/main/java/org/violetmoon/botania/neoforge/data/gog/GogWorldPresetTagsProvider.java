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
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.neoforge.data.NeoForgeDatagenInitializer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GogWorldPresetTagsProvider extends TagsProvider<WorldPreset> {
	public GogWorldPresetTagsProvider(PackOutput output,
			CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.WORLD_PRESET, provider, BotaniaAPI.MODID,
				new ExistingFileHelper(List.of(), Set.of(), false, null, null));
	}

	@NotNull
	@Override
	public String getName() {
		return "GoG world preset tags";
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(WorldPresetTags.NORMAL).add(NeoForgeDatagenInitializer.SKYBLOCK_PRESET);
	}
}
