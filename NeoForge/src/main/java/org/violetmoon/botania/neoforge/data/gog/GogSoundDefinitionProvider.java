/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.data.gog;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import static vazkii.botania.api.BotaniaAPI.gogRL;

public class GogSoundDefinitionProvider extends SoundDefinitionsProvider {
	public GogSoundDefinitionProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, "minecraft", helper);
	}

	@Override
	public void registerSounds() {
		ResourceLocation gogMusicId = gogRL("music/garden_of_glass");
		add(SoundEvents.MUSIC_GAME.value(), definition().with(sound(gogMusicId).stream().volume(0.4f)));
		add(SoundEvents.MUSIC_MENU.value(), definition().with(sound(gogMusicId).stream().volume(0.4f).weight(2)));
	}
}
