/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.client.integration.shared.LocaleHelper;
import vazkii.botania.common.block.block_entity.TinyPotatoBlockEntity;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.Calendar;
import java.util.List;

/**
 * Handle Botania's splash texts.
 *
 * @see BotaniaConfig.ClientConfigAccess#splashesEnabled()
 */
@Mixin(SplashManager.class)
public class SplashManagerMixin {
	@Shadow
	@Final
	private List<String> splashes;

	@Unique
	private boolean botania_alreadyAdded;

	@Inject(
		method = "apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
		at = @At("HEAD")
	)
	private void resetSelf(List<String> object, ResourceManager resourceManager, ProfilerFiller profiler,
			CallbackInfo ci) {
		botania_alreadyAdded = false;
	}

	@Inject(
		method = "getSplash",
		at = @At(value = "INVOKE", target = "Ljava/util/Calendar;get(I)I", ordinal = 0),
		cancellable = true
	)
	private void handleBotaniaSplashes(CallbackInfoReturnable<SplashRenderer> cir, @Local Calendar calendar) {
		var config = BotaniaConfig.client();
		if (config == null || !config.splashesEnabled()) {
			return;
		}

		// override splash for tater's birthday
		if (calendar.get(Calendar.MONTH) + 1 == TinyPotatoBlockEntity.BIRTHDAY.getMonth().getValue()
				&& calendar.get(Calendar.DAY_OF_MONTH) == TinyPotatoBlockEntity.BIRTHDAY.getDayOfMonth()) {
			int age = calendar.get(Calendar.YEAR) - TinyPotatoBlockEntity.BIRTHDAY.getYear();
			cir.setReturnValue(new SplashRenderer("Happy %s birthday, Tiny Potato!"
					.formatted(LocaleHelper.formatAsEnglishOrdinal(age))));
			return;
		}

		// otherwise add our regular splashes to the list, if that didn't already happen
		if (botania_alreadyAdded) {
			return;
		}
		botania_addSplashes();
		botania_alreadyAdded = true;
	}

	@Unique
	private void botania_addSplashes() {
		splashes.addAll(List.of(
				"Do not feed bread to elves!",
				"Drum wild!",
				"Edible textures!",
				"Music by Firel!"
		));
	}
}
