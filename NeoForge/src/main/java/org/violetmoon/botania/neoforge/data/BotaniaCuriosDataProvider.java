/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.neoforge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import top.theillusivec4.curios.api.CuriosDataProvider;

import vazkii.botania.api.BotaniaAPI;

import java.util.concurrent.CompletableFuture;

public class BotaniaCuriosDataProvider extends CuriosDataProvider {
	public BotaniaCuriosDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(BotaniaAPI.MODID, output, null, registries);
	}

	@Override
	public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
		createEntities("default_player_slots").addPlayer().addSlots(
				"belt", "body", "charm", "head", "necklace", "ring");
		createSlot("ring").operation("ADD").size(1);
	}
}
