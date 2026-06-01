/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.common.damagesource.IDeathMessageProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import vazkii.botania.common.BotaniaDamageTypes;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;

// FIXME: This is not how it's supposed to be done, but I don't feel like extending NeoForge-extendable enums right now
@Mixin(IDeathMessageProvider.class)
public interface IDeathMessageProviderMixin {
	/**
	 * When building the death message due to throwing bread into an Alfheim portal, link the issue to blame for it.
	 */
	@WrapOperation(
		method = "lambda$static$0",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/network/chat/Style;)Lnet/minecraft/network/chat/MutableComponent;"),
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/ComponentUtils;wrapInSquareBrackets(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"),
			to = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;getLocalizedDeathMessage(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/network/chat/Component;")
		)
	)
	private static MutableComponent maybeSelectPortalExplainerStyle(MutableComponent instance, Style style,
			Operation<MutableComponent> original, @Local DamageSource damageSource) {
		return original.call(instance,
				damageSource.is(BotaniaDamageTypes.PORTAL_BREAD_EXPLOSION) ? AlfheimPortalBlockEntity.PORTAL_EXPLOSION_EXPLAINER_STYLE : style);

	}
}
