package vazkii.botania.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import vazkii.botania.common.BotaniaDamageTypes;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;

@Mixin(CombatTracker.class)
public class CombatTrackerMixin {
	/**
	 * When building the death message due to throwing bread into an Alfheim portal, link the issue to blame for it.
	 */
	@WrapOperation(
		method = "getDeathMessage",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/network/chat/Style;)Lnet/minecraft/network/chat/MutableComponent;"),
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/ComponentUtils;wrapInSquareBrackets(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"),
			to = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;getLocalizedDeathMessage(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/network/chat/Component;")
		)
	)
	private MutableComponent maybeSelectPortalExplainerStyle(MutableComponent instance, Style style,
			Operation<MutableComponent> original, @Local DamageSource damageSource) {
		return original.call(instance,
				damageSource.is(BotaniaDamageTypes.PORTAL_BREAD_EXPLOSION) ? AlfheimPortalBlockEntity.PORTAL_EXPLOSION_EXPLAINER_STYLE : style);

	}
}
